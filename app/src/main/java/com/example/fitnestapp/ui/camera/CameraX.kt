package com.example.fitnestapp.ui.camera

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.impl.utils.MatrixExt.postRotate
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fitnestapp.databinding.ActivityCameraXBinding
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream

class CameraX : AppCompatActivity() {
    private lateinit var poseDetectorInterpreter: Interpreter
    private lateinit var binding: ActivityCameraXBinding
    private lateinit var imageProcessor: ImageProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraXBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, 0
            )
        }


        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        initializeModel(this)
        startCamera()
    }

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setTargetResolution(Size(1280, 720))

                .build().also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->

                        val paint = Paint()
                        paint.color = Color.YELLOW
                        val bitmap = imageProxy.toBitmaps()
                        imageProxy.close()

                        val poseResult = poseDetector(bitmap)


                        val mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                        val canvas = Canvas(mutable)
                        val h = bitmap.height
                        val w = bitmap.width
                        var x = 0


                        while (x <= 49) {
                            if (poseResult[x + 2] > 0.45f) {
                                canvas.drawCircle(
                                    poseResult[x + 1] * w,
                                    poseResult[x] * h,
                                    10f,
                                    paint
                                )
                            }
                            x += 3
                        }

                        binding.imageView.setImageBitmap(mutable)
                    }
                }

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    imageAnalysis,
                    preview
                )
            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraX,
                    "Gagal memunculkan kamera: ${exc.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    fun ImageProxy.toBitmaps(): Bitmap {
        val yBuffer = planes[0].buffer // Y
        val uBuffer = planes[1].buffer // U
        val vBuffer = planes[2].buffer // V

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        // U dan V diinterleave
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
        val imageBytes = out.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        val rotationDegrees = imageInfo.rotationDegrees
        return rotateBitmap(bitmap, rotationDegrees)
    }

    // Fungsi untuk memutar Bitmap
    fun rotateBitmap(bitmap: Bitmap, rotationDegrees: Int): Bitmap {
        val matrix = Matrix().apply {
            postRotate(rotationDegrees.toFloat()) // Terapkan rotasi
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    private fun initializeModel(context: Context) {
        val options = Interpreter.Options()
        val modelDetector = FileUtil.loadMappedFile(context, "detector.tflite")
        poseDetectorInterpreter = Interpreter(modelDetector, options)
    }


    private fun poseDetector(bitmap: Bitmap): FloatArray {
//        modelDetector = Detector.newInstance(this)
        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)
        tensorImage = imageProcessor.process(tensorImage)

        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)
        val outputFeature0 =
            TensorBuffer.createFixedSize(
                intArrayOf(1, 51), DataType.FLOAT32
            )
        poseDetectorInterpreter.run(inputFeature0.buffer, outputFeature0.buffer)


        val output = outputFeature0.floatArray

        val outputDataText = output.joinToString(separator = ", ", prefix = "[", postfix = "]")
        runOnUiThread {
            Log.d("PoseDetectionOutput", outputDataText)
        }

        return outputFeature0.floatArray

    }


    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
        )

    }
}
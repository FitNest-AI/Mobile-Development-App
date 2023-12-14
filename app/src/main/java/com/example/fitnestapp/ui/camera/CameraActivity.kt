package com.example.fitnestapp.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.impl.utils.CompareSizesByArea
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fitnestapp.R
import com.example.fitnestapp.databinding.ActivityCameraBinding
import com.example.fitnestapp.ml.Detector
import com.example.fitnestapp.ml.PoseClassifier
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.util.Collections

class CameraActivity : AppCompatActivity() {
    val paint = Paint()
    lateinit var imageProcessor: ImageProcessor
    private lateinit var modelDetector: Detector
    private lateinit var modelClassifier: PoseClassifier
    lateinit var bitmap: Bitmap
    lateinit var imageView: ImageView
    lateinit var handler: Handler
    private lateinit var handlerThread: HandlerThread
    lateinit var textureView: TextureView
    private lateinit var cameraManager: CameraManager
    private lateinit var binding: ActivityCameraBinding
    lateinit var feature0TextView : TextView
    lateinit var feature1TextView : TextView
    lateinit var feature2TextView : TextView


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
        }


        imageProcessor =
            ImageProcessor.Builder()
                .add(ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR))
                .build()


        imageView = findViewById(R.id.imageView)
        textureView = findViewById(R.id.textureView)
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)


        paint.setColor(Color.YELLOW)

        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {

            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                bitmap = textureView.bitmap!!


                val poseDetectorOutput = poseDetector(bitmap)


                val mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                val canvas = Canvas(mutable)
                val h = bitmap.height
                val w = bitmap.width
                var x = 0

                Log.d("output__", poseDetectorOutput.size.toString())
                while (x <= 49) {
                    if (poseDetectorOutput.get(x + 2) > 0.45) {
                        canvas.drawCircle(
                            poseDetectorOutput.get(x + 1) * w,
                            poseDetectorOutput.get(x) * h,
                            10f,
                            paint
                        )
                        poseClassifier(poseDetectorOutput)
                    } else {

                    }
                    x += 3
                }

//                poseClassifier(poseDetector(bitmap))
            }
        }

    }


    private fun poseDetector(bitmap: Bitmap): FloatArray {
        modelDetector = Detector.newInstance(this)
        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)
        tensorImage = imageProcessor.process(tensorImage)

        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)

        val outputs = modelDetector.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray


        val outputDataText =
            outputFeature0.joinToString(separator = ", ", prefix = "[", postfix = "]")
        runOnUiThread {
            Log.d("MLData", outputDataText)
        }

        val mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutable)
        val h = bitmap.height
        val w = bitmap.width
        var x = 0

        Log.d("output__", outputFeature0.size.toString())
        while (x <= 49) {
            if (outputFeature0.get(x + 2) > 0.45) {
                canvas.drawCircle(
                    outputFeature0.get(x + 1) * w,
                    outputFeature0.get(x) * h,
                    10f,
                    paint
                )
            } else {
                binding.feature0Text.text = "Not Detected"
                binding.feature1Text.text = "Not Detected"
                binding.feature2Text.text = "Not Detected"
            }
            x += 3
        }

        imageView.setImageBitmap(mutable)

        return outputFeature0
    }

    private fun poseClassifier(outputDetection: FloatArray) {
        modelClassifier = PoseClassifier.newInstance(this@CameraActivity)



        val byteBuffer = ByteBuffer.allocateDirect(4 * 51)
        outputDetection.forEach { value ->
            byteBuffer.putFloat(value)
        }

        val inputFeature0Pose =
            TensorBuffer.createFixedSize(intArrayOf(1, 51), DataType.FLOAT32)
        inputFeature0Pose.loadBuffer(byteBuffer)

        val poseOutputs = modelClassifier.process(inputFeature0Pose)
        val outputFeaature0 = poseOutputs.outputFeature0AsTensorBuffer.floatArray
        val outputFeature1 = poseOutputs.outputFeature1AsTensorBuffer.floatArray
        val outputFeature2 = poseOutputs.outputFeature2AsTensorBuffer.floatArray


        val feature0Labels =
            arrayOf("standing", "jumpingjack", "pushup", "situp", "squat", "front", "side")
        val feature1Labels = arrayOf("front", "side")
        val feature2Labels = arrayOf(
            "rightknee",
            "leftknee",
            "righthip",
            "lefthip",
            "rightelbow",
            "leftelbow"
        )


        fun indexOfMaxValue(arr: FloatArray): Int {
            var maxIndex = -1
            var maxValue = Float.MIN_VALUE

            for (i in arr.indices) {
                if (arr[i] > maxValue) {
                    maxValue = arr[i]
                    maxIndex = i
                }
            }

            return maxIndex
        }


        val maxIndexFeature0 = indexOfMaxValue(outputFeaature0)
        val maxIndexFeature1 = indexOfMaxValue(outputFeature1)
        val maxIndexFeature2 = indexOfMaxValue(outputFeature2)

        val outputDataText0 = outputFeaature0.joinToString(
            separator = ", ",
            prefix = "outputFeature0: [",
            postfix = "]"
        )
        val outputDataText1 = outputFeature1.joinToString(
            separator = ", ",
            prefix = "outputFeature1: [",
            postfix = "]"
        )
        val outputDataText2 = outputFeature2.joinToString(
            separator = ", ",
            prefix = "outputFeature2: [",
            postfix = "]"
        )

        Log.d(
            "MLData",
            "Output Feature 0: ${feature0Labels[maxIndexFeature0]} = $outputDataText0 "
        )
        Log.d(
            "MLData",
            "Output Feature 1: ${feature1Labels[maxIndexFeature1]} = $outputDataText1 "
        )
        Log.d(
            "MLData",
            "Output Feature 2: ${feature2Labels[maxIndexFeature2]} = $outputDataText2 "
        )

        binding.feature0Text.text = feature0Labels[maxIndexFeature0]
        binding.feature1Text.text = feature1Labels[maxIndexFeature1]
        binding.feature2Text.text = feature2Labels[maxIndexFeature1]

    }


    override fun onDestroy() {
        super.onDestroy()
        modelDetector.close()
        modelClassifier.close()
    }

    @SuppressLint("MissingPermission")
    fun openCamera() {
        val cameraId = cameraManager.cameraIdList[0]
        val characteristics = cameraManager.getCameraCharacteristics(cameraId)
        val streamConfigurationMap =
            characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val largestSize = Collections.max(
            listOf(*streamConfigurationMap!!.getOutputSizes(SurfaceTexture::class.java)),
            CompareSizesByArea()
        )

        val rotation = windowManager.defaultDisplay.rotation
        val isPortrait = (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)
        val previewSize = if (isPortrait) {
            Size(largestSize.height, largestSize.width)
        } else {
            largestSize
        }

        textureView.setAspectRatio(previewSize.width, previewSize.height)


        cameraManager.openCamera(
            cameraId,
            object : CameraDevice.StateCallback() {
                override fun onOpened(p0: CameraDevice) {
                    val captureRequest = p0.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    val surface = Surface(textureView.surfaceTexture)
                    captureRequest.addTarget(surface)
                    p0.createCaptureSession(
                        listOf(surface),
                        object : CameraCaptureSession.StateCallback() {
                            override fun onConfigured(p0: CameraCaptureSession) {
                                p0.setRepeatingRequest(captureRequest.build(), null, null)
                            }

                            override fun onConfigureFailed(p0: CameraCaptureSession) {

                            }
                        },
                        handler
                    )
                }

                override fun onDisconnected(p0: CameraDevice) {

                }

                override fun onError(p0: CameraDevice, p1: Int) {

                }
            },
            handler
        )
    }


    fun TextureView.setAspectRatio(width: Int, height: Int) {
        val viewWidth = width
        val viewHeight = height
        if (viewWidth > 0 && viewHeight > 0) {
            if (width > height) {
                this.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            } else {
                this.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            requestLayout()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("CameraActivity", "Izin kamera diberikan")
                    openCamera() // Memperbaiki pemanggilan openCamera
                } else {
                    Log.d("CameraActivity", "Izin kamera ditolak")
                    // Tampilkan pesan bahwa izin kamera diperlukan
                }
            }
        }
    }


    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
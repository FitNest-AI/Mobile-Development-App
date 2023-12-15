package com.example.fitnestapp.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fitnestapp.databinding.ActivityCameraBinding
import com.example.fitnestapp.ml.Detector
import com.example.fitnestapp.ml.PoseClassifier
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer

class CameraActivity : AppCompatActivity() {
    val paint = Paint()
    private lateinit var imageProcessor: ImageProcessor
    private lateinit var modelClassifier: PoseClassifier
    lateinit var bitmap: Bitmap
    lateinit var handler: Handler
    private lateinit var handlerThread: HandlerThread
    private lateinit var cameraManager: CameraManager
    private lateinit var binding: ActivityCameraBinding
    private lateinit var detectorGpuDelegate: GpuDelegate
    private lateinit var poseDetectorInterpreter: Interpreter
    private lateinit var poseClassifierInterpreter: Interpreter


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


        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR))
            .build()


        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        initializeModel(this)

        paint.color = Color.YELLOW

        binding.textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {

            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                bitmap = binding.textureView.bitmap!!


                val poseDetectorOutput = poseDetector(bitmap)


                val mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                val canvas = Canvas(mutable)
                val h = bitmap.height
                val w = bitmap.width
                var x = 0


                while (x <= 49) {
                    if (poseDetectorOutput[x + 2] > 0.45f) {
                        canvas.drawCircle(
                            poseDetectorOutput[x + 1] * w,
                            poseDetectorOutput[x] * h,
                            10f,
                            paint
                        )
                        poseClassifier(poseDetectorOutput)
                    }
                    x += 3
                }

                binding.imageView.setImageBitmap(mutable)



            }
        }

    }


    private fun initializeModel(context: Context) {
//        val detectorOptions = Interpreter.Options().apply {
//            detectorGpuDelegate = GpuDelegate()
//            addDelegate(detectorGpuDelegate)
//        }
        val options = Interpreter.Options()
        val modelDetector = FileUtil.loadMappedFile(context, "detector.tflite")
        val modelClassifier = FileUtil.loadMappedFile(context, "pose_classifier.tflite")
        poseDetectorInterpreter = Interpreter(modelDetector, options)
        poseClassifierInterpreter = Interpreter(modelClassifier, options)

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


    @SuppressLint("MissingPermission")
    fun openCamera() {
        val cameraId = cameraManager.cameraIdList[0]
        cameraManager.openCamera(
            cameraId,
            object : CameraDevice.StateCallback() {
                override fun onOpened(p0: CameraDevice) {
                    val captureRequest = p0.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    val surface = Surface(binding.textureView.surfaceTexture)
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


    override fun onDestroy() {
        super.onDestroy()
        modelClassifier.close()
        handlerThread.quitSafely()
        poseDetectorInterpreter.close()
        poseClassifierInterpreter.close()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
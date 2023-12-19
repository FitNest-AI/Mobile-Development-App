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
//            .add(ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR))
            .add(ResizeOp(192, 192, ResizeOp.ResizeMethod.BILINEAR))
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
                val keypoints = convertToNestedList(poseDetectorOutput)
                val angles = calculateAngle(keypoints)


                val start = listOf(0, 0, 130, 130, 360, 360)
                val end = listOf(90, 90, 50, 50, 360, 360)

//                var allAnglesLessThanOrEqual = true
//
//                for (i in angles.indices) {
//                    if (angles[i] > end[i]) {
//                        allAnglesLessThanOrEqual = false
//                        break
//                    }
//                }
//
//                if (allAnglesLessThanOrEqual) {
//                    Log.d("AngleResult","true")
//                } else {
//                    Log.d("AngleResult","false")
//                }

                if (angles.zip(end).all { it.first <= it.second }) {
                    Log.d("AngleResult","true+$angles")
                } else {
                    Log.d("AngleResult","false+$angles")
                }


                Log.d("Angle", angles.toString())
                Log.d("Keypoints", keypoints.toString())

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
        val modelDetector = FileUtil.loadMappedFile(context, "4.tflite")
        val modelClassifier = FileUtil.loadMappedFile(context, "pose_classifier.tflite")
        poseDetectorInterpreter = Interpreter(modelDetector, options)
        poseClassifierInterpreter = Interpreter(modelClassifier, options)

    }


    private fun poseDetector(bitmap: Bitmap): FloatArray {
//        modelDetector = Detector.newInstance(this)
//        var tensorImage = TensorImage(DataType.FLOAT32)
        var tensorImage = TensorImage(DataType.UINT8)
        tensorImage.load(bitmap)
        tensorImage = imageProcessor.process(tensorImage)

//        val inputFeature0 =
//            TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
//        inputFeature0.loadBuffer(tensorImage.buffer)

        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 192, 192, 3), DataType.UINT8)
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

        val outputs = modelClassifier.process(inputFeature0Pose)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val output = outputFeature0.floatArray

        val outputDataText = output.joinToString(separator = ", ", prefix = "[", postfix = "]")
        runOnUiThread {
            Log.d("PoseDetectionOutput", outputDataText)
        }

        Log.d("PoseClassifierOutput", outputDataText)

        val feature0Labels =
            arrayOf(
                "tricep pushdown",
                "squat",
                "pushup",
                "leg raises",
                "situp",
                "hammer curl",
                "tricep dips",
                "barbell biceps curl",
                "lateral raises",
                "pull up",
                "russian twist",
                "standingSide",
                "hip thrust",
                "standingFront"
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


        val maxIndexFeature0 = indexOfMaxValue(output)


        val outputDataText0 = output.joinToString(
            separator = ", ",
            prefix = "outputFeature0: [",
            postfix = "]"
        )


        Log.d(
            "MLData",
            "Output Feature 0: ${feature0Labels[maxIndexFeature0]} = $outputDataText0 "
        )

        binding.feature0Text.text = feature0Labels[maxIndexFeature0]

    }


    fun calculateAngle(key: List<List<Double>>): List<Double> {
        val rightWrist = key[10]
        val leftWrist = key[9]

        val rightElbow = key[8]
        val leftElbow = key[7]

        val rightShoulder = key[6]
        val leftShoulder = key[5]

        val rightHip = key[12]
        val leftHip = key[11]

        val rightKnee = key[14]
        val leftKnee = key[13]

        val rightAnkle = key[16]
        val leftAnkle = key[15]

        val angleRightWe = Math.toDegrees(
            Math.atan2(
                (rightWrist[1] - rightElbow[1]),
                (rightWrist[0] - rightElbow[0])
            )
        )
        val angleRightEs = Math.toDegrees(
            Math.atan2(
                (rightElbow[1] - rightShoulder[1]),
                (rightElbow[0] - rightShoulder[0])
            )
        )
        val angleRightSh = Math.toDegrees(
            Math.atan2(
                (rightShoulder[1] - rightHip[1]),
                (rightShoulder[0] - rightHip[0])
            )
        )
        val angleRightHk =
            Math.toDegrees(Math.atan2((rightHip[1] - rightKnee[1]), (rightHip[0] - rightKnee[0])))
        val angleRightKa = Math.toDegrees(
            Math.atan2(
                (rightKnee[1] - rightAnkle[1]),
                (rightKnee[0] - rightAnkle[0])
            )
        )

        val angleLeftWe =
            Math.toDegrees(Math.atan2((leftWrist[1] - leftElbow[1]), (leftWrist[0] - leftElbow[0])))
        val angleLeftEs = Math.toDegrees(
            Math.atan2(
                (leftElbow[1] - leftShoulder[1]),
                (leftElbow[0] - leftShoulder[0])
            )
        )
        val angleLeftSh = Math.toDegrees(
            Math.atan2(
                (leftShoulder[1] - leftHip[1]),
                (leftShoulder[0] - leftHip[0])
            )
        )
        val angleLeftHk =
            Math.toDegrees(Math.atan2((leftHip[1] - leftKnee[1]), (leftHip[0] - leftKnee[0])))
        val angleLeftKa =
            Math.toDegrees(Math.atan2((leftKnee[1] - leftAnkle[1]), (leftKnee[0] - leftAnkle[0])))

        var angles = listOf(
            angleRightWe,
            angleRightEs,
            angleRightSh,
            angleRightHk,
            angleRightKa,
            angleLeftWe,
            angleLeftEs,
            angleLeftSh,
            angleLeftHk,
            angleLeftKa
        )
        angles = angles.map { angle -> if (angle > 0) angle else angle + 180 }

        val rightKneeAngle = Math.abs(angles[3] - angles[4])
        val leftKneeAngle = Math.abs(angles[8] - angles[9])

        val rightHipAngle = Math.abs(angles[2] - angles[3])
        val leftHipAngle = Math.abs(angles[7] - angles[8])

        val rightElbowAngle = Math.abs(angles[0] - angles[1])
        val leftElbowAngle = Math.abs(angles[5] - angles[6])

        return listOf(
            rightKneeAngle,
            leftKneeAngle,
            rightHipAngle,
            leftHipAngle,
            rightElbowAngle,
            leftElbowAngle
        )
    }


    private fun convertToNestedList(floatArray: FloatArray): List<List<Double>> {
        val nestedList = mutableListOf<List<Double>>()
        for (i in floatArray.indices step 2) {
            val x = floatArray[i].toDouble()
            val y = floatArray.getOrNull(i + 1)?.toDouble() ?: 0.0
            nestedList.add(listOf(x, y))
        }
        return nestedList
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
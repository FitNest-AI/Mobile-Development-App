//package com.example.fitnestapp.ui.camera
//
//import android.Manifest
//import android.content.ContentValues.TAG
//import android.content.Context
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.PointF
//import android.media.Image
//import android.os.Bundle
//import android.util.Log
//import android.util.Size
//import androidx.appcompat.app.AppCompatActivity
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.example.fitnestapp.databinding.ActivityCameraXBinding
//import org.tensorflow.lite.Interpreter
//import org.tensorflow.lite.gpu.GpuDelegate
//import org.tensorflow.lite.support.common.ops.NormalizeOp
//import org.tensorflow.lite.support.image.ImageProcessor
//import org.tensorflow.lite.support.image.TensorImage
//import org.tensorflow.lite.support.image.ops.ResizeOp
//import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
//import org.tensorflow.lite.support.model.Model
//import java.io.FileInputStream
//import java.nio.MappedByteBuffer
//import java.nio.channels.FileChannel
//import kotlin.math.exp
//import kotlin.math.min
//
//class CameraX : AppCompatActivity() {
//    private lateinit var binding: ActivityCameraXBinding
//    private lateinit var interpreter: Interpreter
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityCameraXBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        if (!hasRequiredPermissions()) {
//            ActivityCompat.requestPermissions(
//                this, CAMERAX_PERMISSIONS, REQUEST_CODE_PERMISSIONS
//            )
//        } else {
//            startCamera()
//        }
//    }
//
//    private fun startCamera() {
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//
//        cameraProviderFuture.addListener({
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//            val preview = Preview.Builder()
//                .build()
//                .also {
//                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
//                }
//
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//            try {
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(
//                    this, cameraSelector, preview
//                )
//                Log.d(TAG, "Camera preview started.")
//            } catch (exc: Exception) {
//                Log.e(TAG, "Use case binding failed", exc)
//            }
//
//        }, ContextCompat.getMainExecutor(this))
//    }
//
//    private fun hasRequiredPermissions(): Boolean {
//        return CAMERAX_PERMISSIONS.all {
//            ContextCompat.checkSelfPermission(
//                applicationContext,
//                it
//            ) == PackageManager.PERMISSION_GRANTED
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == REQUEST_CODE_PERMISSIONS) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startCamera()
//            } else {
//                Log.e(TAG, "Permissions not granted by the user.")
//                finish() // Close the app or alert the user
//            }
//        }
//    }
//
//    sealed class DelegateOptions {
//
//        data class CPU(val numThreads: Int) : DelegateOptions()
//        object GPU : DelegateOptions()
//        object NNAPI : DelegateOptions()
//        object Hexagon : DelegateOptions()
//    }
//
//    private fun createInterpreter(device: Model.Device): Interpreter {
//        val numThreads = 4
//        val options = Interpreter.Options().apply {
//            when (device) {
//                Model.Device.CPU -> setNumThreads(numThreads)
//                Model.Device.GPU -> addDelegate(GpuDelegate())
//                Model.Device.NNAPI -> setUseNNAPI(true)
//            }
//        }
//        return Interpreter(loadModelFile("4.tflite", this), options)
//    }
//
//
//    private fun loadModelFile(path: String, context: Context): MappedByteBuffer {
//        val fileDescriptor = context.assets.openFd(path)
//        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
//        return inputStream.channel.map(
//            FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength
//        )
//    }
//
//    val outputMap = mutableMapOf<Int, Any>()
//
//
//    fun estimatePose(tensorImage: TensorImage): Person {
//        val inputArray = arrayOf(tensorImage.buffer)
//        (0 until interpreter.outputTensorCount).forEach {
//            outputMap[it] = reshapeTo4dArray(interpreter.getOutputTensor(it).shape())
//        }
//        interpreter.runForMultipleInputsOutputs(inputArray, outputMap)
//        // parse outputMap
//        return extractKeyPoints(outputMap, tensorImage.width, tensorImage.height)
//    }
//
//    private fun reshapeTo4dArray(shape: IntArray): Array<Array<Array<FloatArray>>> =
//        Array(shape[0]) { Array(shape[1]) { Array(shape[2]) { FloatArray(shape[3]) } } }
//
//    enum class BodyPart {
//        NOSE, LEFT_EYE, RIGHT_EYE, LEFT_EAR, RIGHT_EAR, LEFT_SHOULDER, RIGHT_SHOULDER,
//        LEFT_ELBOW, RIGHT_ELBOW, LEFT_WRIST, RIGHT_WRIST, LEFT_HIP, RIGHT_HIP,
//        LEFT_KNEE, RIGHT_KNEE, LEFT_ANKLE, RIGHT_ANKLE
//    }
//
//    data class Position(val x: Int, val y: Int)
//    data class KeyPoint(val bodyPart: BodyPart, val position: Position, val score: Float)
//    data class Person(val keyPoints: List<KeyPoint>, val score: Float)
//
//    @Suppress("UNCHECKED_CAST")
//    private fun extractKeyPoints(
//        outputMap: Map<Int, Any>,
//        imageWidth: Int,
//        imageHeight: Int
//    ): Person {
//        val heatMaps = outputMap[0] as Array<Array<Array<FloatArray>>>
//        val offsets = outputMap[1] as Array<Array<Array<FloatArray>>>
//
//        val height = heatMaps[0].size
//        val width = heatMaps[0][0].size
//        val numKeyPoints = heatMaps[0][0][0].size
//
//        val keyPoints = mutableListOf<KeyPoint>()
//        val bodyParts = enumValues<BodyPart>()
//        var totalConfidence = 0f
//        for (keyPoint in 0 until numKeyPoints) {
//            var maxVal = heatMaps[0][0][0][keyPoint]
//            var maxRow = 0
//            var maxCol = 0
//            // Find the (row, col) locations of where the keyPoints are most likely to be.
//            for (row in 0 until height) {
//                for (col in 0 until width) {
//                    if (heatMaps[0][row][col][keyPoint] > maxVal) {
//                        maxVal = heatMaps[0][row][col][keyPoint]
//                        maxRow = row
//                        maxCol = col
//                    }
//                }
//            }
//            val yDisplacement = offsets[0][maxRow][maxCol][keyPoint]
//            val xDisplacement = offsets[0][maxRow][maxCol][keyPoint + numKeyPoints]
//            val yCoord = maxRow / (height - 1).toFloat() * imageHeight + yDisplacement
//            val xCoord = maxCol / (width - 1).toFloat() * imageWidth + xDisplacement
//            val confidence = sigmoid(maxVal)
//            val bodyPart = bodyParts[keyPoint]
//            totalConfidence += confidence
//            keyPoints.add(KeyPoint(bodyPart, Position(xCoord.toInt(), yCoord.toInt()), confidence))
//        }
//
//        return Person(keyPoints, totalConfidence / numKeyPoints)
//    }
//
//    /** Returns a value within [0,1].   */
//    private fun sigmoid(x: Float): Float {
//        return (1.0f / (1.0f + exp(-x)))
//    }
//
//
//    enum class Orientation {
//        HORIZONTAL,
//        VERTICAL
//    }
//
//
//    private val yuvToRgbConverter = YuvToRgbConverter(context.applicationContext)
//
//    data class TransformedImage(
//        val tensorImage: TensorImage,
//        val originalSize: Size,
//        val scaledSize: Size,
//        val paddedSize: Size,
//        val orientation: Orientation
//    )
//
//    fun processImage(
//        image: Image,
//        rotationDegrees: Int,
//        targetWidth: Int, // input tensor size
//        targetHeight: Int // input tensor size
//    ): TransformedImage {
//        val imageBitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
//        yuvToRgbConverter.yuvToRgb(image, imageBitmap)
//        val numRotations = rotationDegrees / 90
//        val scale =
//            min(image.height.toDouble() / targetWidth, image.width.toDouble() / targetHeight)
//        val scaledSize = Size((image.width / scale).toInt(), (image.height / scale).toInt())
//        val orientation = if (numRotations % 2 == 0) {
//            Orientation.HORIZONTAL
//        } else {
//            Orientation.VERTICAL
//        }
//        val imageProcessor = ImageProcessor.Builder()
//            .add(ResizeOp(scaledSize.height, scaledSize.width, ResizeOp.ResizeMethod.BILINEAR))
//            .add(ResizeWithCropOrPadOp(targetHeight, targetWidth))
//            .add(NormalizeOp(127.5f, 127.5f))
//            .build()
//        val tensorImage = TensorImage.fromBitmap(imageBitmap)
//        return TransformedImage(
//            imageProcessor.process(tensorImage),
//            Size(image.width, image.height),
//            scaledSize,
//            Size(targetWidth, targetHeight),
//            orientation
//        )
//    }
//
//
//    private val minConfidence = 0.7f
//
//    fun PoseData.extractKeyPoints(val width: Int, val height: Int): Map<BodyPart, PointF> {
//        val scaledWidth: Int
//        val scaledHeight: Int
//        val originalWidth: Int
//        val originalHeight: Int
//        when (orientation) {
//            Orientation.HORIZONTAL -> {
//                scaledWidth = scaledSize.width
//                scaledHeight = scaledSize.height
//                originalWidth = originalSize.width
//                originalHeight = originalSize.height
//            }
//
//            Orientation.VERTICAL -> {
//                scaledWidth = scaledSize.height
//                scaledHeight = scaledSize.width
//                originalWidth = originalSize.height
//                originalHeight = originalSize.width
//            }
//        }
//        val xOffset = (scaledWidth - paddedSize.width) / 2.0
//        val yOffset = (scaledHeight - paddedSize.height) / 2.0
//
//        // crop or pad to fit current view
//        val originalRatio = originalHeight / originalWidth.toDouble()
//        val widthFactor: Double
//        val heightFactor: Double
//        val xPad: Double
//        val yPad: Double
//        if (width * originalRatio >= height) {
//            // width is the basis
//            xPad = .0
//            yPad = (height - width * originalRatio) / 2
//            widthFactor =
//                (width / originalWidth.toDouble()) * originalWidth / scaledWidth.toDouble()
//            heightFactor =
//                (width * originalRatio / originalHeight.toDouble()) * originalHeight / scaledHeight.toDouble()
//        } else {
//            xPad = (width - height / originalRatio) / 2
//            yPad = .0
//            widthFactor =
//                ((height / originalRatio) / originalWidth.toDouble()) * originalWidth / scaledWidth.toDouble()
//            heightFactor =
//                (height / originalHeight.toDouble()) * originalHeight / scaledHeight.toDouble()
//        }
//
//        return person.keyPoints
//            .asSequence()
//            .filter { it.score > minConfidence }
//            .map {
//                it.bodyPart to it.position.toAdjustedPoints(
//                    widthFactor,
//                    heightFactor,
//                    xOffset,
//                    yOffset,
//                    xPad,
//                    yPad
//                )
//            }
//            .toMap()
//    }
//
//    private fun Position.toAdjustedPoints(
//        widthFactor: Double,
//        heightFactor: Double,
//        xOffset: Double,
//        yOffset: Double,
//        xPad: Double,
//        yPad: Double
//    ) = PointF(
//        ((x + xOffset) * widthFactor + xPad).toFloat(),
//        ((y + yOffset) * heightFactor + yPad).toFloat()
//    )
//
//
//    private var pointMap: Map<BodyPart, PointF> = emptyMap()
//        set(value) {
//            field = value
//            invalidate()
//        }
//
//    private val circleRadius = 8.0f
//    private val circlePaint: Paint = Paint().apply {
//        color = Color.WHITE
//        strokeWidth = 8.0f
//    }
//
//    fun updatePoseData(poseData: PoseData) {
//        pointMap = poseData.extractKeyPoints()
//    }
//
//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//        canvas ?: return
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
//        pointMap.forEach { entry ->
//            entry.values.forEach { canvas.drawCircle(it.x, it.y, circleRadius, circlePaint) }
//        }
//    }
//
//
//    companion object {
//        private val CAMERAX_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
//        private const val REQUEST_CODE_PERMISSIONS = 10
//    }
//}

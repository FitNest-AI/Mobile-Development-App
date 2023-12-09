package com.example.fitnestapp.ui.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fitnestapp.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraManager: CameraManager
    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private var previewRequestBuilder: CaptureRequest.Builder? = null

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

        val surfaceView = binding.surfaceView
        val surfaceHolder = surfaceView.holder


        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        surfaceHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                openCamera(holder)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                // Reconfigure the camera preview if needed
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraDevice?.close()
            }
        })
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
                    openCamera(binding.surfaceView.holder) // Memperbaiki pemanggilan openCamera
                } else {
                    Log.d("CameraActivity", "Izin kamera ditolak")
                    // Tampilkan pesan bahwa izin kamera diperlukan
                }
            }
        }
    }

    private fun openCamera(holder: SurfaceHolder) {
        try {
            val cameraId =
                cameraManager.cameraIdList[1] // Pilih ID kamera, misalnya kamera belakang
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val screenWidth = displayMetrics.widthPixels
            val screenHeight = displayMetrics.heightPixels

            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val supportedSizes = map?.getOutputSizes(SurfaceHolder::class.java)
            // Di sini, Anda dapat memilih resolusi pratinjau yang sesuai dengan layar Anda
            val selectedSize = chooseOptimalSize(supportedSizes, screenWidth, screenHeight)

            // Set proporsi aspek pratinjau sesuai dengan ukuran yang dipilih
            holder.setFixedSize(selectedSize.width, selectedSize.height)


            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    createCameraPreviewSession(camera, holder.surface)
                }

                override fun onDisconnected(camera: CameraDevice) {
                    camera.close()
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    camera.close()
                }
            }, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun chooseOptimalSize(sizes: Array<Size>?, width: Int, height: Int): Size {
        val desiredRatio = height.toDouble() / width.toDouble()

        var selectedSize: Size? = null
        var minDiff = Double.MAX_VALUE

        for (size in sizes ?: emptyArray()) {
            val ratio = size.width.toDouble() / size.height.toDouble()
            val diff = Math.abs(ratio - desiredRatio)
            if (diff < minDiff) {
                selectedSize = size
                minDiff = diff
            }
        }

        return selectedSize ?: sizes?.get(0) ?: Size(
            640,
            480
        ) // Default size jika tidak ada yang cocok
    }

    private fun createCameraPreviewSession(camera: CameraDevice, surface: Surface) {
        try {
            previewRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            previewRequestBuilder?.addTarget(surface)

            camera.createCaptureSession(
                listOf(surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        Log.d("CameraActivity", "Sesi pratinjau dikonfigurasi")

                        if (cameraDevice == null) return

                        captureSession = session
                        previewRequestBuilder?.set(
                            CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                        )
                        val previewRequest = previewRequestBuilder?.build()
                        session.setRepeatingRequest(previewRequest!!, null, null)
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        Log.e("CameraActivity", "Create capture session failed")
                    }
                },
                null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            Log.e("CameraActivity", "Exception saat membuat sesi pratinjau", e)
        }
    }

    override fun onPause() {
        super.onPause()
        captureSession?.close()
        cameraDevice?.close()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
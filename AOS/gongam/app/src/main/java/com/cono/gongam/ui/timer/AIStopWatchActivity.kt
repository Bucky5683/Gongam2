package com.cono.gongam.ui.timer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.SurfaceTexture
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView

import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.cono.gongam.R
import com.cono.gongam.data.InferenceResult
import com.cono.gongam.ui.OverlayView
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection

import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AIStopWatchActivity : AppCompatActivity() {
    private val TAG = "AIActivity"

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aistop_watch)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview: Preview = Preview.Builder()
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        preview.setSurfaceProvider(findViewById<PreviewView>(R.id.preview_view).surfaceProvider)

        // ImageAnalysis : 카메라 스트림에서 이미지를 가져와 사용자가 지정한 분석 작업을 수행할 수 있도록 해 줌
        // setAnalyzer를 통해 분석기를 설정(분석기는 각 프레임에 대해 호출될 분석 작업을 정의)
        // 카메라에 바인딩: bindToLifecycle 메서드를 통해 ImageAnalysis를 카메라 lifecycle과 바인딩
        // 그 후에 analyzer로 객체를 감지하고 처리하는 로직 구현 후 UI에 표시 작업
        val imageAnalysis = ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                    // ML Kit 얼굴 감지와 통합
                    val mediaImage = imageProxy.image
//                    val planes = imageProxy.planes
//                    val buffer = planes[0].buffer
//                    Log.d(TAG, "bufferSize : ${buffer.remaining()}")
//                    val pixelStride = planes[0].pixelStride
//                    val rowStride = planes[0].rowStride
//                    val width = imageProxy.width
//                    val height = imageProxy.height
//                    val yPlaneSize = width * height
//                    val uvPlaneSize = (width / 2) * (height / 2)
//                    val totalSize = yPlaneSize + (2 * uvPlaneSize)
//                    Log.d(TAG, "Total Size: $totalSize")

                    val inputImage = mediaImage?.let { it1 ->
                        InputImage.fromMediaImage(
                            it1,
                            imageProxy.imageInfo.rotationDegrees
                        )
                    }
//                    val inputImage = InputImage.fromByteBuffer(buffer, width, height, imageProxy.imageInfo.rotationDegrees, InputImage.IMAGE_FORMAT_NV21)
                    val detector = FaceDetection.getClient()
                    if (inputImage != null) {
                        detector.process(inputImage)
                            .addOnSuccessListener { faces ->
                                // 얼굴 감지 관련 로직 처리(얼굴 주변에 사각형 그리기)
                                val overlay = findViewById<OverlayView>(R.id.overlay_view)
                                overlay.clear()
                                for (face in faces) {
                                    val bounds = face.boundingBox
                                    overlay.addRect(bounds)
                                }
                            }
                            .addOnFailureListener { e ->
                                // 에러 처리
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    }
                }
            }

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
    }

}
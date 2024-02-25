package com.cono.gongam.ui.timer

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
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
import com.cono.gongam.model.TensorFlowLiteModelRunner
import com.cono.gongam.ui.OverlayView
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AIStopWatchActivity : AppCompatActivity() {
    private val TAG = "AIActivity"
    private lateinit var cameraView: PreviewView

    private lateinit var objectDetector: ObjectDetector

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aistop_watch)

        cameraView = findViewById(R.id.cameraView)

        // 카메라 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            initializeCamera()
        }
    }

    @OptIn(ExperimentalGetImage::class) private fun processCameraFrame(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            // TensorFLow Lite 모델을 사용하여 객체 감지 수행
            objectDetector.process(inputImage)
                .addOnSuccessListener { detectedObjects ->
                    displayDetectedObjects(detectedObjects)
                }
                .addOnFailureListener { e ->
                    // 객체 감지 실패 시 처리
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    private fun initializeCamera() {
        // TensorFlow Lite 모델 로드
        objectDetector = ObjectDetection.getClient(ObjectDetectorOptions.DEFAULT_OPTIONS)

        // CameraX 초기화
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // 카메라 미리 보기 설정
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(cameraView.surfaceProvider)

            // 카메라 선택 및 분석기 설정
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalyzer.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                // 카메라 프레임 처리
                processCameraFrame(imageProxy)
            }

            // 카메라에 미리 보기 및 분석기 바인딩
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여되었을 때 카메라 초기화
                initializeCamera()
            } else {
                // 권한이 거부되었을 때 사용자에게 설명을 제공하거나 다시 요청할 수 있습니다.
                // 이 예시에서는 간단히 권한이 필요한 이유를 설명하는 다이얼로그를 표시합니다.
                AlertDialog.Builder(this)
                    .setMessage("카메라 액세스 권한이 필요합니다.")
                    .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }

    private fun displayDetectedObjects(detectedObjects: List<DetectedObject>) {
//        val overlay = cameraView.overlay
//        overlay.clear()

        for (detectedObject in detectedObjects) {
            val boundingBox = detectedObject.boundingBox
            val labels = detectedObject.labels
            Log.d(TAG, "- 바운딩 박스: $boundingBox, labels : $labels")
            for (label in labels) {
                val text = label.text
                val confidence = label.confidence
                Log.d(TAG, "- 레이블: $text, 신뢰도: $confidence")
            }
        }
    }

}
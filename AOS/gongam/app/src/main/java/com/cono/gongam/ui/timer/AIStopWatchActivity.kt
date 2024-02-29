package com.cono.gongam.ui.timer

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cono.gongam.R
import com.cono.gongam.data.AIStopWatchViewModel
import com.cono.gongam.databinding.ActivityAistopWatchBinding
import com.cono.gongam.utils.TimeUtils
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection

class AIStopWatchActivity : AppCompatActivity() {
    private val TAG = "AIActivity"
    private var isStarted: Boolean = false

    private lateinit var binding: ActivityAistopWatchBinding
    private lateinit var viewModel: AIStopWatchViewModel
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAistopWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(AIStopWatchViewModel::class.java)

        observeViewModel()

        binding.tvMain.setOnClickListener {
            finish()
        }
        binding.btnStart.setOnClickListener {
            if (isStarted) {
                binding.btnStart.setBackgroundResource(R.drawable.round_button_blue)
                binding.btnStart.text = getString(R.string.start)
            } else {
                binding.btnStart.setBackgroundResource(R.drawable.round_button_red)
                binding.btnStart.text = getString(R.string.stop)
            }
            isStarted = !isStarted
        }

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun observeViewModel() {
        viewModel.startCounting()
        viewModel.seconds.observe(this) { seconds ->
            val times = TimeUtils.convertSecondsToTimeInTriple(seconds)
            binding.tvHoursCount.text = times.first
            binding.tvMinutesCount.text = times.second
            binding.tvSecondsCount.text = times.third
        }
    }

    @OptIn(ExperimentalGetImage::class)
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview: Preview = Preview.Builder()
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        preview.setSurfaceProvider(findViewById<PreviewView>(R.id.preview_view).surfaceProvider)

        /**
         * ImageAnalysis : 카메라 스트림에서 이미지를 가져와 사용자가 지정한 분석 작업을 수행할 수 있도록 해 줌
         * setAnalyzer를 통해 분석기를 설정(분석기는 각 프레임에 대해 호출될 분석 작업을 정의)
         * 카메라에 바인딩: bindToLifecycle 메서드를 통해 ImageAnalysis를 카메라 lifecycle과 바인딩
         * 그 후에 analyzer로 객체를 감지하고 처리하는 로직 구현 후 UI에 표시 작업
         */
        val imageAnalysis = ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                    // ML Kit 얼굴 감지와 통합
                    val mediaImage = imageProxy.image

                    val inputImage = mediaImage?.let { it1 ->
                        InputImage.fromMediaImage(
                            it1,
                            imageProxy.imageInfo.rotationDegrees
                        )
                    }
                    val detector = FaceDetection.getClient()
                    if (inputImage != null) {
                        detector.process(inputImage)
                            .addOnSuccessListener { faces ->
                                if (faces.isNotEmpty()) {
                                    binding.tvIsFaceDetected.text = "얼굴 인식중"
                                    binding.tvNoFaceDetected.visibility = View.GONE
                                    if (isStarted) {
                                        viewModel.setFaceDetected(true)
                                    }
                                } else {
                                    if (isStarted) {
                                        binding.tvIsFaceDetected.text = "인식된 얼굴 없음"
                                        binding.tvNoFaceDetected.visibility = View.VISIBLE
                                    }
                                    viewModel.setFaceDetected(false)
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
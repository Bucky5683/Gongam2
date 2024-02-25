package com.cono.gongam.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import java.io.FileInputStream
import java.nio.channels.FileChannel

class TensorFlowLiteModelRunner(context: Context) {
    private lateinit var interpreter: Interpreter
    private val NUM_DETECTIONS = 5

//    init {
//        // 모델 파일 경로 설정
//        val modelFile = context.assets.openFd("model.tflite").createInputStream()
//        val modelChannel = modelFile.channel
//        val modelBuffer = modelChannel.map(FileChannel.MapMode.READ_ONLY, 0, modelChannel.size())
//        interpreter = Interpreter(modelBuffer)
//    }
//
//    fun detectObjects(bitmap: Bitmap): List<ObjectDetectionResult> {
//        // TODO :: 여기에 객체 감지를 위한 코드 추가
//        // TensorFlowLite 모델을 사용하여 객체 감지를 수행하고 결과를 반환
//        // 감지된 객체의 이치 및 레이블을 포함하는 ObjectDetectionResult 리스트를 반환
//
//
//        fun detectObjects(bitmap: Bitmap): List<ObjectDetectionResult> {
//            val inputTensorImage = TensorImage.fromBitmap(bitmap)
//
//            // 입력 이미지를 모델에 공급하여 추론 수행
//            val outputs = runInference(inputTensorImage)
//
//            // 추론 결과를 해석하여 객체 감지 결과로 변환
//            val detectionResults = mutableListOf<ObjectDetectionResult>()
//            val outputLocations = outputs[0].asMultiDimensionalArray()
//            val outputClasses = outputs[1].asMultiDimensionalArray()
//            val outputScores = outputs[2].asMultiDimensionalArray()
//            val numDetections = outputs[3].asMultiDimensionalArray()
//
//            for (i in 0 until numDetections[0][0].toInt()) {
//                val score = outputScores[0][i]
//                if (score >= MIN_CONFIDENCE_THRESHOLD) {
//                    val label = labels[outputClasses[0][i].toInt()]
//                    val topLeftX = outputLocations[0][i][1] * bitmap.width
//                    val topLeftY = outputLocations[0][i][0] * bitmap.height
//                    val bottomRightX = outputLocations[0][i][3] * bitmap.width
//                    val bottomRightY = outputLocations[0][i][2] * bitmap.height
//                    val rect = Rect(topLeftX.toInt(), topLeftY.toInt(), bottomRightX.toInt(), bottomRightY.toInt())
//                    detectionResults.add(ObjectDetectionResult(label, score, rect))
//                }
//            }
//
//            return detectionResults
//        }
//
//    }
//
//    data class ObjectDetectionResult(
//        val label: String,
//        val confidence: Float,
//        val boundingBox: Rect,
//        val location: Rect
//    )
//
//    // 모델 실행하는 함수 정의
//    fun runInference(inputImage: TensorImage): Array<out Any> {
//        // 추론 실행
//        // input data를 모델에 전달하고 결과를 반환
//        // interpreter.run(inputData, outputData)
//        // TODO :: Implement model inference
//
//        val inputBuffer = TensorImage.fromBitmap(bitmap)
//        interpreter.run(inputBuffer.buffer, )
//    }
}
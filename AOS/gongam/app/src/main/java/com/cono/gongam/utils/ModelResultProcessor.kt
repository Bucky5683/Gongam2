package com.cono.gongam.utils

import com.cono.gongam.data.InferenceResult

class ModelResultProcessor {
    fun processInferenceResult(output: FloatArray): InferenceResult {
        val personDetected = output.maxOrNull() ?: 0f > 0.5f // 감지된 객체 중 가장 확률이 높은 것이 사람인지 확인
        return InferenceResult(personDetected)
    }
}
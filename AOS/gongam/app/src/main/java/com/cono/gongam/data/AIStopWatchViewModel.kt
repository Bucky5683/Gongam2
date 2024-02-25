package com.cono.gongam.data

import androidx.lifecycle.ViewModel
import com.cono.gongam.model.TensorFlowLiteModelRunner
import com.google.common.base.Stopwatch

class AIStopWatchViewModel : ViewModel() {
    private lateinit var model: TensorFlowLiteModelRunner
    private lateinit var stopwatch: Stopwatch

//    fun init(model: TensorFlowLiteModelRunner) {
//        this.model = model
//        stopwatch = Stopwatch()
//    }
}
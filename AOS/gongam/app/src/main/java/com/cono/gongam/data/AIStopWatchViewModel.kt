package com.cono.gongam.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.common.base.Stopwatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AIStopWatchViewModel : ViewModel() {
    private var job: Job? = null
    private var isFaceDetected: Boolean = false
    private var _seconds = MutableLiveData<Int>()
    val seconds: LiveData<Int>
        get() = _seconds

    fun startCounting() {
        job = CoroutineScope(Dispatchers.Default).launch {
            var count = 0
            while (isActive) {
                if (isFaceDetected) {
                    delay(1000)
                    count++
                    _seconds.postValue(count)
                } else {
                    delay(1000)
                }
            }
        }
    }
    fun stopCounting() {
        job?.cancel()
    }

    fun setFaceDetected(isDetected: Boolean) {
        isFaceDetected = isDetected
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
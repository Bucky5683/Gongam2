package com.cono.gongam.data

import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    private val _remainingTime = MutableLiveData<Triple<Int, Int, Int>>()
    private var _sumSeconds = MutableLiveData(0)
    private var _isStopped = MutableLiveData(false)

    private var _hour by mutableIntStateOf(0)
    private var _minute by mutableIntStateOf(0)
    private var _second by mutableIntStateOf(0)

    val remainingTime: LiveData<Triple<Int, Int, Int>> get() = _remainingTime
    val sumSeconds: LiveData<Int> get() = _sumSeconds
    val isStopped: LiveData<Boolean> get() = _isStopped

    val hour: Int get() = _hour
    val minute: Int get() = _minute
    val second: Int get() = _second

    fun setHour(hour: Int) {
        _hour = hour
    }
    fun setMinute(minute: Int) {
        _minute = minute
    }
    fun setSecond(second: Int) {
        _second = second
    }
    fun setIsStopped(stopped: Boolean) {
        _isStopped.value = stopped
    }

    fun startCountDown() {
        if (_isStopped.value == false) {
            viewModelScope.launch {
                var remainingHour = _hour
                var remainingMinute = _minute
                var remainingSecond = _second

                while ((remainingHour > 0 || remainingMinute > 0 || remainingSecond >= 0) && _isStopped.value == false) {
                    _remainingTime.value = Triple(remainingHour, remainingMinute, remainingSecond)
                    delay(1000)
                    _sumSeconds.value = _sumSeconds.value?.plus(1)

                    if (remainingSecond > 0) {
                        remainingSecond--
                    } else {
                        if (remainingMinute > 0) {
                            remainingMinute--
                            remainingSecond = 59
                        }
                        else {
                            if (remainingHour > 0) {
                                remainingHour--
                                remainingMinute = 59
                                remainingSecond = 59
                            } else break
                        }
                    }
                }
            }
        }
    }

}
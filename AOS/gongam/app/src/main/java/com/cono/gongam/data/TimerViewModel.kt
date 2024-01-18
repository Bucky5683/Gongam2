package com.cono.gongam.data

import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Handler

class TimerViewModel : ViewModel() {
    private val _remainingTime = MutableLiveData<Triple<Int, Int, Int>>()
    private var _sumSeconds = MutableLiveData(0)
    val remainingTime: LiveData<Triple<Int, Int, Int>> get() = _remainingTime
    val sumSeconds: LiveData<Int> get() = _sumSeconds

    private val handler = android.os.Handler(Looper.getMainLooper())
    private var isCountingDown = false


    private var _hour by mutableIntStateOf(0)
    private var _minute by mutableIntStateOf(0)
    private var _second by mutableIntStateOf(0)

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

    fun startCountDown() {
        if (!isCountingDown) {
            isCountingDown = true
            viewModelScope.launch {
                var remainingHour = _hour
                var remainingMinute = _minute
                var remainingSecond = _second

                while (remainingHour > 0 || remainingMinute > 0 || remainingSecond >= 0) {
                    _remainingTime.value = Triple(remainingHour, remainingMinute, remainingSecond)
                    delay(1000)
                    _sumSeconds.value = _sumSeconds.value?.plus(1)

                    // 수정된 부분: 0초에서도 마지막에 0초로 표시되도록 수정
                    if (remainingSecond > 0) {
                        remainingSecond--
                    } else { // 초 == 0
                        if (remainingMinute > 0) {
                            remainingMinute--
                            remainingSecond = 59
                        }
                        else { // 초 == 0 and 분 == 0
                            if (remainingHour > 0) {
                                remainingHour--
                                remainingMinute = 59
                                remainingSecond = 59
                            } else break
                        }
                    }
                }

                isCountingDown = false
            }
        }
    }

}
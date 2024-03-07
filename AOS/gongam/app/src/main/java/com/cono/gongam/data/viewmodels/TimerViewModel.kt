package com.cono.gongam.data.viewmodels

import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cono.gongam.utils.DateUtils
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
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

    fun setSumSeconds(seconds: Int) {
        _sumSeconds.value = seconds
        Log.d("TimerViewModel", "setSumSeconds 0 -> _sumSeconds.value : ${_sumSeconds.value}")
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
                    Log.d("TimerViewModel", "ticking : _sumSeconds.value : ${_sumSeconds.value}")

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

    fun updateSecondsInDatabase(uid: String) {
        val studyTimes = _sumSeconds.value ?: 0
        Log.d("TimerViewModel", "studyTimesVal : $studyTimes")
        val todayDate = DateUtils.getCurrentDate()

        val studyDataRef = Firebase.database.getReference("StudyDataes").child(uid)
        val todayRef = studyDataRef.child(todayDate)

        todayRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val timerStudyTimeRef = todayRef.child("timerStudyTime")
                val totalStudyTimeRef = todayRef.child("totalStudyTime")
                val stopwatchStudyTimeRef = todayRef.child("stopwatchStudyTime")

                Log.d("TimerViewModel", "update today DB -> $studyTimes")
                if (studyTimes != 0) {
                    val currentTimerStudyTime = snapshot.child("timerStudyTime").getValue(Int::class.java) ?: 0
                    val currentTotalStudyTime = snapshot.child("totalStudyTime").getValue(Int::class.java) ?: 0
                    timerStudyTimeRef.setValue(currentTimerStudyTime + studyTimes)
                    totalStudyTimeRef.setValue(currentTotalStudyTime + studyTimes)
                }

                if (!snapshot.exists()) {
                    stopwatchStudyTimeRef.setValue(0)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TimerViewModel", "Database Error : $error")
            }

        })

        val userRef = Firebase.database.getReference("Users").child(uid)
        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val timerStudyTimeRef = userRef.child("timerStudyTime")
                val todayStudyTimeRef = userRef.child("todayStudyTime")

                Log.d("TimerViewModel", "update User DB -> $studyTimes")
                if (studyTimes != 0) {
                    val currentTimerStudyTime = snapshot.child("timerStudyTime").getValue(Int::class.java) ?: 0
                    val currentTodayStudyTime = snapshot.child("todayStudyTime").getValue(Int::class.java) ?: 0

                    timerStudyTimeRef.setValue(currentTimerStudyTime + studyTimes)
                    todayStudyTimeRef.setValue(currentTodayStudyTime + studyTimes)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TimerViewModel", "Database Error : $error")
            }

        })
    }

}
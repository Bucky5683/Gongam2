package com.cono.gongam.data.viewmodels

import android.util.Log
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

class StopWatchViewModel : ViewModel() {
    private val _sumSeconds = MutableLiveData(0)
    private val _isStopped = MutableLiveData(true)

    val sumSeconds: LiveData<Int> get() = _sumSeconds
    val isStopped: LiveData<Boolean> get() = _isStopped

    fun setIsStopped(stopped: Boolean) {
        _isStopped.value = stopped
    }

    fun setSumSeconds(seconds: Int) {
        _sumSeconds.value = seconds
    }

    fun startCounting() {
        if (_isStopped.value == false) {
            viewModelScope.launch {
                while (_isStopped.value == false) {
                    delay(1000)
                    _sumSeconds.value = _sumSeconds.value?.plus(1)
                }
            }
        }
    }

    fun updateSecondsInDatabase(uid: String) {
        val studyTimes = _sumSeconds.value ?: 0
        val todayDate = DateUtils.getCurrentDate()

        val studyDataRef = Firebase.database.getReference("StudyDataes").child(uid)
        val todayRef = studyDataRef.child(todayDate)

        todayRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val timerStudyTimeRef = todayRef.child("timerStudyTime")
                val totalStudyTimeRef = todayRef.child("totalStudyTime")
                val stopwatchStudyTimeRef = todayRef.child("stopwatchStudyTime")

                val currentStopWatchStudyTime = snapshot.child("stopwatchStudyTime").getValue(Int::class.java) ?: 0
                val currentTotalStudyTime = snapshot.child("totalStudyTime").getValue(Int::class.java) ?: 0

                stopwatchStudyTimeRef.setValue(currentStopWatchStudyTime + studyTimes)
                totalStudyTimeRef.setValue(currentTotalStudyTime + studyTimes)

                if (!snapshot.exists()) {
                    timerStudyTimeRef.setValue(0)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("StopWatchViewModel", "Database Error : $error")
            }

        })

        val userRef = Firebase.database.getReference("Users").child(uid)
        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stopwatchStudyTimeRef = userRef.child("stopwatchStudyTime")
                val todayStudyTimeRef = userRef.child("todayStudyTime")

                val currentStopWatchTime = snapshot.child("stopwatchStudyTime").getValue(Int::class.java) ?: 0
                val currentTodayStudyTime = snapshot.child("todayStudyTime").getValue(Int::class.java) ?: 0

                stopwatchStudyTimeRef.setValue(currentStopWatchTime + studyTimes)
                todayStudyTimeRef.setValue(currentTodayStudyTime + studyTimes)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("StopWatchViewModel", "Database Error : $error")
            }

        })
    }
}
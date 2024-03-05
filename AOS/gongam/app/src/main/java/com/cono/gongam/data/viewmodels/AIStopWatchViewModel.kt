package com.cono.gongam.data.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cono.gongam.utils.DateUtils
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
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

    fun updateSecondsInDatabase(uid: String) {
        val aiStudyTime = _seconds.value?: 0
        Log.d("AIActivity", "_seconds.value : ${_seconds.value}")
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

                stopwatchStudyTimeRef.setValue(currentStopWatchStudyTime + aiStudyTime)
                totalStudyTimeRef.setValue(currentTotalStudyTime + aiStudyTime)

                if (!snapshot.exists()) { // 오늘 공부 기록 없음
                    timerStudyTimeRef.setValue(0)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("AIActivity", "Database Error : $error")
            }

        })

        val userRef = Firebase.database.getReference("Users").child(uid)

        userRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                val timerStudyTimeRef = userRef.child("timerStudyTime")
                val stopwatchStudyTimeRef = userRef.child("stopwatchStudyTime")
                val todayStudyTimeRef = userRef.child("todayStudyTime")

//                val currentTimerStudyTime = snapshot.child("timerStudyTime").getValue(Int::class.java) ?: 0
                val currentStopWatchTime = snapshot.child("stopwatchStudyTime").getValue(Int::class.java) ?: 0
                val currentTodayStudyTime = snapshot.child("todayStudyTime").getValue(Int::class.java) ?: 0

                stopwatchStudyTimeRef.setValue(currentStopWatchTime + aiStudyTime)
                todayStudyTimeRef.setValue(currentTodayStudyTime + aiStudyTime)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
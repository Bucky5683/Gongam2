package com.cono.gongam.data

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

    private fun updateSecondsInDatabase(uid: String) {
        val aiStudyTime = _seconds.value?: 0
        val todayDate = DateUtils.getCurrentDate()

        val studyDataRef = Firebase.database.getReference("StudyDataes").child(uid)
        val todayRef = studyDataRef.child(todayDate)

        todayRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val timerStudyTimeRef = todayRef.child("timerStudyTime")
                val totalStudyTimeRef = todayRef.child("totalStudyTime")
                val stopwatchStudyTimeRef = todayRef.child("stopwatchStudyTime")

                val currentTimerStudyTime = snapshot.child("timerStudyTime").getValue(Int::class.java) ?: 0
                val currentTotalStudyTime = snapshot.child("totalStudyTime").getValue(Int::class.java) ?: 0

                timerStudyTimeRef.setValue(currentTimerStudyTime + aiStudyTime)
                totalStudyTimeRef.setValue(currentTotalStudyTime + aiStudyTime)

                if (!snapshot.exists()) { // 오늘 공부 기록 없음
                    stopwatchStudyTimeRef.setValue(0)
                }
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
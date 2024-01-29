package com.cono.gongam.data

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
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class StudyDatesViewModel : ViewModel() {
    private val _studyDatesList = MutableLiveData<List<Pair<String, StudyDates>>>()
    private val _thisWeekStudyDate = MutableLiveData<List<Pair<String, StudyDates>>?>()
    private val _averageThisWeek = MutableLiveData<Int>()

    val studyDatesList: LiveData<List<Pair<String, StudyDates>>> get() = _studyDatesList
    val thisWeekStudyDate: LiveData<List<Pair<String, StudyDates>>?> get() = _thisWeekStudyDate
    val averageThisWeek: LiveData<Int> get() = _averageThisWeek

//    fun getThisWeekStudyData(): List<Pair<String, StudyDates>>? {
//        return thisWeekStudyData
//    }

    fun getThisWeekStudyDataWithDay(): MutableList<Pair<Int, StudyDates>> {
        val pairDayStudy : MutableList<Pair<Int, StudyDates>> = mutableListOf()

        _thisWeekStudyDate.value?.let {
            for ((dateString, studyDates) in it) {
                pairDayStudy.add(Pair(DateUtils.getDayFromDateString(dateString), studyDates))
            }
        }

        return pairDayStudy
    }

    fun updateStudyDates(uid: String) {
        viewModelScope.launch {
            _studyDatesList.value = getStudyDatesDataFromFirebaseDB(uid)
            Log.d("StudyDatesDBData", "studyDatesList : ${studyDatesList.value}")

            updateThisWeekStudyData()
        }
    }

    private fun calculateThisWeekStudyTimesAverage() {
        var totalStudyTimeThisWeek = 0

        _thisWeekStudyDate.value?.let { data ->
            for ((_, studyTimes) in data) {
                totalStudyTimeThisWeek += studyTimes.totalStudyTime
            }
        }

        _averageThisWeek.value = if (_thisWeekStudyDate.value?.isNotEmpty() == true) {
            totalStudyTimeThisWeek / _thisWeekStudyDate.value!!.size
        } else 0
    }

    fun getAverageThisWeek(): Int {
        return _averageThisWeek.value ?: 0
    }

    private fun updateThisWeekStudyData() {
        val startDate = DateUtils.getStartOfWeek()
        val endDate = DateUtils.getEndOfWeek()
        Log.d("StudyDatesDBData", "startDate: $startDate, endDate: $endDate")

        val thisWeekData = _studyDatesList.value?.filter {
            val date = LocalDate.parse(it.first, DateUtils.dateFormatter)
            date in startDate..endDate
        }
        _thisWeekStudyDate.value = thisWeekData
        Log.d("StudyDatesDBData", "This week study data: $thisWeekData")

        calculateThisWeekStudyTimesAverage()
    }

    private suspend fun getStudyDatesDataFromFirebaseDB(uid: String): List<Pair<String, StudyDates>> {
        return suspendCoroutine { continuation ->
            val userStudyDatesRef = Firebase.database.getReference("StudyDataes").child(uid)
            val studyDatesList = mutableListOf<Pair<String, StudyDates>>()

            userStudyDatesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        val date = child.key
                        val childData = child.getValue(StudyDates::class.java)
                        Log.d("StudyDatesDBData", "date : $date, childData : $childData")
                        if (date != null && childData != null) {
                            studyDatesList.add(Pair(date, childData))
                        }
                    }

                    continuation.resume(studyDatesList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("StudyDatesDBData", "Error occured: ${error.message}")
                    continuation.resumeWithException(error.toException())
                }
            })
        }
    }
}
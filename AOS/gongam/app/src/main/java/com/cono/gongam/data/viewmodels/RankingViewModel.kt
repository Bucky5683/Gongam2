package com.cono.gongam.data.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cono.gongam.data.RankUser
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RankingViewModel : ViewModel() {
    private val _rankUserList = MutableLiveData<List<RankUser>>()
//    private var userRank: String = ""
    private val _userRank = MutableLiveData<String>()
//    private var studyTimeAverage: Int = 0
    private val _studyTimeAverage = MutableLiveData<Int>()
    val rankUserList: LiveData<List<RankUser>> get() = _rankUserList
    val userRank: LiveData<String> get() = _userRank
    val studyTimeAverage: LiveData<Int> get() = _studyTimeAverage

    fun updateRankUserList() {
        viewModelScope.launch {
            val fetchedRankUserList = getRankingDataFromFirebaseDB()
            val sortedList = sortRankUserListByTotalStudyTime(fetchedRankUserList)
            _rankUserList.value = sortedList
            Log.d("TestRankingViewModel", "updated : ${rankUserList.value}")
        }
    }

    fun setUserRank(email: String) {
        Log.d("TestRankingViewModel", "setUserRank ->>")
        val rankIndex = rankUserList.value?.indexOfFirst { it.email == email }
        _userRank.value = if (rankIndex != null) {
            (rankIndex + 1).toString()
        } else ""
        Log.d("TestRankingViewModel", "setUserRank ->> $rankIndex")
    }

    fun setStudyTimeAverage() {
        val users = rankUserList.value

        if (users == null) {
            updateRankUserList()
        } else {
            val totalStudyTimeSum = users.mapNotNull { it.totalStudyTime }.sum()
            val average =
                if (users.isNotEmpty()) totalStudyTimeSum.toDouble() / users.size
                else 0.0
            _studyTimeAverage.value = average.toInt()
        }
    }

    fun getUserRank(): String {
        return userRank.value ?: ""
    }

    fun getStudyTimeAverage(): Int {
        return studyTimeAverage.value ?: 0
    }

    private suspend fun getRankingDataFromFirebaseDB(): List<RankUser> {
        return suspendCoroutine { continuation ->
            val rankRef = Firebase.database.getReference("Rank")
            val rankUserList = mutableListOf<RankUser>()

            rankRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        val childData = child.getValue(RankUser::class.java)
//                        Log.d("RankDBData", "$childData")
                        if (childData != null) {
                            rankUserList.add(childData)
                        }
                    }

                    continuation.resume(rankUserList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("RankDBData", "Error occurred: ${error.message}")
                    continuation.resumeWithException(error.toException())
                }
            })
        }
    }

    private fun sortRankUserListByTotalStudyTime(list: List<RankUser>): List<RankUser> {
        return list.sortedByDescending { it.totalStudyTime ?: 0 }
    }
}
package com.cono.gongam.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RankingViewModel @Inject constructor() : ViewModel() {
    private val _rankUserList = MutableLiveData<List<RankUser>>()
    val rankUserList: LiveData<List<RankUser>> get() = _rankUserList

    fun updateRankUserList() {
        viewModelScope.launch {
            val fetchedRankUserList = getRankingDataFromFirebaseDB()
            val sortedList = sortRankUserListByTotalStudyTime(fetchedRankUserList)
            _rankUserList.value = sortedList
            Log.d("TestRankingViewModel", "updated : ${rankUserList.value}")
        }
    }

    fun getUserRank(email: String): String {
        val rankIndex = rankUserList.value?.indexOfFirst { it.email == email }
        if (rankIndex != null) {
            return (rankIndex + 1).toString()
        } else return ""
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

//    fun getRankingDataFromFirebaseDB() {
//        viewModelScope.launch {
//            val fetchedRankUserList = getRankingData()
////            updateRankUserList(fetchedRankUserList)
//        }
//    }
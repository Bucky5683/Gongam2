package com.cono.gongam.data.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cono.gongam.data.User
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException

class UserViewModel(uid: String) : ViewModel() {
    private val _currentUser = MutableLiveData<User>()
//    private var currentUser: User? = null
    private val _userProfileURL = MutableLiveData<String>()
    val currentUser: LiveData<User> get() = _currentUser
    val userProfileURL: LiveData<String> get() = _userProfileURL

    init {
        val userRef = Firebase.database.getReference("Users").child(uid)

        userRef.child("stopwatchStudyTime").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                updateUserValue(snapshot) { user, value ->
                    user.stopwatchStudyTime = value
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("UserViewModel", "Error fetching stopwatchStudyTime")
            }
        })

        userRef.child("timerStudyTime").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                updateUserValue(snapshot) { user, value ->
                    user.timerStudyTime = value
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("UserViewModel", "Error fetching stopwatchStudyTime")
            }
        })

        userRef.child("todayStudyTime").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                updateUserValue(snapshot) { user, value ->
                    user.todayStudyTime = value
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("UserViewModel", "Error fetching stopwatchStudyTime")
            }
        })
    }

    private fun updateUserValue(snapshot: DataSnapshot, updateFunction: (User, Int) -> Unit) {
        val user = _currentUser.value ?: User()
        val value = snapshot.getValue(Int::class.java) ?: 0
        updateFunction(user, value)
        _currentUser.value = user

        Log.d("UserViewModel", "currentUser :: 스톱워치 : ${_currentUser.value!!.stopwatchStudyTime}, 타이머 : ${_currentUser.value!!.timerStudyTime},  오늘공부시간: ${_currentUser.value!!.todayStudyTime}")
    }


    fun setCurrentUser(user: User) {
//        currentUser = user
        _currentUser.value = user
        Log.d("UserViewModel", "사용자 설정됨 : $currentUser, ProfileImage :: ${currentUser.value?.profileImageURL}")
        _userProfileURL.value = user.profileImageURL ?: ""
    }

    fun getCurrentUser(): User? {
        return currentUser.value
    }

//    fun clearCurrentUser() {
//        currentUser. = null
//    }

    fun getProfileImageURL(): String? {
//        Log.d("UserViewModel", "getProfileImage :: ${currentUser!!.profileImageURL}")
        return _userProfileURL.value
    }

    fun setProfileImageURL(newProfileImageURL: String) {
        currentUser.value?.profileImageURL = newProfileImageURL
        _userProfileURL.value = newProfileImageURL
    }

    suspend fun uploadImageToFirebase(imageUri: Uri){
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        val imagesRef: StorageReference = storageRef.child(imageUri.lastPathSegment ?: "imageName.jpg")
        try {
            withContext(Dispatchers.IO) {
                imagesRef.putFile(imageUri).await()
            }
            val downloadUrl = imagesRef.downloadUrl.await().toString()
            setProfileImageURL(downloadUrl)
        } catch (e: IOException) {
            Log.d("FirebaseStorage", "get error")
        }
    }

    fun setProfileImageURLToFirebase(uid: String, selectedImageUrl: String) {
        Firebase.database.getReference("Users").child(uid).child("profileImageURL")
            .setValue(selectedImageUrl)
            .addOnSuccessListener {
                Log.d("FirebaseDatabase", "profileImageURL 업데이트 성공")
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseDatabase", "profileImageURL 업데이트 실패", e)
            }
    }
}
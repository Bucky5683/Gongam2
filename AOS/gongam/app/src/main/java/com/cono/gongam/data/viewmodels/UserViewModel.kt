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

class UserViewModel : ViewModel() {
    private val _currentUser = MutableLiveData<User>()
//    private var currentUser: User? = null
    private val _userProfileURL = MutableLiveData<String>()
    val currentUser: LiveData<User> get() = _currentUser
    val userProfileURL: LiveData<String> get() = _userProfileURL


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
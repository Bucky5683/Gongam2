package com.cono.gongam.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException

class UserViewModel : ViewModel() {
    private val _currentUser = MutableLiveData<User>()
//    private var currentUser: User? = null
    val currentUser: LiveData<User> get() = _currentUser

    fun setCurrentUser(user: User) {
//        currentUser = user
        _currentUser.value = user
        Log.d("UserViewModel", "사용자 설정됨 : $currentUser, ProfileImage :: ${currentUser.value?.profileImageURL}")
    }

    fun getCurrentUser(): User? {
        return currentUser.value
    }

//    fun clearCurrentUser() {
//        currentUser. = null
//    }

    fun getProfileImageURL(): String? {
//        Log.d("UserViewModel", "getProfileImage :: ${currentUser!!.profileImageURL}")
        return currentUser.value?.profileImageURL
    }

    suspend fun uploadImageToFirebase(imageUri: Uri): String {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        val imagesRef: StorageReference = storageRef.child("images/${imageUri.lastPathSegment}")
        return try {
            withContext(Dispatchers.IO) {
                imagesRef.putFile(imageUri).await()
            }
            val downloadUrl = imagesRef.downloadUrl.await().toString()
            downloadUrl
        } catch (e: IOException) {
            Log.d("FirebaseStorage", "get error")
            ""
        }
    }
}
package com.cono.gongam.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
}
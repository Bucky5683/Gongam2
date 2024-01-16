package com.cono.gongam.data

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class UserViewModel @Inject constructor() : ViewModel() {
    private var currentUser: User? = null

    fun setCurrentUser(user: User) {
        currentUser = user
        Log.d("UserViewModel", "사용자 설정됨 : $currentUser, ProfileImage :: ${currentUser!!.profileImageURL}")
    }

    fun getCurrentUser(): User? {
        return currentUser
    }

    fun clearCurrentUser() {
        currentUser = null
    }

    fun getProfileImageURL(): String? {
//        Log.d("UserViewModel", "getProfileImage :: ${currentUser!!.profileImageURL}")
        return currentUser?.profileImageURL
    }
}
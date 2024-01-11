package com.cono.gongam.data

import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private var currentUser: User? = null

    fun setCurrentUser(user: User) {
        currentUser = user
    }

    fun getCurrentUser(): User? {
        return currentUser
    }

    fun clearCurrentUser() {
        currentUser = null
    }
}
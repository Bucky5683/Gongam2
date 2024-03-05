package com.cono.gongam.utils

import android.content.Context
import android.content.SharedPreferences
import com.cono.gongam.data.User

class SharedPreferencesUtil(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveUser(user: User, uid: String) {
        editor.putString("email", user.email)
        editor.putInt("goalStudyTime", user.goalStudyTime ?: 0)
        editor.putString("lastUpdateDate", user.lastUpdateDate)
        editor.putString("name", user.name)
        editor.putString("profileImageURL", user.profileImageURL)
        editor.putInt("stopwatchStudyTime", user.stopwatchStudyTime ?: 0)
        editor.putInt("timerStudyTime", user.timerStudyTime ?: 0)
        editor.putInt("todayStudyTime", user.todayStudyTime ?: 0)

        editor.putString("uid", uid)
        editor.apply()
    }

    fun getUser(): User {
        return User(
            email = sharedPreferences.getString("email", ""),
            goalStudyTime = sharedPreferences.getInt("goalStudyTime", 0),
            lastUpdateDate = sharedPreferences.getString("lastUpdateDate", ""),
            name = sharedPreferences.getString("name", ""),
            profileImageURL = sharedPreferences.getString("profileImageURL", ""),
            stopwatchStudyTime = sharedPreferences.getInt("stopwatchStudyTime", 0),
            timerStudyTime = sharedPreferences.getInt("timerStudyTime", 0),
            todayStudyTime = sharedPreferences.getInt("todayStudyTime", 0)
        )
    }

    fun getUid(): String {
        return sharedPreferences.getString("uid", "") ?: ""
    }

    fun getGoalTime(): Int {
        return sharedPreferences.getInt("goalStudyTime", 0)
    }

    fun clearUser() {
        editor.clear().apply()
    }
}
package com.cono.gongam.data

data class User(
    val email: String? = null, // from google
    val goalStudyTime: Int? = null,
    val lastUpdateDate: String? = null,
    val name: String? = null, // from google
    val profileImageURL: String? = null, // from google
    val stopwatchStudyTime: Int? = null,
    val timerStudyTime: Int? = null,
    val todayStudyTime: Int? = null,
)

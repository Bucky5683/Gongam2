package com.cono.gongam.data

data class User(
    var email: String? = "", // from google
    var goalStudyTime: Int? = 0,
    var lastUpdateDate: String? = "", // 회원가입 or 프로필 변경 시 업데이트
    var name: String? = "", // from google
    var profileImageURL: String? = "", // from google
    var stopwatchStudyTime: Int? = 0,
    var timerStudyTime: Int? = 0,
    var todayStudyTime: Int? = 0,
)

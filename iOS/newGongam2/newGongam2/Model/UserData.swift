//
//  UserData.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import Foundation

struct UserData: Identifiable, Hashable, Codable{
    var id = UUID()
    var userProfile: String = ""
    var userName: String = ""
    var todayTotalStudyTime: Int = 0
    var purposeTime: Int = 0
}

extension Date {
    var sevenDaysOut: Date {
        Calendar.autoupdatingCurrent.date(byAdding: .day, value: 7, to: self) ?? self
    }
    
    var thirtyDaysOut: Date {
        Calendar.autoupdatingCurrent.date(byAdding: .day, value: 30, to: self) ?? self
    }
}

public enum TimerType{
    case timer
    case stopWatch
}

public enum weeklyDay{
    case Mon
    case Tue
    case Wen
    case Thu
    case Fri
    case Sat
    case Sun
}

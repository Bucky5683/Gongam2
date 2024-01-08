//
//  UserData.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import Foundation
import UIKit

class UserData: ObservableObject{
    var id = UUID()
    var profileImageURL: String = ""
    var name: String = ""
    var todayTotalStudyTime: Int = 0
    var goalStudyTime: Int = 0
    var todayStopwatchStudyTime: Int = 0
    var todayTimerStudyTime: Int = 0
    var profileImage = UIImage()
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

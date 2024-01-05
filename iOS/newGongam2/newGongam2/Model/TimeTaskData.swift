//
//  TimeTaskData.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import Foundation

//스톱워치 또는 타이머가 종료될 경우 이 데이터가 생성되어 저장 후, 유저데이터에 저장.
struct TimeTaskData: Identifiable, Hashable{
    var id = UUID()
    var timerType = TimerType.timer
    var studyTime: Int = 0
    var date: Date
    var week = weeklyDay.Mon
    
    init(timerType: TimerType, studyTime: Int, date: Date, week: weeklyDay){
        self.timerType = timerType
        self.studyTime = studyTime
        self.date = date
        self.week = week
    }
}

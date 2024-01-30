//
//  AITimerViewModel.swift
//  newGongam2
//
//  Created by 김서연 on 1/30/24.
//

import SwiftUI
import UIKit
import AVFoundation
import Vision

class AITimerViewModel: ObservableObject, Equatable {
    @Published var hours: Int = 0
    @Published var minutes: Int = 0
    @Published var seconds: Int = 0
    @Published var isStarted: Bool = true   //타이머 버튼을 눌렀는지 안눌렀는지 true : 아직 안누름, false : 누름
    @Published var timerFinished: Bool = true
    @Published var timerTime: Int = 0
    @Published var isPaused: Bool = false   //일시정지 true : 정지됨, false : 정지안됨
    @Published var timer: Timer? = nil
    
    static func == (lhs: AITimerViewModel, rhs: AITimerViewModel) -> Bool {
        // 여기에 동등성 비교 로직을 추가하세요.
        // 예를 들어, 각 속성들을 비교하거나, 특정 조건에 따라 동등 여부를 판단할 수 있습니다.
        return lhs.hours == rhs.hours && lhs.minutes == rhs.minutes && lhs.seconds == rhs.seconds && lhs.timerTime == rhs.timerTime && lhs.timerFinished == rhs.timerFinished && lhs.isStarted == rhs.isStarted
    }
    
    var notificationService: NotificationService = .init()
    
    func updateFirebase(userData: UserData, userTimeData: UserTimeData, isTimerFinished: Bool){
        if isTimerFinished {
            userData.todayStudyTime += self.timerTime
            userData.stopwatchStudyTime += self.timerTime
            userTimeData.totalStudyTime += self.timerTime
            userData.lastUpdateDate = Date().getCurrentDateAsString()
            userTimeData.updateTimeData(stopwatch: userData.stopwatchStudyTime, timer: userData.timerStudyTime)
            userData.setUserDataPartly(type: .stopwatchStudyTime, data: userData.stopwatchStudyTime)
            userData.setUserDataPartly(type: .todayStudyTime, data: userData.todayStudyTime)
            userData.setUserDataPartly(type: .lastUpdateDate, data: userData.lastUpdateDate)
            userTimeData.uploadTimeData(stopwatch: userData.stopwatchStudyTime, timer: userData.timerStudyTime)
            userTimeData.uploadRankData()
            self.isStarted = true
        }
    }
    
    func calculateReMainTime(goalTime: Int, todayStudyTime: Int) -> Int{ //목표시간에서 현재 공부한 시간을 빼, 0보다 크면 남은 시간을, 아니면 -1을 반환
        let remainTime = goalTime - todayStudyTime
        if remainTime > 0{
            return remainTime
        } else {
            return -1
        }
    }
    
    func pausedTimer(){
        if self.isPaused {
            self.startTimer()
            self.isPaused = false
        } else {
            self.timer?.invalidate()
            self.timer = nil
            self.isPaused = true
        }
    }
    
    func startTimer(){
        guard timer == nil else {return}
        
        timer = Timer.scheduledTimer(
            withTimeInterval: 1,
            repeats: true
        ) { _ in
            DispatchQueue.main.async {
                // @Published 속성 업데이트
                self.timerTime += 1
                self.seconds = self.timerTime % 60
                self.minutes = (self.timerTime - self.seconds) % 60
                self.hours = (self.timerTime - self.seconds - (self.minutes * 60))
                self.timerFinished = false
            }
        }
    }
    
    func stopTimer(){
        DispatchQueue.main.async {
            self.timer?.invalidate()
            self.timer = nil
            self.timerFinished = true
        }
    }
}

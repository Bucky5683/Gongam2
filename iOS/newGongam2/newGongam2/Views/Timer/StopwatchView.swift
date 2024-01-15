//
//  StopwatchView.swift
//  newGongam2
//
//  Created by 김서연 on 1/11/24.
//

import SwiftUI

class StopwatchViewModel: ObservableObject {
    @Published var hours: Int = 0
    @Published var minutes: Int = 0
    @Published var seconds: Int = 0
    @Published var isStarted: Bool = true
    @Published var timerFinished: Bool = false
    @Published var timerTime: Int = 0
    @Published var timer: Timer? = nil
    
    var notificationService: NotificationService = .init()
    
    func updateFirebase(userData: UserData, userTimeData: UserTimeData, isTimerFinished: Bool){
        if isTimerFinished {
            userData.todayStudyTime += self.timerTime
            userData.stopwatchStudyTime += self.timerTime
            userTimeData.totalStudyTime += self.timerTime
            userTimeData.updateTimeData(stopwatch: userData.stopwatchStudyTime, timer: userData.timerStudyTime)
            userData.setUserDataPartly(type: .stopwatchStudyTime, data: userData.stopwatchStudyTime)
            userData.setUserDataPartly(type: .todayStudyTime, data: userData.todayStudyTime)
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
    
    func startTimer(userData: UserData, userTimeData: UserTimeData){
        guard timer == nil else {return}
        
        timer = Timer.scheduledTimer(
            withTimeInterval: 1,
            repeats: true
        ) { _ in
            self.timerTime += 1
            self.seconds = self.timerTime % 60
            self.minutes = (self.timerTime - self.seconds) % 60
            self.hours = (self.timerTime - self.seconds - (self.minutes * 60))
            self.timerFinished = false
        }
    }
    
    func stopTimer(){
        self.timer?.invalidate()
        self.timer = nil
        self.timerFinished = true
    }
}

struct StopwatchView: View {
    @EnvironmentObject var userData: UserData
    @EnvironmentObject var userTimeData: UserTimeData
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @ObservedObject var viewModel: StopwatchViewModel = StopwatchViewModel()
    
    var body: some View {
        VStack{
            if self.viewModel.isStarted == false{
                Text("📚😴📚")
            } else{
                Text("📚🙇📚")
            }
            HStack {
                Text("\(String(format: "%02d", self.viewModel.hours/3600))")
                Text(":")
                Text("\(String(format: "%02d", self.viewModel.minutes/60))")
                Text(":")
                Text("\(String(format: "%02d", self.viewModel.seconds))")
            }
            HStack{
                Text("오늘 목표")
                Spacer()
                Text("\(self.userData.goalStudyTime)")
            }
            if self.viewModel.isStarted {
                HStack{
                    Spacer()
                    Text("목표까지")
                    let remainTime = viewModel.calculateReMainTime(goalTime: self.userData.goalStudyTime, todayStudyTime: self.userData.todayStudyTime)
                    if remainTime > 0{
                        Text("\(remainTime.timeToText())")
                        Text("남았어요")
                    } else {
                        Text("완료했어요")
                    }
                    Spacer()
                }
            }
            ZStack {
                if self.viewModel.isStarted {
                    Button {
                        self.viewModel.startTimer(userData: self.userData, userTimeData: self.userTimeData)
                        self.viewModel.isStarted = false
                    } label: {
                        Text("START")
                    }.disabled(self.viewModel.isStarted == false)
                    
                } else {
                    Button {
                        self.viewModel.stopTimer()
                        self.viewModel.updateFirebase(userData: self.userData, userTimeData: self.userTimeData, isTimerFinished: self.viewModel.timerFinished)
                        self.viewModel.isStarted = true
                    } label: {
                        Text("STOP")
                    }.disabled(self.viewModel.isStarted == true)
                }
            }
            if self.viewModel.isStarted == false {
                Text("STOP을 누르면 시간이 저장돼요!")
            }
        }
        .background(.darkBlue414756, ignoresSafeAreaEdges: .all)
        .navigationBarHidden(!self.viewModel.isStarted)
        .navigationBarTitle("스톱워치",displayMode: .inline)
        .navigationBarBackButtonHidden(true)
        .navigationBarItems(leading: Button("Main"){self.coordinator.pop()})
    }
}

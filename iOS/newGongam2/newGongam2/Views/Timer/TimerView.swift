//
//  TimerView.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import SwiftUI

enum timeType {
    case hour
    case minute
    case second
}

//MARK: ViewModel
class TimeViewModel: ObservableObject {
    @Published var hours: Int = 0
    @Published var minutes: Int = 0
    @Published var seconds: Int = 0
    @Published var isStarted: Bool = true
    @Published var timerFinished: Bool = false
    private var timeRemaining: Int = 0
    @Published var timerTime: Int = 0
    @Published var timer: Timer? = nil
    
    var notificationService: NotificationService = .init()
    
    func downGestureTime(type: timeType){
        switch(type){
        case .hour:
            self.hours -= 3600
            if self.hours < 0 {
                self.hours = 0
            }
        case .minute:
            self.minutes -= 60
            if self.minutes < 0 {
                self.minutes = 0
            }
        case .second:
            self.seconds -= 1
            if self.seconds < 0 {
                self.seconds = 0
            }
        }
        self.timeRemaining = self.hours + self.minutes + self.seconds
    }
    
    func upGestureTime(type: timeType){
        switch(type){
        case .hour:
            self.hours += 3600
            if self.hours > 24 * 3600 {
                self.hours = 24 * 3600
            }
        case .minute:
            self.minutes += 60
            if self.minutes >= 3600 {
                self.minutes = 3540
            }
        case .second:
            self.seconds += 1
            if self.seconds >= 60 {
                self.seconds = 59
            }
        }
    }
    
    func updateFirebase(userData: UserData, userTimeData: UserTimeData, isTimerFinished: Bool){
        if isTimerFinished {
            userData.todayStudyTime += self.timerTime
            userData.timerStudyTime += self.timerTime
            userTimeData.totalStudyTime += self.timerTime
            userData.lastUpdateDate = Date().getCurrentDateAsString()
            userTimeData.updateTimeData(stopwatch: userData.stopwatchStudyTime, timer: userData.timerStudyTime)
            userData.setUserDataPartly(type: .timerStudyTime, data: userData.timerStudyTime)
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
    
    func startTimer(userData: UserData, userTimeData: UserTimeData){
        guard timer == nil else {return}
        
        timer = Timer.scheduledTimer(
            withTimeInterval: 1,
            repeats: true
        ) { _ in
            if self.timeRemaining > 0 {
                self.timerTime += 1
                self.timeRemaining -= 1
                self.seconds = self.timeRemaining % 60
                self.minutes = (self.timeRemaining - self.seconds) % 60
                self.hours = (self.timeRemaining - self.seconds - (self.minutes * 60))
                self.timerFinished = false
            } else {
                self.stopTimer()
                self.notificationService.sendNotification()
                self.updateFirebase(userData: userData, userTimeData: userTimeData, isTimerFinished: self.timerFinished)
            }
        }
    }
    
    func stopTimer(){
        self.timer?.invalidate()
        self.timer = nil
        self.timerFinished = true
    }
}

//MARK: View
struct TimerView: View {
    @EnvironmentObject var userData: UserData
    @EnvironmentObject var userTimeData: UserTimeData
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @State private var previousTranslation: CGFloat = 0
    @ObservedObject private var viewModel: TimeViewModel = TimeViewModel()
    
    var body: some View {
        VStack{
            if self.viewModel.isStarted == false{
                Text("📚😴📚")
            } else{
                Text("📚🙇📚")
            }
            HStack {
                Text("\(String(format: "%02d", self.viewModel.hours/3600))")
                    .font(.largeTitle)
                    .padding()
                    .gesture(
                        DragGesture()
                            .onChanged { value in
                                let translation = value.translation.height
                                // 드래그 방향에 따라 숫자를 증가 또는 감소
                                if self.previousTranslation - translation > 10{
                                    self.viewModel.downGestureTime(type: .hour)
                                    self.previousTranslation = translation
                                } else if self.previousTranslation - translation < -10{
                                    self.viewModel.upGestureTime(type: .hour)
                                    self.previousTranslation = translation
                                }
                            }
                    )
                Text(":")
                Text("\(String(format: "%02d", self.viewModel.minutes/60))")
                    .font(.largeTitle)
                    .padding()
                    .gesture(
                        DragGesture()
                            .onChanged { value in
                                let translation = value.translation.height
                                print("translation: \(translation), previousTranslation: \(self.previousTranslation)")
                                // 드래그 방향에 따라 숫자를 증가 또는 감소
                                if self.previousTranslation - translation > 10{
                                    self.viewModel.downGestureTime(type: .minute)
                                    self.previousTranslation = translation
                                } else if self.previousTranslation - translation < -10{
                                    self.viewModel.upGestureTime(type: .minute)
                                    self.previousTranslation = translation
                                }
                                
                            }
                    )
                Text(":")
                Text("\(String(format: "%02d", self.viewModel.seconds))")
                    .font(.largeTitle)
                    .padding()
                    .gesture(
                        DragGesture()
                            .onChanged { value in
                                let translation = value.translation.height
                                print("translation: \(translation), previousTranslation: \(self.previousTranslation)")
                                // 드래그 방향에 따라 숫자를 증가 또는 감소
                                if self.previousTranslation - translation > 10{
                                    self.viewModel.downGestureTime(type: .second)
                                    self.previousTranslation = translation
                                } else if self.previousTranslation - translation < -10{
                                    self.viewModel.upGestureTime(type: .second)
                                    self.previousTranslation = translation
                                }
                                
                            }
                    )
            }
            HStack{
                Spacer()
                Text("오늘 목표")
                Spacer()
                Text("\(self.userData.goalStudyTime.timeToText())")
                Spacer()
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
        .navigationBarTitle("타이머",displayMode: .inline)
        .navigationBarBackButtonHidden(true)
        .navigationBarItems(leading: Button("Main"){self.coordinator.pop()})
    }
}

#Preview {
    TimerView()
}

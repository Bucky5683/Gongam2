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
        self.timeRemaining = self.hours + self.minutes + self.seconds
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
                self.minutes = (self.timeRemaining - self.seconds) % 3600
                self.hours = (self.timeRemaining - self.seconds - self.minutes)
                print("\(self.timeRemaining) = \(self.hours) : \(self.minutes) : \(self.seconds)")
                
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
    
    init() {
        //Use this if NavigationBarTitle is with Large Font
        UINavigationBar.appearance().largeTitleTextAttributes = [.foregroundColor: UIColor.whiteFFFFFF]

        //Use this if NavigationBarTitle is with displayMode = .inline
        UINavigationBar.appearance().titleTextAttributes = [.foregroundColor: UIColor.whiteFFFFFF]
    }
    
    var body: some View {
        VStack{
            HStack{
                if self.viewModel.isStarted == true{
                    Text("📚😴📚")
                        .font(Font.system(size: 48).bold())
                        .padding(.bottom, 25)
                        .padding(.top, 75)
                        .padding(.leading, 67)
                } else{
                    Text("📚🙇📚")
                        .font(Font.system(size: 48).bold())
                        .padding(.bottom, 25)
                        .padding(.top, 75)
                        .padding(.leading, 67)
                }
                Spacer()
            }
            HStack {
                VStack{
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
                        .font(Font.system(size: 48).bold())
                        .foregroundColor(.whiteFFFFFF)
                    Text("시간")
                        .font(Font.system(size: 15).bold())
                        .foregroundColor(.whiteFFFFFF)
                }
                Text(":")
                VStack{
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
                        .font(Font.system(size: 48).bold())
                        .foregroundColor(.whiteFFFFFF)
                    Text("분")
                        .font(Font.system(size: 15).bold())
                        .foregroundColor(.whiteFFFFFF)
                }
                Text(":")
                VStack{
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
                        .font(Font.system(size: 48).bold())
                        .foregroundColor(.whiteFFFFFF)
                    Text("초")
                        .font(Font.system(size: 15).bold())
                        .foregroundColor(.whiteFFFFFF)
                }
                Spacer()
                ZStack {
                    if self.viewModel.isStarted {
                        Button {
                            self.viewModel.startTimer(userData: self.userData, userTimeData: self.userTimeData)
                            self.viewModel.isStarted = false
                        } label: {
                            Text("START")
                                .font(Font.system(size: 18).bold())
                                .multilineTextAlignment(.center)
                                .foregroundColor(.white)
                        }.disabled(self.viewModel.isStarted == false)
                            .frame(width: 100, height: 100)
                            .background(.blue5C84FF)
                            .cornerRadius(100)
                        
                    } else {
                        Button {
                            self.viewModel.stopTimer()
                            self.viewModel.updateFirebase(userData: self.userData, userTimeData: self.userTimeData, isTimerFinished: self.viewModel.timerFinished)
                            self.viewModel.isStarted = true
                        } label: {
                            Text("STOP")
                                .font(Font.system(size: 18).bold())
                                .multilineTextAlignment(.center)
                                .foregroundColor(.white)
                        }.disabled(self.viewModel.isStarted == true)
                            .frame(width: 100, height: 100)
                            .background(.redFF0000)
                            .cornerRadius(100)
                    }
                }
                Spacer()
            }
            HStack{
                Text("오늘 목표")
                    .font(Font.system(size: 18).bold())
                    .underline()
                    .foregroundColor(.white)
                Spacer()
                Text("\(self.userData.goalStudyTime.timeToText())")
                    .font(Font.system(size: 18))
                    .multilineTextAlignment(.trailing)
                    .foregroundColor(.white)
            }.padding(.top, 51)
                .padding(.leading, 67)
                .padding(.trailing, 64)
            if self.viewModel.isStarted {
                HStack{
                    Spacer()
                    Text("목표까지")
                        .font(Font.system(size: 12).bold())
                        .multilineTextAlignment(.trailing)
                        .foregroundColor(.lightGrayA5ABBD)
                    let remainTime = viewModel.calculateReMainTime(goalTime: self.userData.goalStudyTime, todayStudyTime: self.userData.todayStudyTime)
                    if remainTime > 0{
                        Text("\(remainTime.timeToText())")
                            .font(Font.system(size: 12).bold())
                            .underline()
                            .multilineTextAlignment(.trailing)
                            .foregroundColor(.lightGrayA5ABBD)
                        Text("남았어요")
                            .font(Font.system(size: 12).bold())
                            .multilineTextAlignment(.trailing)
                            .foregroundColor(.lightGrayA5ABBD)
                    } else {
                        Text("완료했어요")
                            .font(Font.system(size: 12).bold())
                            .multilineTextAlignment(.trailing)
                            .foregroundColor(.lightGrayA5ABBD)
                    }
                }.padding(.trailing, 64)
            }
            Spacer()
            ZStack {
                if self.viewModel.isStarted {
                    Button {
                        self.viewModel.startTimer(userData: self.userData, userTimeData: self.userTimeData)
                        self.viewModel.isStarted = false
                    } label: {
                        Text("START")
                            .font(Font.system(size: 18).bold())
                            .multilineTextAlignment(.center)
                            .foregroundColor(.white)
                    }.disabled(self.viewModel.isStarted == false)
                        .frame(width: 100, height: 100)
                        .background(.blue5C84FF)
                        .cornerRadius(100)
                    
                } else {
                    Button {
                        self.viewModel.stopTimer()
                        self.viewModel.updateFirebase(userData: self.userData, userTimeData: self.userTimeData, isTimerFinished: self.viewModel.timerFinished)
                        self.viewModel.isStarted = true
                    } label: {
                        Text("STOP")
                            .font(Font.system(size: 18).bold())
                            .multilineTextAlignment(.center)
                            .foregroundColor(.white)
                    }.disabled(self.viewModel.isStarted == true)
                        .frame(width: 100, height: 100)
                        .background(.redFF0000)
                        .cornerRadius(100)
                }
            }
            if self.viewModel.isStarted == false {
                Text("STOP을 누르면 시간이 저장돼요!")
                    .font(Font.system(size: 12).bold())
                    .multilineTextAlignment(.trailing)
                    .foregroundColor(.lightGrayA5ABBD)
                    .padding(.top, 31)
            }
            Spacer()
        }
        .background(.darkBlue414756)
        .navigationBarHidden(!self.viewModel.isStarted)
        .navigationBarTitle("타이머",displayMode: .inline)
        .navigationBarBackButtonHidden(true)
        .navigationBarItems(leading:
            Button{
                self.coordinator.pop()
            } label: {
                Text("Main")
                    .foregroundColor(.white)
            }
        )
    }
}

#Preview {
    TimerView()
}

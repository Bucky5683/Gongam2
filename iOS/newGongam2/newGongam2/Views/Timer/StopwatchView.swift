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
    
    func updateFirebase(userDataManager: UserDataManager, isTimerFinished: Bool){
        if isTimerFinished {
            userDataManager.userInfo.todayStudyTime += self.timerTime
            userDataManager.userInfo.stopwatchStudyTime += self.timerTime
            userDataManager.userInfo.todayStudyTime += self.timerTime
            userDataManager.rankRecord.totalStudyTime += self.timerTime
            userDataManager.userInfo.lastUpdateDate = Date().getCurrentDateAsString()
            
            userDataManager.updateTimeData(stopwatch: userDataManager.userInfo.stopwatchStudyTime, timer: userDataManager.userInfo.timerStudyTime)
            
            userDataManager.writeUserInfo()
            userDataManager.writeStudyData()
            userDataManager.writeRankData()
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
    
    func startTimer(userDataManager: UserDataManager){
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
    @EnvironmentObject var userDataManager: UserDataManager
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @ObservedObject var viewModel: StopwatchViewModel = StopwatchViewModel()
    
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
                        .font(Font.system(size: 48).bold())
                        .foregroundColor(.whiteFFFFFF)
                    Text("시간")
                        .font(Font.system(size: 15).bold())
                        .foregroundColor(.whiteFFFFFF)
                }
                Text(":")
                VStack{
                    Text("\(String(format: "%02d", self.viewModel.minutes/60))")
                        .font(Font.system(size: 48).bold())
                        .foregroundColor(.whiteFFFFFF)
                    Text("분")
                        .font(Font.system(size: 15).bold())
                        .foregroundColor(.whiteFFFFFF)
                }
                Text(":")
                VStack{
                    Text("\(String(format: "%02d", self.viewModel.seconds))")
                        .font(Font.system(size: 48).bold())
                        .foregroundColor(.whiteFFFFFF)
                    Text("초")
                        .font(Font.system(size: 15).bold())
                        .foregroundColor(.whiteFFFFFF)
                }
            }
            HStack{
                Text("오늘 목표")
                    .font(Font.system(size: 18).bold())
                    .underline()
                    .foregroundColor(.white)
                Spacer()
                Text("\(self.userDataManager.userInfo.goalStudyTime.timeToText())")
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
                    let remainTime = viewModel.calculateReMainTime(goalTime: self.userDataManager.userInfo.goalStudyTime, todayStudyTime: self.userDataManager.userInfo.todayStudyTime)
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
                        self.viewModel.startTimer(userDataManager: self.userDataManager)
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
                        self.viewModel.updateFirebase(userDataManager: self.userDataManager, isTimerFinished: self.viewModel.timerFinished)
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
        .background(.darkBlue414756, ignoresSafeAreaEdges: .all)
        .navigationBarHidden(!self.viewModel.isStarted)
        .navigationBarTitle("스톱워치",displayMode: .inline)
        .navigationBarBackButtonHidden(true)
        .navigationBarItems(leading: 
                                Button{
                                    self.coordinator.pop()
                                } label: {
                                    Text("Main")
                                        .foregroundColor(.white)
                                })
    }
}

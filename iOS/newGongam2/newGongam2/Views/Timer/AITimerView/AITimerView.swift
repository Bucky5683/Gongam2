//
//  AITimerView.swift
//  newGongam2
//
//  Created by 김서연 on 1/29/24.
//

import SwiftUI
import UIKit
import AVFoundation
import Vision

class AITimerViewModel: ObservableObject, Equatable {
    @Published var hours: Int = 0
    @Published var minutes: Int = 0
    @Published var seconds: Int = 0
    @Published var isStarted: Bool = true
    @Published var timerFinished: Bool = false
    @Published var timerTime: Int = 0
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

struct AITimerView: View {
    @EnvironmentObject var userData: UserData
    @EnvironmentObject var userTimeData: UserTimeData
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @ObservedObject private var viewModel = AITimerViewModel()
    
    var body: some View {
        VStack(spacing: 20){
            MySwiftUIView(viewModel: viewModel)
                .frame(height: 480)
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
                Text("\(self.userData.goalStudyTime.timeToText())")
                    .font(Font.system(size: 18))
                    .multilineTextAlignment(.trailing)
                    .foregroundColor(.white)
            }
            .padding(.leading, 67)
            .padding(.trailing, 64)
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
                        .frame(width: 80, height: 80)
                        .background(.blue5C84FF)
                        .cornerRadius(80)
                    
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
                        .frame(width: 80, height: 80)
                        .background(.redFF0000)
                        .cornerRadius(80)
                }
            }
            Spacer()
        }
        .background(.darkBlue414756)
        .navigationBarTitle("AI 스톱워치",displayMode: .inline)
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

struct MySwiftUIView: UIViewControllerRepresentable {
    @ObservedObject var viewModel: AITimerViewModel
    func makeUIViewController(context: Context) -> VisionObjectRecognitionViewController {
        let storyboard = UIStoryboard(name: "AITimer", bundle: nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "VisionObjectRecognitionViewController") as! VisionObjectRecognitionViewController
        vc.viewModel = viewModel
        return vc
    }
    
    func updateUIViewController(_ uiViewController: VisionObjectRecognitionViewController, context: Context) {
        if uiViewController.viewModel != viewModel {
            uiViewController.viewModel = viewModel
        }
    }
    
    typealias UIViewControllerType = VisionObjectRecognitionViewController
    
}

#Preview {
    AITimerView()
}

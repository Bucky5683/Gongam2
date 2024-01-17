//
//  SetProfileView.swift
//  newGongam2
//
//  Created by 김서연 on 1/8/24.
//

import SwiftUI

class SetProfileViewModel : ObservableObject{
    @Published var hours: Int = 0
    @Published var minutes: Int = 0
    @Published var seconds: Int = 0
    @Published var isStarted: Bool = true
    @Published var timerFinished: Bool = false
    @Published var goalTime: Int = 0
    
    func initGoalTime(goalTime: Int) {
        self.goalTime = goalTime
        self.seconds = goalTime % 60
        self.minutes = (goalTime - self.seconds) % 3600
        self.hours = (goalTime - self.seconds - self.minutes)
    }
    
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
        self.goalTime = self.hours + self.minutes + self.seconds
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
}

struct SetProfileView: View {
    @EnvironmentObject var userData: UserData
    @EnvironmentObject var userTimeData: UserTimeData
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @State var name: String = ""
    @State var email: String = ""
    @State var goalTime: String = ""
    @State var gotoMainView: Bool = false
    @State private var previousTranslation: CGFloat = 0
    @ObservedObject var viewModel: SetProfileViewModel = SetProfileViewModel()
    var isEditProfile: Bool
    
    var body: some View {
        NavigationView {
            VStack{
                Button{
                    print(self.userData.id)
                    print(self.userData.name)
                } label: {
                    AsyncImage(url: URL(string: self.userData.profileImageURL)){ image in
                        image.resizable()
                    } placeholder: {
                        ProgressView()
                    }
                    .frame(width: 75, height: 105)
                }
                Text("닉네임")
                if self.userData.name != ""{
                    TextField(self.userData.name, text: $name)
                        .onSubmit {
                            if name != "" {
                                self.userData.name = name
                            }
                        }
                        .padding()
                        .background(Color(uiColor: .secondarySystemBackground))
                } else{
                    TextField("Enter your name", text: $name)
                        .onSubmit {
                            self.userData.name = name
                        }
                        .padding()
                        .background(Color(uiColor: .secondarySystemBackground))
                }
                Text("이메일")
                if self.userData.email != "" {
                    TextField(self.userData.email, text: $email)
                        .onSubmit {
                            self.userData.email = email
                        }
                        .padding()
                        .background(Color(uiColor: .secondarySystemBackground))
                } else{
                    TextField("Enter your email", text: $email)
                        .onSubmit {
                            self.userData.email = email
                        }
                        .padding()
                        .background(Color(uiColor: .secondarySystemBackground))
                }
                if isEditProfile {
                    Text("목표시간")
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
                    Button {
                        print("회원탈퇴")
                        FirebassDataManager.shared.deleteData(uid: self.userData.id)
                        self.userData.deleteUserData()
                        self.userTimeData.deleteUserTimeData()
                        coordinator.changeRoot(.login)
                    } label: {
                        Text("회원탈퇴")
                    }
                }
                Button{
                    self.userData.goalStudyTime = self.viewModel.goalTime
                    userData.uploadUserData()
                    userTimeData.name = userData.name
                    userTimeData.email = userData.email
                    userTimeData.profileURL = userData.profileImageURL
                    userTimeData.uploadRankData(name: userData.name, profileURL: userData.profileImageURL, email: userData.email)
                    if self.isEditProfile {
                        coordinator.pop()
                    } else {
                        coordinator.changeRoot(.main)
                    }
                    
                } label: {
                    Text("Go")
                }.disabled(self.userData.id == "")
            }
        }
        .background(.whiteFFFFFF, ignoresSafeAreaEdges: .all)
        .navigationBarHidden(true)
        .onAppear(){
            self.viewModel.initGoalTime(goalTime: self.userData.goalStudyTime)
        }
    }
}

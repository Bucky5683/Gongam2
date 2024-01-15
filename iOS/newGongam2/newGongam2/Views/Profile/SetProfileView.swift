//
//  SetProfileView.swift
//  newGongam2
//
//  Created by 김서연 on 1/8/24.
//

import SwiftUI

struct SetProfileView: View {
    @EnvironmentObject var userData: UserData
    @EnvironmentObject var userTimeData: UserTimeData
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @State var name: String = ""
    @State var email: String = ""
    @State var goalTime: String = ""
    @State var gotoMainView: Bool = false
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
                    if self.userData.goalStudyTime != 0 {
                        TextField(self.userData.goalStudyTime.timeToText(), text: $goalTime)
                            .onSubmit {
                                self.userData.goalStudyTime = goalTime.timeToInt()
                            }
                            .padding()
                            .background(Color(uiColor: .secondarySystemBackground))
                    } else{
                        TextField("Enter your goalTime", text: $goalTime)
                            .onSubmit {
                                self.userData.goalStudyTime = goalTime.timeToInt()
                            }
                            .padding()
                            .background(Color(uiColor: .secondarySystemBackground))
                    }
                    Button {
                        print("회원탈퇴")
                    } label: {
                        Text("회원탈퇴")
                    }
                }
                Button{
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
    }
}

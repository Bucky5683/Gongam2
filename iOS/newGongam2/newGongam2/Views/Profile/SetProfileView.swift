//
//  SetProfileView.swift
//  newGongam2
//
//  Created by 김서연 on 1/8/24.
//

import SwiftUI

struct SetProfileView: View {
    @EnvironmentObject var userData: UserData
    @State var name: String = ""
    @State var email: String = ""
    @State var gotoMainView: Bool = false
    var body: some View {
        NavigationView {
            VStack{
                NavigationLink(
                    destination: MainView(),
                    isActive: $gotoMainView,
                    label: {
                        EmptyView()
                    }
                )
                Button{
                    print(userData.id)
                    print(userData.name)
                } label: {
                    AsyncImage(url: URL(string: userData.profileImageURL)){ image in
                        image.resizable()
                    } placeholder: {
                        ProgressView()
                    }
                    .frame(width: 75, height: 105)
                }
                Text("닉네임")
                if userData.name != ""{
                    TextField(userData.name, text: $name)
                        .onSubmit {
                            if name != "" {
                                userData.name = name
                            }
                        }
                        .padding()
                        .background(Color(uiColor: .secondarySystemBackground))
                } else{
                    TextField("Enter your name", text: $name)
                        .onSubmit {
                            userData.name = name
                        }
                        .padding()
                        .background(Color(uiColor: .secondarySystemBackground))
                }
                Text("이메일")
                if userData.email != "" {
                    TextField(userData.email, text: $email)
                        .onSubmit {
                            userData.email = email
                        }
                        .padding()
                        .background(Color(uiColor: .secondarySystemBackground))
                } else{
                    TextField("Enter your email", text: $email)
                        .onSubmit {
                            userData.email = email
                        }
                        .padding()
                        .background(Color(uiColor: .secondarySystemBackground))
                }
                Button{
                    userData.uploadUserData()
                    gotoMainView = true
                    
                } label: {
                    Text("Go")
                }
            }
        }.navigationBarHidden(true)
    }
}

#Preview {
    SetProfileView()
}

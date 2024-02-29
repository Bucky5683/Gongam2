//
//  LoginView.swift
//  newGongam2
//
//  Created by 김서연 on 1/8/24.
//

import SwiftUI
import AuthenticationServices
import CryptoKit
import Firebase
import FirebaseAuth
import FirebaseFirestore
import GoogleSignInSwift
import GoogleSignIn

struct LoginView: View {
    @EnvironmentObject var userDataManager: UserDataManager
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @StateObject var viewModel = LoginViewModel()
    @State private var shouldNavigateToSetProfile = false
    @State private var isNotlogined: Bool = false
    
    var body: some View {
        GeometryReader { geometry in
            HStack{
                Spacer()
                VStack{
                    Spacer()
                    Image("LaunchImage")
                        .frame(width: 118)
                    Spacer()
                    GoogleSignInButton(viewModel: GoogleSignInButtonViewModel(scheme: .dark, style: .standard, state: .normal)){
                        Task{
                            do{
                                try await viewModel.googleLogin(userDataManager: self.userDataManager)
                                userDataManager.readUserInfo()
                                userDataManager.readStudyData()
                                userDataManager.readRankData()
                                await MainActor.run {
                                    self.nextView(self.userDataManager.isNewUser)
                                }
                            } catch {
                                print(error)
                            }
                        }
                    }
                    .frame(width: geometry.size.width * 0.9, height: 50)
                    .alert(isPresented: $isNotlogined){
                        Alert(title: Text("로그인 오류"), message: Text("구글 로그인에 실패했습니다. 다시 로그인해주세요."), dismissButton: .default(Text("확인")))
                    }
                    SignInWithAppleButton { (request) in
                        viewModel.norce = randomNonceString()
                        request.requestedScopes = [.email, .fullName]
                        request.nonce = sha256(viewModel.norce)
                    } onCompletion: { (result) in
                        Task {
                            switch result {
                            case .success(let user):
                                print("success")
                                guard let credential = user.credential as? ASAuthorizationAppleIDCredential else{
                                    print("error with firebase")
                                    return
                                }
                                do {
                                    try await viewModel.appleLogin(credential: credential, userDataManager: self.userDataManager)
                                    userDataManager.readUserInfo()
                                    userDataManager.readStudyData()
                                    userDataManager.readRankData()
                                    await MainActor.run {
                                        self.nextView(self.userDataManager.isNewUser)
                                    }
                                } catch {
                                    print(error)
                                }
                            case .failure(let error):
                                print(error.localizedDescription)
                            }
                        }
                    }.alert(isPresented: $isNotlogined){
                        Alert(title: Text("로그인 오류"), message: Text("애플 로그인에 실패했습니다. 다시 로그인해주세요."), dismissButton: .default(Text("확인")))
                    }
                    .frame(width: geometry.size.width * 0.9, height: 50)
                    Spacer()
                }
                Spacer()
            }
        }
        .background(.whiteFFFFFF)
        .navigationBarHidden(true)
    }
    
    
    func nextView(_ isNewUser: Bool) {
        if isNewUser == true {
            coordinator.isProfileEdit = false
            coordinator.push(.setProfile)
        } else {
            coordinator.changeRoot(.main)
        }
    }
}

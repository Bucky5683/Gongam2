//
//  LoginView.swift
//  newGongam2
//
//  Created by 김서연 on 1/8/24.
//

import SwiftUI
import KakaoSDKUser
import KakaoSDKAuth
import AuthenticationServices
import CryptoKit
import Firebase
import FirebaseAuth
import FirebaseFirestore
import GoogleSignInSwift
import GoogleSignIn
import PopupView

struct LoginView: View {
    @EnvironmentObject var userData: UserData
    @EnvironmentObject var userTimeData: UserTimeData
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
                                try await viewModel.googleLogin()
                                let logined = await viewModel.setUserDataes(userData: userData, userTimeData: userTimeData)
                                self.isNotlogined = !logined
                                if logined {
                                    await MainActor.run {
                                        self.nextView(self.userData.isNewUser)
                                    }
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
                                    try await viewModel.appleLogin(credential: credential)
                                    let logined = await viewModel.setUserDataes(userData: userData, userTimeData: userTimeData)
                                    self.isNotlogined = !logined
                                    if logined {
                                        await MainActor.run {
                                            self.nextView(self.userData.isNewUser)
                                        }
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

// Helper for Apple Login with Firebase
func sha256(_ input: String) -> String {
    let inputData = Data(input.utf8)
    let hashedData = SHA256.hash(data: inputData)
    let hashString = hashedData.compactMap {
        String(format: "%02x", $0)
    }.joined()
    
    return hashString
}

func randomNonceString(length: Int = 32) -> String {
    precondition(length > 0)
    let charset: [Character] =
    Array("0123456789ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvwxyz-._")
    var result = ""
    var remainingLength = length
    
    while remainingLength > 0 {
        let randoms: [UInt8] = (0 ..< 16).map { _ in
            var random: UInt8 = 0
            let errorCode = SecRandomCopyBytes(kSecRandomDefault, 1, &random)
            if errorCode != errSecSuccess {
                fatalError(
                    "Unable to generate nonce. SecRandomCopyBytes failed with OSStatus \(errorCode)"
                )
            }
            return random
        }
        
        randoms.forEach { random in
            if remainingLength == 0 {
                return
            }
            
            if random < charset.count {
                result.append(charset[Int(random)])
                remainingLength -= 1
            }
        }
    }
    
    return result
}

final class SignInGoogleHelper{
    @MainActor
    func signIn() async throws -> GoogleSignInResultModel {
        guard let topVC = await Utilities.shared.topViewController() else {
            throw URLError(.cannotFindHost)
        }
        
        let gidSignInResult = try await GIDSignIn.sharedInstance.signIn(withPresenting: topVC)
        
        guard let idToken = gidSignInResult.user.idToken?.tokenString else {
            throw URLError(.badServerResponse)
        }
        
        let accessToken = gidSignInResult.user.accessToken.tokenString
        let tokens = GoogleSignInResultModel(idToken: idToken, accessToken: accessToken)
        return tokens
    }
}


final class LoginViewModel: ObservableObject {
    @Published var norce = ""
    
    func googleLogin() async throws {
        let helper = SignInGoogleHelper()
        let tokens = try await helper.signIn()
        try await AuthenticationManager.shared.signInWithGoogle(tokens: tokens)
    }
    
    func appleLogin(credential: ASAuthorizationAppleIDCredential) async throws {
        //getting token
        guard let token = credential.identityToken else {
            print("error with firebase")
            return
        }
        
        guard let tokenString = String(data: token, encoding: .utf8) else {
            print("error with token")
            return
        }
        try await AuthenticationManager.shared.signInWithApple(tokens: tokenString, norce: self.norce)
    }
    
    @MainActor
    func setUserDataes(userData: UserData, userTimeData: UserTimeData) async -> Bool{
        Task{
            userData.downloadUserData()
            userTimeData.downloadUserTimeData()
        }
        return true
    }
}

extension AuthenticationManager {
    
    //Sign In SSO
    func signInWithGoogle(tokens: GoogleSignInResultModel) async throws -> AuthDataResultModel {
        let credential = GoogleAuthProvider.credential(withIDToken: tokens.idToken, accessToken: tokens.accessToken)
        return try await signInWithCredential(credential: credential)
    }
    
    func signInWithApple(tokens: String, norce: String) async throws -> AuthDataResultModel {
        let firebaseCredential = OAuthProvider.credential(withProviderID: "apple.com", idToken: tokens, rawNonce: norce)
        return try await signInWithCredential(credential: firebaseCredential)
    }
}


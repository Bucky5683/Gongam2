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

struct LoginView: View {
    @EnvironmentObject var userData: UserData
    @StateObject var viewModel = LoginViewModel()
    @State private var shouldNavigateToSetProfile = false
    
    var body: some View {
        NavigationView{
            VStack{
                NavigationLink(
                    destination: SetProfileView(),
                    isActive: $shouldNavigateToSetProfile,
                    label: {
                        EmptyView()
                    }
                )
                GoogleSignInButton(viewModel: GoogleSignInButtonViewModel(scheme: .dark, style: .wide, state: .normal)){
                    Task{
                        do{
                            try await viewModel.googleLogin()
                            userData.downloadUserData()
                            shouldNavigateToSetProfile = true
                        } catch {
                            print(error)
                        }
                    }
                }
                SignInWithAppleButton { (request) in
                    viewModel.norce = randomNonceString()
                    request.requestedScopes = [.email, .fullName]
                    request.nonce = sha256(viewModel.norce)
                } onCompletion: { (result) in
                    switch result {
                    case .success(let user):
                        print("success")
                        guard let credential = user.credential as? ASAuthorizationAppleIDCredential else{
                            print("error with firebase")
                            return
                        }
                        viewModel.appleLogin(credential: credential)
                        userData.downloadUserData()
                        shouldNavigateToSetProfile = true
                    case .failure(let error):
                        print(error.localizedDescription)
                    }
                }
                .frame(width: UIScreen.main.bounds.width * 0.9, height: 50)
            }
        }.navigationBarHidden(true)
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


#Preview {
    LoginView()
}

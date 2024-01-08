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

struct LoginView: View {
    @EnvironmentObject var userData: UserData
    @State var norce = ""
    var body: some View {
        Button {
            kakaoLogin()
        } label: {
            Image("kakaoLogjnButton")
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: UIScreen.main.bounds.width * 0.9)
        }
        SignInWithAppleButton { (request) in
            self.norce = randomNonceString()
            request.requestedScopes = [.email, .fullName]
            request.nonce = sha256(norce)
        } onCompletion: { (result) in
            switch result {
            case .success(let user):
                print("success")
                guard let credential = user.credential as? ASAuthorizationAppleIDCredential else{
                    print("error with firebase")
                    return
                }
                appleLogin(credential: credential)
            case .failure(let error):
                print(error.localizedDescription)
            }
        }
        .frame(width: UIScreen.main.bounds.width * 0.9, height: 50)
    }
    
    func kakaoLogin(){
        if UserApi.isKakaoTalkLoginAvailable() {
            UserApi.shared.loginWithKakaoTalk {(oauthToken, error) in
                print("👇 oauthToken?.accessToken 👇")
                print(oauthToken?.accessToken ?? "")
                print("👇 oauthToken?.refreshToken 👇")
                print(oauthToken?.refreshToken ?? "")
                print("👇 error 👇")
                print(error ?? "")
            }
        } else {
            UserApi.shared.loginWithKakaoAccount{(oauthToken, error) in
                print("👇 oauthToken?.accessToken 👇")
                print(oauthToken?.accessToken ?? "")
                print("👇 oauthToken?.refreshToken 👇")
                print(oauthToken?.refreshToken ?? "")
                print("👇 error 👇")
                print(error ?? "")
            }
        }
        
        UserApi.shared.me() { (user,error) in
            if let error = error{
                print("saveUserData error : ", error)
            } else {
                let exampleImage = URL(string: "https://firebasestorage.googleapis.com/v0/b/gongam2-ff081.appspot.com/o/example2.jpeg?alt=media&token=2b4bbe1f-9ba2-49a7-bf54-87b5ca70eddd")!
                let kakaoName = user?.kakaoAccount?.profile?.nickname ?? ""
                let kakaoProfile = user?.kakaoAccount?.profile?.profileImageUrl?.absoluteString
//
//                do {
//                    try self.userData.profileImageURL = String(contentsOf: kakaoProfile!)
//                } catch{
//                    print("Kakao Proifle Error")
//                }
                self.userData.name = kakaoName
                self.userData.profileImageURL = kakaoProfile ?? ""
                print(self.userData.name)
                print(self.userData.profileImageURL)
            }
        }
    }
    
    func appleLogin(credential: ASAuthorizationAppleIDCredential) {
        //getting token
        guard let token = credential.identityToken else {
            print("error with firebase")
            return
        }
        
        guard let tokenString = String(data: token, encoding: .utf8) else {
            print("error with token")
            return
        }
        
        let firebaseCredential = OAuthProvider.credential(withProviderID: "apple.com", idToken: tokenString, rawNonce: norce)
        Auth.auth().signIn(with: firebaseCredential) { result, err in
            if let err = err {
                print(err.localizedDescription)
            }
            
            print("로그인 완료")
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


#Preview {
    LoginView()
}

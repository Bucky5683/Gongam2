//
//  LoginViewModel.swift
//  newGongam2
//
//  Created by 김서연 on 1/9/24.
//

import Foundation
import KakaoSDKUser
import KakaoSDKAuth
import AuthenticationServices
import CryptoKit
import Firebase
import FirebaseAuth
import FirebaseFirestore
import GoogleSignInSwift
import GoogleSignIn

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
    //    func kakaoAuthSignIn() {
    //        if AuthApi.hasToken() { // 발급된 토큰이 있는지
    //            UserApi.shared.accessTokenInfo { _, error in // 해당 토큰이 유효한지
    //                if error != nil { // 에러가 발생했으면 토큰이 유효하지 않다.
    //                    self.openKakaoService()
    //                } else { // 유효한 토큰
    //                    self.loadingInfoDidKakaoAuth()
    //                }
    //            }
    //        } else { // 만료된 토큰
    //            self.openKakaoService()
    //        }
    //    }
    //
    //    func openKakaoService() {
    //        if UserApi.isKakaoTalkLoginAvailable() { // 카카오톡 앱 이용 가능한지
    //            UserApi.shared.loginWithKakaoTalk { oauthToken, error in // 카카오톡 앱으로 로그인
    //                if let error = error { // 로그인 실패 -> 종료
    //                    print("Kakao Sign In Error: ", error.localizedDescription)
    //                    return
    //                }
    //
    //                _ = oauthToken // 로그인 성공
    //                self.loadingInfoDidKakaoAuth() // 사용자 정보 불러와서 Firebase Auth 로그인하기
    //            }
    //        } else { // 카카오톡 앱 이용 불가능한 사람
    //            UserApi.shared.loginWithKakaoAccount { oauthToken, error in // 카카오 웹으로 로그인
    //                if let error = error { // 로그인 실패 -> 종료
    //                    print("Kakao Sign In Error: ", error.localizedDescription)
    //                    return
    //                }
    //                _ = oauthToken // 로그인 성공
    //                self.loadingInfoDidKakaoAuth() // 사용자 정보 불러와서 Firebase Auth 로그인하기
    //            }
    //        }
    //    }
    //
    //    func loadingInfoDidKakaoAuth() {  // 사용자 정보 불러오기
    //        UserApi.shared.me { kakaoUser, error in
    //            if error != nil {
    //                print("카카오톡 사용자 정보 불러오는데 실패했습니다.")
    //
    //                return
    //            }
    //            guard let email = kakaoUser?.kakaoAccount?.email else { return }
    //            guard let password = kakaoUser?.id else { return }
    //            guard let userName = kakaoUser?.kakaoAccount?.profile?.nickname else { return }
    //
    //            Auth.auth().createUser(withEmail: email, password: "\(password)"){ result, error in
    //                if let error = error {
    //                    print("error: \(error.localizedDescription)")
    //                }
    //                if result != nil{
    //                    let changeRequest = Auth.auth().currentUser?.createProfileChangeRequest()
    //                    changeRequest?.displayName = userName
    //                    print("사용자 이메일: \(String(describing: result?.user.email))")
    //                }
    //            }
    //
    //            Auth.auth().signIn(withEmail: email, password: "\(password)") { result, error in
    //                if let error = error {
    //                    print("error: \(error.localizedDescription)")
    //
    //                    return
    //                }
    //
    //                if result != nil {
    //                    self.shouldNavigateToSetProfile = true
    //                    print("사용자 이메일: \(String(describing: result?.user.email))")
    //                    print("사용자 이름: \(String(describing: result?.user.displayName))")
    //
    //                }
    //            }
    //        }
    //    }
}

extension AuthenticationManager {
    
    //Sign In SSO
    func signInWithGoogle(tokens: GoogleSignInResultModel) async throws -> AuthDataResultModel {
        let credential = GoogleAuthProvider.credential(withIDToken: tokens.idToken, accessToken: tokens.accessToken)
        return try await signInWithCredential(credential: credential)
    }
}

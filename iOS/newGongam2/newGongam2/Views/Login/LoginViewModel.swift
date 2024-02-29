//
//  LoginViewModel.swift
//  newGongam2
//
//  Created by 김서연 on 2/27/24.
//

import SwiftUI
import AuthenticationServices
import CryptoKit
import Firebase
import FirebaseAuth
import FirebaseFirestore
import GoogleSignInSwift
import GoogleSignIn

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
    
    func googleLogin(userDataManager: UserDataManager) async throws {
        let helper = SignInGoogleHelper()
        let tokens = try await helper.signIn()
        let userInfo = try await AuthenticationManager.shared.signInWithGoogle(tokens: tokens)
        
        DispatchQueue.main.async{
            userDataManager.id = userInfo.uid
            userDataManager.userInfo.email = userInfo.email ?? ""
            userDataManager.userInfo.profileImageURL = userInfo.photoUrl ?? "https://firebasestorage.googleapis.com/v0/b/gongam2-ff081.appspot.com/o/example2.jpeg?alt=media&token=2b4bbe1f-9ba2-49a7-bf54-87b5ca70eddd"
        }
    }
    
    func appleLogin(credential: ASAuthorizationAppleIDCredential, userDataManager: UserDataManager) async throws {
        //getting token
        guard let token = credential.identityToken else {
            print("error with firebase")
            return
        }
        
        guard let tokenString = String(data: token, encoding: .utf8) else {
            print("error with token")
            return
        }
        let userInfo = try await AuthenticationManager.shared.signInWithApple(tokens: tokenString, norce: self.norce)
        
        DispatchQueue.main.async{
            userDataManager.id = userInfo.uid
            userDataManager.userInfo.email = userInfo.email ?? ""
            userDataManager.userInfo.profileImageURL = userInfo.photoUrl ?? "https://firebasestorage.googleapis.com/v0/b/gongam2-ff081.appspot.com/o/example2.jpeg?alt=media&token=2b4bbe1f-9ba2-49a7-bf54-87b5ca70eddd"
        }
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

//
//  newGongam2App.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import SwiftUI
import FirebaseCore
import KakaoSDKCommon
import KakaoSDKAuth
import GoogleSignIn

class AppDelegate: NSObject, UIApplicationDelegate {
    var notifDelegate = NotificationDelegate()
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        UNUserNotificationCenter.current().delegate = notifDelegate
        return true
    }
    
    func application(
      _ app: UIApplication,
      open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]
    ) -> Bool {
      var handled: Bool

      handled = GIDSignIn.sharedInstance.handle(url)
      if handled {
        return true
      }

      // Handle other custom URL types.

      // If not handled by this app, return false.
      return false
    }
}

@main
struct newGongam2App: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    @StateObject var userData = UserData()
    @StateObject var userTimeData = UserTimeData()
    @State private var coordinator = NavigationCoordinator()
    
    init() {
        // Kakao SDK 초기화
        let KAKAO_APP_KEY: String = Bundle.main.infoDictionary?["KAKAO_APP_KEY"] as? String ?? "KAKAO_APP_KEY is nil"
        KakaoSDK.initSDK(appKey: KAKAO_APP_KEY, loggingEnable: true)
    }
    
    var body: some Scene {
        WindowGroup {
            NavigationStack(path: $coordinator.paths.animation(.linear(duration: 0))){
                coordinator.navigate(to: .login)
                    .navigationDestination(for: Screens.self){ screen in
                        coordinator.navigate(to: screen)
                    }
                    .onOpenURL { url in
                        GIDSignIn.sharedInstance.handle(url)
                        //                if (AuthApi.isKakaoTalkLoginUrl(url)) {
                        //                    // 뷰가 속한 Window에 대한 URL을 받았을 때 호출할 Handler를 등록하는 함수
                        //                    // 카카오 로그인을 위해 웹 혹은 카카오톡 앱으로 이동 후 다시 앱으로 돌아오는 과정을 거쳐야하므로, Handler를 추가로 등록해줌
                        //                    _ = AuthController.handleOpenUrl(url: url)
                        //                }
                    }
//                    .onAppear {
//                        GIDSignIn.sharedInstance.restorePreviousSignIn { user, error in
//                            // Check if `user` exists; otherwise, do something with `error`
//                        }
//                    }
            }.environmentObject(userData)
                .environmentObject(userTimeData)
                .environment(coordinator)
        }
    }
}

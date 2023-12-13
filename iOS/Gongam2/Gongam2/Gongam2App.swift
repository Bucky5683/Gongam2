//
//  Gongam2App.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/12.
//

import SwiftUI
import FirebaseCore


class MyAppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    func application( _ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil ) -> Bool {
    // ...
        FirebaseApp.configure()
        UNUserNotificationCenter.current().delegate = self

        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(
          options: authOptions,
          completionHandler: { _, _ in }
        )

        application.registerForRemoteNotifications()
        return true
    }
    func application( _ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions ) -> UISceneConfiguration {
        let sceneConfig = UISceneConfiguration(name: nil, sessionRole: connectingSceneSession.role)
        sceneConfig.delegateClass = MySceneDelegate.self
        return sceneConfig
    }
}

class MySceneDelegate: NSObject, UIWindowSceneDelegate {
    var window: UIWindow?

    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        guard let _ = (scene as? UIWindowScene) else { return }
    }

    func sceneDidDisconnect(_ scene: UIScene) {
      
    }

    func sceneDidBecomeActive(_ scene: UIScene) {
      
    }

    func sceneWillResignActive(_ scene: UIScene) {
      
    }

    func sceneWillEnterForeground(_ scene: UIScene) {
      
    }

    func sceneDidEnterBackground(_ scene: UIScene) {
      
    }
}

@available(iOS 14.0, *)
@main
struct Gongam2App: App {
  // register app delegate for Firebase setup
    /*
     UIApplicationDelegateAdaptor 는 애플에서 SwiftUI에서도 AppDelegate 를 사용할 수 있도록 제공한 프로퍼티 래퍼로, 위 정의를 보면 알겠지만 NSObject와 UIApplicationDelegate를 충족해야 하는 것으로 되어있다. 우리의 MyAppDelegate 와 같은 상황이다.
    */
    @UIApplicationDelegateAdaptor(MyAppDelegate.self) var delegate


    var body: some Scene {
        WindowGroup {
            NavigationView {
                MainContentView()
            }
        }
    }
}

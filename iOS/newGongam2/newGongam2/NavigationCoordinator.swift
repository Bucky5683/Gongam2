//
//  NavigationCoordinator.swift
//  newGongam2
//
//  Created by 김서연 on 1/11/24.
//

import SwiftUI

// Screens that we need to navigate
enum Screens {
    case login
    case setProfile
    case main
    case timer
    case stopwatch
    case rank
    case myReport
}

@Observable
class NavigationCoordinator {
    var paths = NavigationPath()
    var isProfileEdit: Bool = false
    
    @ViewBuilder
         func navigate(to screen: Screens) -> some View {
            switch screen {
            case .login:
                LoginView()
            case .setProfile:
                SetProfileView(isEditProfile: isProfileEdit)
            case .main:
                MainView()
            case .timer:
                TimerView()
            case .stopwatch:
                StopwatchView()
            case .rank:
                RankView()
            case .myReport:
                ReportView()
            }
        }
    
    func push(_ screen: Screens) {
        print("coordinator: View append!")
        paths.append(screen)
        print("coordinator: View path = \(paths)")
    }
    
    func pop() {
        print("coordinator: View pop!")
        paths.removeLast()
        print("coordinator: View path = \(paths)")
    }
    
    func popToRoot(){
        print("coordinator: View pop to root!")
        paths.removeLast(paths.count)
        print("coordinator: View path = \(paths)")
    }
    
    func changeRoot(_ screen: Screens){
        print("coordinator: Changed root View!")
        self.popToRoot()
        paths.append(screen)
        print("coordinator: View path = \(paths)")
    }
}

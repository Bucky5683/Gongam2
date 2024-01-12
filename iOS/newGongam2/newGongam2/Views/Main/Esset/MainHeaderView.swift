//
//  MainHeaderView.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import SwiftUI

struct MainHeaderView: View {
    @EnvironmentObject var userData: UserData
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @Binding var viewModel: MainViewModel
    
    var body: some View {
        VStack(content: {
            Text("오늘 공부한 시간")
            Text(userData.todayStudyTime.timeToText())
            HStack(){
                if viewModel.calculateReMainTime(goalTime: userData.goalStudyTime, todayStudyTime: userData.todayStudyTime) > 0{
                    let remaintime = viewModel.calculateReMainTime(goalTime: userData.goalStudyTime, todayStudyTime: userData.todayStudyTime)
                    Text("🔥")
                    Text("목표까지")
                    Text(remaintime.timeToText())
                    Text("🔥")
                } else {
                    Text("🔥")
                    Text("목표를 완료했어요!")
                    Text("🔥")
                }
            }.background(Color.lightGrayA5ABBD)
        }).background(.darkBlue414756, ignoresSafeAreaEdges: .all)
    }
}

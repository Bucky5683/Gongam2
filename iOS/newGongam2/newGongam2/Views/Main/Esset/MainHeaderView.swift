//
//  MainHeaderView.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import SwiftUI

struct MainHeaderView: View {
    @EnvironmentObject var userData: UserData
    @State var purposeTimeRemains = true
    
    var body: some View {
        VStack(content: {
            Text("오늘 공부한 시간")
            Text(userData.timeToText(studyTime: userData.todayStudyTime))
            HStack(){
                if purposeTimeRemains{
                    Text("🔥")
                    Text("목표까지")
                    Text(userData.remainToGoalText(goal: userData.goalStudyTime, time: userData.todayStudyTime))
                    Text("🔥")
                } else {
                    Text("🔥")
                    Text("목표를 완료했어요!")
                    Text("🔥")
                }
            }.background(Color.lightGrayA5ABBD)
        })
    }
}

#Preview {
    MainHeaderView()
}

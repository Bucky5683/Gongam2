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
        GeometryReader { geometry in
            HStack{
                Spacer()
                VStack(spacing: 15,content: {
                    Spacer()
                    Text("오늘 공부한 시간")
                        .font(Font.system(size: 12).weight(.regular))
                        .underline(true, color: .whiteFFFFFF)
                        .foregroundColor(.whiteFFFFFF)
                    Text(userData.todayStudyTime.timeToText())
                        .font(Font.system(size: 30))
                        .bold()
                        .foregroundColor(.whiteFFFFFF)
                    HStack(){
                        if viewModel.calculateReMainTime(goalTime: userData.goalStudyTime, todayStudyTime: userData.todayStudyTime) > 0{
                            let remaintime = viewModel.calculateReMainTime(goalTime: userData.goalStudyTime, todayStudyTime: userData.todayStudyTime)
                            Text("🔥").font(Font.system(size: 12))
                                .padding(.leading, 30)
                                .padding(.top, 5)
                                .padding(.bottom, 5)
                            Text("목표까지")
                                .font(Font.system(size: 12).weight(.regular))
                                .foregroundColor(.darkBlue414756)
                            Text(remaintime.timeToText())
                                .font(Font.system(size: 12))
                                .bold()
                                .foregroundColor(.darkBlue414756)
                            Text("🔥").font(Font.system(size: 12))
                                .padding(.trailing, 30)
                        } else {
                            Text("🔥").font(Font.system(size: 12))
                                .padding(.leading, 30)
                                .padding(.top, 5)
                                .padding(.bottom, 5)
                            Text("목표를 완료했어요!")
                                .font(Font.system(size: 12).weight(.regular))
                                .foregroundColor(.darkBlue414756)
                            Text("🔥").font(Font.system(size: 12))
                                .padding(.trailing, 30)
                        }
                    }.background(Color.lightGrayA5ABBD)
                        .cornerRadius(20)
                        .padding(.bottom, 52)
                    
                })
                Spacer()
            }
        }.background(.darkBlue414756, ignoresSafeAreaEdges: .all)
            .edgesIgnoringSafeArea(.top)
    }
}

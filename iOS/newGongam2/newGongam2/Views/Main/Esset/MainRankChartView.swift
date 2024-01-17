//
//  MainRankChartView.swift
//  newGongam2
//
//  Created by 김서연 on 1/10/24.
//

import SwiftUI

struct MainRankChartView: View {
    @EnvironmentObject var userTimeData: UserTimeData
    @EnvironmentObject var userData: UserData
    var body: some View {
        ZStack{
            HStack{
                Rectangle().frame(width: 54, height: 215).padding(.leading, 78)
                    .shadow(radius: 10)
                Spacer()
            }
            VStack{
                if (self.userTimeData.totalStudyTime - self.userTimeData.averageTime)>0 {
                    HStack{
                        Rectangle().frame(width: 67, height: 11).padding(.leading, 72)
                            .foregroundColor(.pinkECB9C2)
                            .shadow(radius: 10)
                        HStack{
                            Text("\(self.userTimeData.name)님의 등수")
                            if self.userTimeData.myRank > 999 {
                                Text("999+")
                            } else {
                                Text("\(self.userTimeData.myRank)")
                            }
                        }.background(.pinkECB9C2)
                            .shadow(radius: 10)
                        Spacer()
                    }
                    HStack{
                        Rectangle().frame(width: 67, height: 11).padding(.leading, 72)
                            .foregroundColor(.lightGrayA5ABBD)
                            .shadow(radius: 10)
                        HStack{
                            Text("평균 공부시간")
                            Text(":")
                            Text("\(self.userTimeData.averageTime.timeToTextForWeekly())")
                        }.background(.lightGrayA5ABBD)
                            .shadow(radius: 10)
                        Spacer()
                    }
                } else {
                    HStack{
                        Rectangle().frame(width: 67, height: 11).padding(.leading, 72)
                            .foregroundColor(.lightGrayA5ABBD)
                            .shadow(radius: 10)
                        HStack{
                            Text("평균 공부시간")
                            Text(":")
                            Text("\(self.userTimeData.averageTime.timeToTextForWeekly())")
                        }.background(.lightGrayA5ABBD)
                            .shadow(radius: 10)
                        Spacer()
                    }
                    HStack{
                        Rectangle().frame(width: 67, height: 11).padding(.leading, 72)
                            .foregroundColor(.lightBlueCBD9FE)
                            .shadow(radius: 10)
                        HStack{
                            Text("\(self.userTimeData.name)님의 등수")
                            if self.userTimeData.myRank > 999 {
                                Text("999+")
                            } else {
                                Text("\(self.userTimeData.myRank)")
                            }
                        }.background(.lightBlueCBD9FE)
                            .shadow(radius: 10)
                        Spacer()
                    }
                }
            }
        }.background(.whiteFFFFFF, ignoresSafeAreaEdges: .all)
    }
}

#Preview {
    MainRankChartView()
}

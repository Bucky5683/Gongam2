//
//  MainRankChartView.swift
//  newGongam2
//
//  Created by 김서연 on 1/10/24.
//

import SwiftUI

struct MainRankChartView: View {
    @EnvironmentObject var userDataManager: UserDataManager
    var body: some View {
        ZStack{
            HStack{
                Rectangle().frame(width: 54, height: 215)
                    .foregroundStyle(
                        LinearGradient(
                            stops: [
                                Gradient.Stop(color: Color(red: 0.25, green: 0.28, blue: 0.34).opacity(0.7), location: 0.00),
                                Gradient.Stop(color: Color(red: 0.25, green: 0.28, blue: 0.34), location: 1.00),
                            ],
                            startPoint: UnitPoint(x: 0.5, y: 0),
                            endPoint: UnitPoint(x: 0.5, y: 1)
                        )
                    )
                    .cornerRadius(10)
                    .padding(.leading, 78)
                Spacer()
            }
            VStack(spacing: 10){
                if (self.userDataManager.rankRecord.totalStudyTime - self.userDataManager.rankRecord.averageTime)>0 {
                    HStack{
                        Rectangle().frame(width: 67, height: 11)
                            .foregroundColor(.pinkECB9C2)
                            .cornerRadius(10)
                            .padding(.leading, 72)
                        HStack{
                            Text("\(self.userDataManager.userInfo.name)님의 등수")
                                .font(Font.system(size: 12))
                                .foregroundColor(.darkBlue414756)
                                .padding(.leading, 15)
                            if self.userDataManager.rankRecord.myRank > 999 {
                                Text("999+")
                                    .font(
                                        Font.system(size: 20)
                                            .weight(.bold)
                                    )
                                    .foregroundColor(.darkBlue414756)
                                    .padding(.trailing, 15)
                                    .padding(.top, 11)
                                    .padding(.bottom, 11)
                            } else {
                                Text("\(self.userDataManager.rankRecord.myRank)")
                                    .font(
                                        Font.system(size: 20)
                                            .weight(.bold)
                                    )
                                    .foregroundColor(.darkBlue414756)
                                    .padding(.trailing, 15)
                                    .padding(.top, 11)
                                    .padding(.bottom, 11)
                            }
                        }.background(.pinkECB9C2)
                            .cornerRadius(10)
                        Spacer()
                    }
                    .shadow(color: .black.opacity(0.25), radius: 2, x: 0, y: 4)
                    HStack(spacing: 13){
                        Rectangle().frame(width: 67, height: 11)
                            .foregroundColor(.lightGrayA5ABBD)
                            .cornerRadius(10)
                            .padding(.leading, 72)
                        HStack{
                            Text("평균 공부시간 : \(self.userDataManager.rankRecord.averageTime.timeToTextForWeekly())")
                                .font(
                                    Font.system(size: 10)
                                        .weight(.medium)
                                )
                                .foregroundColor(.white)
                                .padding(.leading, 15)
                                .padding(.trailing, 15)
                                .padding(.top, 3)
                                .padding(.bottom, 3)
                        }.background(.lightGrayA5ABBD)
                            .cornerRadius(10)
                        Spacer()
                    }
                    .shadow(color: .black.opacity(0.25), radius: 2, x: 0, y: 4)
                } else {
                    HStack(spacing: 13){
                        Rectangle().frame(width: 67, height: 11)
                            .foregroundColor(.lightGrayA5ABBD)
                            .cornerRadius(10)
                            .padding(.leading, 72)
                        HStack{
                            Text("평균 공부시간 : \(self.userDataManager.rankRecord.averageTime.timeToTextForWeekly())")
                                .font(
                                    Font.system(size: 10)
                                        .weight(.medium)
                                )
                                .foregroundColor(.white)
                                .padding(.leading, 15)
                                .padding(.trailing, 15)
                                .padding(.top, 3)
                                .padding(.bottom, 3)
                        }.background(.lightGrayA5ABBD)
                            .cornerRadius(10)

                        Spacer()
                    }
                    .shadow(color: .black.opacity(0.25), radius: 2, x: 0, y: 4)
                    HStack(spacing: 13){
                        Rectangle().frame(width: 67, height: 11)
                            .foregroundColor(.lightBlueCBD9FE)
                            .cornerRadius(10)
                            .padding(.leading, 72)
                        HStack{
                            Text("\(self.userDataManager.userInfo.name)님의 등수")
                                .font(Font.system(size: 12))
                                .foregroundColor(.darkBlue414756)
                                .padding(.leading, 15)
                            if self.userDataManager.rankRecord.myRank > 999 {
                                Text("999+")
                                    .font(
                                        Font.system(size: 20)
                                            .weight(.bold)
                                    )
                                    .foregroundColor(.darkBlue414756)
                                    .padding(.trailing, 15)
                                    .padding(.top, 11)
                                    .padding(.bottom, 11)
                            } else {
                                Text("\(String(format: "%03d", self.userDataManager.rankRecord.myRank))")
                                    .font(
                                        Font.system(size: 20)
                                            .weight(.bold)
                                    )
                                    .foregroundColor(.darkBlue414756)
                                    .padding(.trailing, 15)
                                    .padding(.top, 11)
                                    .padding(.bottom, 11)
                            }
                        }.background(.lightBlueCBD9FE)
                            .cornerRadius(10)

                        Spacer()
                    }
                    .shadow(color: .black.opacity(0.25), radius: 2, x: 0, y: 4)
                }
            }
        }.background(.whiteFFFFFF)
    }
}

#Preview {
    MainRankChartView()
}

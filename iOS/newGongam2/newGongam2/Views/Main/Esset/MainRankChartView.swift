//
//  MainRankChartView.swift
//  newGongam2
//
//  Created by 김서연 on 1/10/24.
//

import SwiftUI

struct MainRankChartView: View {
    var body: some View {
        ZStack{
            HStack{
                Rectangle().frame(width: 54, height: 215).padding(.leading, 78)
                    .shadow(radius: 10)
                Spacer()
            }
            VStack{
                HStack{
                    Rectangle().frame(width: 67, height: 11).padding(.leading, 72)
                        .foregroundColor(.lightBlueCBD9FE)
                        .shadow(radius: 10)
                    HStack{
                        Text("OOO님의 등수")
                        Text("999+")
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
                        Text("9999:99:99")
                    }.background(.lightGrayA5ABBD)
                        .shadow(radius: 10)
                    Spacer()
                }
                HStack{
                    Rectangle().frame(width: 67, height: 11).padding(.leading, 72)
                        .foregroundColor(.lightBlueCBD9FE)
                        .shadow(radius: 10)
                    HStack{
                        Text("OOO님의 등수")
                        Text("999+")
                    }.background(.lightBlueCBD9FE)
                        .shadow(radius: 10)
                    Spacer()
                }
            }
        }.background(.whiteFFFFFF, ignoresSafeAreaEdges: .all)
    }
}

#Preview {
    MainRankChartView()
}

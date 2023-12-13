//
//  ContentView.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/12.
//

import SwiftUI
import SafeAreaBrush

struct MainContentView: View {
    var body: some View {
        GeometryReader{ geometry in
            ScrollView{
                VStack(spacing: 0){
                    Rectangle().frame(width: geometry.size.width, height: 40).foregroundStyle(.darkBlue414756)
                    VStack(spacing: 20.0){
                        MainHeaderView().frame(width: geometry.size.width, height: 280)
                        MainTimerView()
                        MainRankView(nickname: "Rb", rank: "999+", higherThenAverage: false, averageTime: "100:32:57")
                        MyReportView(weeklyReport: [(99, "h"),(0, "m"),(-99, "h"),(-99, "h"),(-99, "h"),(-99, "h"),(-99, "h")])
                        Spacer()
                    }.background(.whiteFFFFFF, ignoresSafeAreaEdges: .top)
                }
            }.background(.whiteFFFFFF).ignoresSafeArea()
        }
    }
}

#Preview {
    MainContentView()
}

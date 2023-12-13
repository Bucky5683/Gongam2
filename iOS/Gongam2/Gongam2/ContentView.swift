//
//  ContentView.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/12.
//

import SwiftUI

struct ContentView: View {
    var body: some View {
        GeometryReader{ geometry in
            ScrollView{
                VStack(spacing: 20.0){
                    MainHeaderView().frame(height: 280)
                    MainTimerView()
                    MainRankView(nickname: "Rb", rank: "999+", higherThenAverage: false, averageTime: "100:32:57")
                    Spacer()
                }.background(.whiteFFFFFF)
            }
        }
        
    }
}

#Preview {
    ContentView()
}

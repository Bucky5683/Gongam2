//
//  MainRankView.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/13.
//

import SwiftUI

struct MainRankView: View {
    var nickname: String
    var rank: String
    var higherThenAverage: Bool
    var averageTime: String
    var body: some View {
        VStack(alignment: .center, spacing: 12){
            MainFuncTitleView(moreButton: true, title: "랭킹")
            MainRankBar()
                .overlay(content: {
                    HStack{
                        Spacer().frame(width: 71)
                        if higherThenAverage{
                            VStack(alignment: .leading, spacing: 10){
                                Spacer()
                                MainMyRank(nickName: nickname, rank: rank, higherThanAverage: higherThenAverage)
                                MainAverageRank(averageTime: averageTime)
                                Spacer()
                            }
                        }else{
                            VStack(alignment: .leading, spacing: 10){
                                Spacer()
                                MainAverageRank(averageTime: averageTime)
                                MainMyRank(nickName: nickname, rank: rank, higherThanAverage: higherThenAverage)
                                Spacer()
                            }
                        }
                        Spacer()
                    }
                })
            MainFuncTexts(time: "999:999:999", textType: .rank, minOrMore: higherThenAverage)
        }.frame(height: 350)
    }
}

#Preview {
    MainRankView(nickname: "Rb", rank: "999+", higherThenAverage: false, averageTime: "100:32:57")
}

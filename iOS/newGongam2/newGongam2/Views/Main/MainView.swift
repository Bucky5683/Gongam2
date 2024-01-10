//
//  MainView.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import SwiftUI

struct MainView: View {
    @EnvironmentObject var userData: UserData
    
    var body: some View {
        NavigationView{
            VStack{
                Text("MainView!")
                MainHeaderView()
                //MARK: 타이머
                VStack {
                    Text("타이머")
                    TimersButtonView(isTimer: true)
                    TimersButtonView(isTimer: false)
                }
                
                //MARK: 랭크
                VStack{
                    MainSubHeaderView(isRank: true)
                    NavigationLink(destination: RankView(), label: {
                        MainRankChartView()
                    })
                    Text("평균보다 만큼 덜 공부했어요")
                }
                
                //MARK: 마이리포트
                VStack{
                    MainSubHeaderView(isRank: false)
                    NavigationLink(destination: ReportView(), label: {
                        MainMyReportGridView()
                    })
                    Text("이번 주에 평균 만큼 공부했어요!")
                }
            }
        }.navigationBarHidden(true)
    }
}


struct TimersButtonView: View {
    var isTimer: Bool
    var body: some View {
        HStack{
            if isTimer{
                Text("⏰")
                Text("타이머")
                NavigationLink(destination: TimerView(), label: {
                    HStack{
                        Text("GO")
                        Image("goButtonIcon")
                    }
                })
            } else {
                Text("⏱️")
                Text("스톱워치")
                NavigationLink(destination: TimerView(), label: {
                    HStack{
                        Text("GO")
                        Image("goButtonIcon")
                    }
                })
            }
        }
    }
}

struct MainSubHeaderView: View {
    var isRank: Bool = false
    var body: some View {
        HStack{
            Spacer()
            if isRank {
                Text("랭크")
            } else {
                Text("마이 리포트")
            }
            Spacer()
            if isRank {
                NavigationLink(destination: RankView(), label: {
                    Text("더보기 >")
                })
            } else {
                NavigationLink(destination: ReportView(), label: {
                    Text("더보기 >")
                })
            }
            Spacer()
        }
    }
}

#Preview {
    MainView()
}

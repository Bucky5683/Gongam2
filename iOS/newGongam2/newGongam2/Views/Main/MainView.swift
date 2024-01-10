//
//  MainView.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import SwiftUI

struct MainView: View {
    @EnvironmentObject var userData: UserData
    @State var timerButtonClicked: Bool = false
    @State var stopwatchButtonClicked: Bool = false
    @State var rankMoreButtonClicked: Bool = false
    @State var myReportMoreButtonClicked: Bool = false
    
    var body: some View {
        NavigationView{
            NavigationLink(
                destination: TimerView(),
                isActive: $timerButtonClicked,
                label: {
                    EmptyView()
                }
            )
            NavigationLink(
                destination: TimerView(),
                isActive: $stopwatchButtonClicked,
                label: {
                    EmptyView()
                }
            )
            NavigationLink(
                destination: TimerView(),
                isActive: $rankMoreButtonClicked,
                label: {
                    EmptyView()
                }
            )
            NavigationLink(
                destination: TimerView(),
                isActive: $myReportMoreButtonClicked,
                label: {
                    EmptyView()
                }
            )
            VStack{
                MainHeaderView()
                MainTimerView(timerButtonClicked: $timerButtonClicked, stopwatchButtonClicked: $stopwatchButtonClicked)
            }
        }
    }
}

#Preview {
    MainView()
}

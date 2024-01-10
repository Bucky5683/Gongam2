//
//  MainTimerView.swift
//  newGongam2
//
//  Created by 김서연 on 1/10/24.
//

import SwiftUI

struct MainTimerView: View {
    @Binding var timerButtonClicked: Bool
    @Binding var stopwatchButtonClicked: Bool
    var body: some View {
        VStack {
            Text("타이머")
            TimersButtonView(timerButtonCliked: $timerButtonClicked, stopwatchButtonClicked: $stopwatchButtonClicked, isTimer: true)
            TimersButtonView(timerButtonCliked: $timerButtonClicked, stopwatchButtonClicked: $stopwatchButtonClicked, isTimer: false)
        }
    }
}

struct TimersButtonView: View {
    @Binding var timerButtonCliked: Bool
    @Binding var stopwatchButtonClicked: Bool
    var isTimer: Bool
    var body: some View {
        HStack{
            if isTimer{
                Text("⏰")
                Text("타이머")
                Button {
                    print("TimerButton Tabbed!")
                    timerButtonCliked = true
                    stopwatchButtonClicked = false
                } label: {
                    HStack{
                        Text("GO")
                        Image("goButtonIcon")
                    }
                }
            } else {
                Text("⏱️")
                Text("스톱워치")
                Button {
                    print("StopwatchButton Tabbed!")
                    timerButtonCliked = false
                    stopwatchButtonClicked = true
                } label: {
                    HStack{
                        Text("GO")
                        Image("goButtonIcon")
                    }
                }
            }
        }
    }
}

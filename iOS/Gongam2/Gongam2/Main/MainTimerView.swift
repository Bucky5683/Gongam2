//
//  MainTimerView.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/13.
//

import SwiftUI

struct MainTimerView: View {
    var body: some View {
        VStack(spacing: 15.0){  //타이머
            MainFuncTitleView(moreButton: false, title: "타이머")
            MainTimerButtonView(type: .timer)
            MainTimerButtonView(type: .stopWatch)
            Spacer()
        }.frame(height: 221)
    }
}

#Preview {
    MainTimerView()
}

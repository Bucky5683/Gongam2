//
//  MainTimerButtonView.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/13.
//

import SwiftUI
enum timerButtonType{
    case timer
    case stopWatch
}
struct MainTimerButtonView: View {
    var type: timerButtonType
    var body: some View {
        GeometryReader{ geometry in
            HStack{
                Spacer()
                HStack(spacing: 10.0){
                    switch(type){
                    case .timer:
                        Text("⏰")
                            .font(.system(size: 20))
                            .fontWeight(.medium)
                            .foregroundStyle(.darkBlue414756)
                        Text("타이머")
                            .font(.system(size: 15))
                            .fontWeight(.medium)
                            .foregroundStyle(.darkBlue414756)
                    case .stopWatch:
                        Text("⏱️")
                            .font(.system(size: 20))
                            .fontWeight(.medium)
                            .foregroundStyle(.darkBlue414756)
                        Text("스톱워치")
                            .font(.system(size: 15))
                            .fontWeight(.medium)
                            .foregroundStyle(.darkBlue414756)
                    }
                    Spacer()
                    HStack{
                        Text("GO")
                            .font(.system(size: 15))
                            .fontWeight(.medium)
                            .foregroundColor(.whiteFFFFFF)
                        Image("goTriangle")
                            .resizable()
                            .frame(width: 15.0, height: 15.0)
                    }.frame(width: 80, height: 48).background(.darkBlue414756).cornerRadius(10, corners: [.topRight, .bottomRight])
                }
                .padding(.leading, 20)
                .frame(width: geometry.size.width-80, height: 48)
                .background(.whiteFFFFFF)
                .cornerRadius(10)
                .shadow(color: .black.opacity(0.1), radius: 5, x: 0, y: 2)
                Spacer()
            }
        }.frame(height: 48)
    }
}

#Preview {
    MainTimerButtonView(type: .stopWatch)
}

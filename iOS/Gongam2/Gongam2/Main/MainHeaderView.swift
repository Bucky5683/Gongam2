//
//  MainHeaderView.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/13.
//

import SwiftUI

struct MainHeaderView: View {
    var body: some View {
        GeometryReader{geometry in
            VStack(spacing: 15.0){  // 헤더
                Spacer().frame(height: 15)
                HStack{
                    Spacer()
                    Image("exampleImage")
                        .resizable()
                        .frame(width: 30, height: 30)
                        .cornerRadius(15)
                }.padding(.trailing, 15)
                Text("오늘 공부한 시간")
                    .font(.system(size: 12))
                    .underline()
                    .multilineTextAlignment(.center)
                    .foregroundColor(.whiteFFFFFF)
                
                Text("99:99:99")
                    .font(.system(size: 30).weight(.bold))
                    .multilineTextAlignment(.center)
                    .foregroundColor(.whiteFFFFFF)
                
                HStack{
                    Text("🔥")
                        .font(.system(size: 12))
                        .multilineTextAlignment(.center)
                    Text("목표까지")
                        .font(.system(size: 12))
                        .multilineTextAlignment(.center)
                    Text("99:99:99")
                        .font(.system(size: 12).weight(.bold))
                        .multilineTextAlignment(.center)
                    Text("🔥")
                        .font(.system(size: 12))
                        .multilineTextAlignment(.center)
                }.padding(.leading, 30)
                    .padding(.trailing, 30)
                    .padding(.top, 7)
                    .padding(.bottom, 7)
                    .background(.grayA5ABBD)
                    .cornerRadius(20)
                Spacer()
            }.frame(width: geometry.size.width, height: 280).background(Color.darkBlue414756)
                .alignmentGuide(.top) { _ in 0 }
        }
    }
}

#Preview {
    MainHeaderView()
}

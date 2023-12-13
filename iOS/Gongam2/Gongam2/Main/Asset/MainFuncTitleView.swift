//
//  MainFuncTitleView.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/13.
//

import SwiftUI

struct MainFuncTitleView: View {
    var moreButton: Bool
    var title: String
    var body: some View {
        HStack{
            Text(title)
                .fontWeight(.bold)
                .underline()
                .foregroundStyle(.darkBlue414756)
                .padding(40).frame(height: 30)
            Spacer()
            if moreButton{
                Button("더보기 >", action: {
                    print("더보기")
                }).font(.system(size: 12)).foregroundColor(.grayA5ABBD).padding(40).frame(height: 30)
            }
        }.frame(height: 30)
    }
}

#Preview {
    MainFuncTitleView(moreButton: true, title: "더보기")
}

//
//  MainAverageRank.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/13.
//

import SwiftUI

struct MainAverageRank: View {
    var averageTime: String
    var body: some View {
        HStack(spacing: 11){
            Rectangle()
                .foregroundColor(.grayA5ABBD)
                .frame(width: 67, height: 11)
                .cornerRadius(10)
                .shadow(color: .black.opacity(0.1), radius: 2, x: 0, y: 4)
            HStack(spacing: 3){
                Text("평균 공부시간 :")
                    .font(.system(size: 10))
                    .foregroundColor(.whiteFFFFFF)
                    .padding(.leading, 15)
                    .padding(.vertical, 3)
                Text(averageTime)
                    .font(.system(size: 10))
                    .fontWeight(.bold)
                    .padding(.trailing, 15)
                    .padding(.vertical, 3)
                    .foregroundColor(.whiteFFFFFF)
                    .lineLimit(1) // 텍스트가 한 줄로 표시되도록 설정
                    .minimumScaleFactor(0.5) // 텍스트 축소 비율 설정
            }.fixedSize(horizontal: false, vertical: true)
                .background(.grayA5ABBD).cornerRadius(10)
                .shadow(color: .black.opacity(0.1), radius: 2, x: 0, y: 4)
        }
    }
}

#Preview {
    MainAverageRank(averageTime: "9999:99:99")
}

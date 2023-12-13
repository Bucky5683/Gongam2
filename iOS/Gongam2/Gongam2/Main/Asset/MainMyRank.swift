//
//  MainMyRank.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/13.
//

import SwiftUI

struct MainMyRank: View {
    var nickName: String
    var rank: String
    var higherThanAverage: Bool
    var body: some View {
        if higherThanAverage{
            HStack(spacing: 11){
                Rectangle().frame(width: 67, height: 11).foregroundColor(.pinkECB9C2).cornerRadius(10).shadow(color: .black.opacity(0.1), radius: 5, x: 0, y: 2)
                HStack(spacing: 13){
                    Text("\(nickName)님의 등수")
                        .font(.system(size: 12))
                        .fontWeight(.medium)
                        .padding(.leading, 15)
                        .padding(.vertical, 15)
                        .foregroundColor(.darkBlue414756)
                    Text(rank)
                        .font(.system(size: 20))
                        .fontWeight(.bold)
                        .padding(.vertical, 11)
                        .padding(.trailing, 15)
                        .foregroundColor(.darkBlue414756)
                }.background(.pinkECB9C2)
                    .cornerRadius(10)
                    .shadow(color: .black.opacity(0.1), radius: 5, x: 0, y: 2)
            }
        }else{
            HStack(spacing: 11){
                Rectangle().frame(width: 67, height: 11).foregroundColor(.skyBlueCBD9FE).cornerRadius(10).shadow(color: .black.opacity(0.1), radius: 5, x: 0, y: 2)
                HStack(spacing: 13){
                    Text("\(nickName)님의 등수")
                        .font(.system(size: 12))
                        .fontWeight(.medium)
                        .padding(.leading, 15)
                        .padding(.vertical, 15)
                        .foregroundColor(.darkBlue414756)
                    Text(rank)
                        .font(.system(size: 20))
                        .fontWeight(.bold)
                        .padding(.vertical, 11)
                        .padding(.trailing, 15)
                        .foregroundColor(.darkBlue414756)
                }.background(.skyBlueCBD9FE)
                    .cornerRadius(10)
                    .shadow(color: .black.opacity(0.1), radius: 5, x: 0, y: 2)
            }
        }
    }
}

#Preview {
    MainMyRank(nickName: "응애!", rank: "999+", higherThanAverage: false)
}

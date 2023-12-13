//
//  MainFuncTexts.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/13.
//

import SwiftUI

enum textType{
    case rank
    case myReport
}

struct MainFuncTexts: View {
    var time: String
    var textType: textType
    var minOrMore: Bool
    var body: some View {
        switch(textType){
        case .rank:
            HStack{
                Text("평균보다").font(.system(size: 12)).fontWeight(.medium).foregroundColor(.darkBlue414756)
                if !minOrMore{
                    Text(time).font(.system(size: 15)).fontWeight(.bold).foregroundColor(.blue5C84FF)
                    Text("덜 공부했어요!").font(.system(size: 12)).fontWeight(.medium).foregroundColor(.darkBlue414756)
                }else{
                    Text(time).font(.system(size: 15)).fontWeight(.bold).foregroundColor(.redFF0000)
                    Text("더 공부했어요!").font(.system(size: 12)).fontWeight(.medium).foregroundColor(.darkBlue414756)
                }
            }
        case .myReport:
            HStack{
                Text("이번 주에 평균").font(.system(size: 12)).fontWeight(.medium).foregroundColor(.darkBlue414756)
                if !minOrMore{
                    Text(time).font(.system(size: 15)).fontWeight(.bold).foregroundColor(.blue5C84FF)
                }else{
                    Text(time).font(.system(size: 15)).fontWeight(.bold).foregroundColor(.redFF0000)
                }
                Text("만큼 공부했어요!").font(.system(size: 12)).fontWeight(.medium).foregroundColor(.darkBlue414756)
            }
        }
    }
}

#Preview {
    MainFuncTexts(time: "9999:99:99", textType: .rank, minOrMore: false)
}

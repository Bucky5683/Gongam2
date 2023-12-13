//
//  MyReportView.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/13.
//

import SwiftUI

struct MyReportView: View {
    var weeklyReport: Dictionary<String,(Int,String)>
    var body: some View {
        MainFuncTitleView(moreButton: true, title: "마이 리포트")
        MainFuncTexts(time: "9999:99:99", textType: .myReport, minOrMore: false)
    }
}

#Preview {
    MyReportView(weeklyReport: [
        "S" : (-99, "h"),
        "M" : (-99, "h"),
        "T" : (-99, "h"),
        "W" : (-99, "h"),
        "T" : (-99, "h"),
        "F" : (-99, "h"),
        "S" : (-99, "h")
    ])
}

//
//  MyReportView.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/13.
//

import SwiftUI

struct MyReportView: View {
    var weeklyReport: [(Int, String)]
    var range = Range<Int>(uncheckedBounds: (0,6))
    var body: some View {
        VStack(spacing: 30){
            MainFuncTitleView(moreButton: true, title: "마이 리포트")
            HStack(spacing: 10) {
                DayReportCircle(Aday: "S", Atime: weeklyReport[0].0, AtimeMeasure: weeklyReport[0].1)
                DayReportCircle(Aday: "M", Atime: weeklyReport[1].0, AtimeMeasure: weeklyReport[1].1)
                DayReportCircle(Aday: "T", Atime: weeklyReport[2].0, AtimeMeasure: weeklyReport[2].1)
                DayReportCircle(Aday: "W", Atime: weeklyReport[3].0, AtimeMeasure: weeklyReport[3].1)
            }
            HStack(spacing: 10){
                DayReportCircle(Aday: "T", Atime: weeklyReport[4].0, AtimeMeasure: weeklyReport[4].1)
                DayReportCircle(Aday: "F", Atime: weeklyReport[5].0, AtimeMeasure: weeklyReport[5].1)
                DayReportCircle(Aday: "S", Atime: weeklyReport[6].0, AtimeMeasure: weeklyReport[6].1)
            }
            MainFuncTexts(time: "9999:99:99", textType: .myReport, minOrMore: false)
        }.frame(height: 244)
    }
}

#Preview {
    MyReportView(weeklyReport: [(99, "h"),(0, "m"),(-99, "h"),(-99, "h"),(-99, "h"),(-99, "h"),(-99, "h")])
}

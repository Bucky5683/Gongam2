//
//  MainMyReportGridView.swift
//  newGongam2
//
//  Created by 김서연 on 1/10/24.
//

import SwiftUI

struct MainMyReportGridView: View {
    var body: some View {
        VStack{
            HStack{
                MyReportGridItem(weeklys: "S")
                MyReportGridItem(weeklys: "M")
                MyReportGridItem(weeklys: "T")
                MyReportGridItem(weeklys: "W")
            }
            HStack {
                MyReportGridItem(weeklys: "T")
                MyReportGridItem(weeklys: "F")
                MyReportGridItem(weeklys: "S")
            }
        }
    }
}

struct MyReportGridItem: View {
    var weeklys = ""
    var body: some View{
        VStack{
            Text(weeklys)
            Text("-99h")
        }
    }
}

#Preview {
    MainMyReportGridView()
}

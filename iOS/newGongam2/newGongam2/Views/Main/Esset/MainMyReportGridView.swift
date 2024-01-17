//
//  MainMyReportGridView.swift
//  newGongam2
//
//  Created by 김서연 on 1/10/24.
//

import SwiftUI

struct MainMyReportGridView: View {
    @EnvironmentObject var userTimeData: UserTimeData
    @EnvironmentObject var userData: UserData
    @Binding var viewModel: MainViewModel
    var body: some View {
        VStack{
            if self.viewModel.thisWeekDataes.count > 0{
                HStack{
                    self.viewModel.thisWeeks[0]
                    self.viewModel.thisWeeks[1]
                    self.viewModel.thisWeeks[2]
                    self.viewModel.thisWeeks[3]
                }
                HStack {
                    self.viewModel.thisWeeks[4]
                    self.viewModel.thisWeeks[5]
                    self.viewModel.thisWeeks[6]
                }
            } else {
                ProgressView()
            }
            Text("이번 주는 평균 \(self.userTimeData.averageTime.timeToTextForWeekly())만큼 공부했어요")
        }.background(.whiteFFFFFF, ignoresSafeAreaEdges: .all)
            .onAppear(){
                self.userTimeData.downloadData()
                self.viewModel.makeWeeklyChartReport(userTimeData: self.userTimeData, userData: self.userData)
            }
    }
}

struct MyReportGridItem: View {
    var weeklys = ""
    var date = ""
    var studyTime = 0
    
    static func == (lhs: MyReportGridItem, rhs: MyReportGridItem) -> Bool {
        // Implement your own equality check based on your requirements
        return lhs.weeklys == rhs.weeklys &&
                lhs.date == rhs.date &&
                lhs.studyTime == rhs.studyTime
    }
    
    var body: some View{
        VStack{
            Text(self.weeklys)
            if Int(self.studyTime / 3600) != 0{
                Text("\(self.studyTime/3600)h")
            } else if Int(self.studyTime / 60) != 0{
                Text("\(self.studyTime/60)m")
            } else if self.studyTime != 0{
                Text("\(self.studyTime)s")
            } else {
                Text("-")
            }
        }
    }
}

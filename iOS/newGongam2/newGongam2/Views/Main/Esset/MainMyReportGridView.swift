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
    
    private func colorSwitch() -> Color {
        if self.userTimeData.averageTime < self.userTimeData.totalStudyTime{
            return Color.redFF0000
        } else if self.userTimeData.averageTime > self.userTimeData.totalStudyTime{
            return Color.blue5C84FF
        } else {
            return Color.lightGrayA5ABBD
        }
    }
    
    var body: some View {
        VStack(spacing: 28){
            if self.viewModel.thisWeekDataes.count > 0{
                VStack(spacing: 8){
                    HStack(spacing: 16){
                        self.viewModel.thisWeeks[0]
                        self.viewModel.thisWeeks[1]
                        self.viewModel.thisWeeks[2]
                        self.viewModel.thisWeeks[3]
                    }
                    HStack(spacing: 16) {
                        self.viewModel.thisWeeks[4]
                        self.viewModel.thisWeeks[5]
                        self.viewModel.thisWeeks[6]
                    }
                }
            } else {
                ProgressView()
            }
            HStack(alignment: .bottom){
                Text("이번 주는 평균")
                    .font(Font.system(size: 12).weight(.medium))
                Text(self.userTimeData.totalStudyTime.timeToTextForWeekly())
                    .font(Font.system(size: 15).weight(.bold))
                    .foregroundColor(self.colorSwitch())
                Text("만큼 공부했어요")
                    .font(Font.system(size: 12).weight(.medium))
            }
            
        }.background(.whiteFFFFFF)
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
    
    private func colorSwitch() -> Color {
        if self.studyTime < 0{
            return Color.blue5C84FF
        } else if self.studyTime > 0{
            return Color.redFF0000
        } else {
            return Color.lightGrayA5ABBD
        }
    }
    
    var body: some View{
        VStack{
            Text(self.weeklys)
                .font(Font.system(size: 15).bold())
                .foregroundColor(.black)
            if Int(self.studyTime / 3600) != 0{
                Text("\(self.studyTime/3600)h")
                    .font(Font.system(size: 10).weight(.regular))
                    .foregroundColor(self.colorSwitch())
            } else if Int(self.studyTime / 60) != 0{
                Text("\(self.studyTime/60)m")
                    .font(Font.system(size: 10).weight(.regular))
                    .foregroundColor(self.colorSwitch())
            } else if self.studyTime != 0{
                Text("\(self.studyTime)s")
                    .font(Font.system(size: 10).weight(.regular))
                    .foregroundColor(self.colorSwitch())
            } else {
                Text("-")
                    .font(Font.system(size: 10).weight(.regular))
                    .foregroundColor(self.colorSwitch())
            }
        }.frame(width: 50, height: 50)
            .background(.whiteFFFFFF)
            .cornerRadius(50)
            .shadow(color: .black.opacity(0.25), radius: 2, x: 0, y: 4)
    }
}

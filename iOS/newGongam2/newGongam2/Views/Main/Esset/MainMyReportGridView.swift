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
    @State var thisWeekDataes : [String:Int] = [:]
    @State var thisWeeks: [MyReportGridItem] = []
    @State var average: Int = 0
    var body: some View {
        VStack{
            if thisWeekDataes.count > 0{
                HStack{
                    thisWeeks[0]
                    thisWeeks[1]
                    thisWeeks[2]
                    thisWeeks[3]
                }
                HStack {
                    thisWeeks[4]
                    thisWeeks[5]
                    thisWeeks[6]
                }
            } else {
                ProgressView()
            }
            Text("이번 주는 평균 \(average.timeToTextForWeekly())만큼 공부했어요")
        }.background(.whiteFFFFFF, ignoresSafeAreaEdges: .all)
            .onAppear(){
                self.userTimeData.downloadData()
                self.makeWeeklyChartReport()
            }
    }
    private func makeWeeklyChartReport(){
        let data = self.userTimeData.studyDataes
        let todayDate = Date().getCurrentDateAsString()
        let thisWeek = Date().getWeeksAgoSunday(week: 1)
        for (_, value) in self.userTimeData.divideDataByWeeks(startDate: thisWeek?[0].getCurrentDateAsString() ?? Date().getCurrentDateAsString(), endDate: todayDate, data: data){
            for (dKey, dValue) in value {
                self.thisWeekDataes[dKey] = dValue.totalStudyTime - self.userData.goalStudyTime
            }
        }
        var sum = 0
        for (key, value) in thisWeekDataes {
            self.thisWeeks.append(MyReportGridItem(weeklys: key.getDaysFromString(), date: key, studyTime: value))
            sum += value
        }
        self.thisWeeks.sort(by: {$0.date < $1.date})
        self.average = Int(sum/7)
    }
}
struct MyReportGridItem: View {
    var weeklys = ""
    var date = ""
    var studyTime = 0
    var body: some View{
        VStack{
            Text(self.weeklys)
            if Int(self.studyTime / 3600) != 0{
                Text("\(self.studyTime/3600)h")
            } else if Int(self.studyTime / 60) != 0{
                Text("\(self.studyTime/60)m")
            } else {
                Text("\(self.studyTime)s")
            }
        }
    }
}

//
//  ReportView.swift
//  newGongam2
//
//  Created by 김서연 on 1/10/24.
//

import SwiftUI
import Charts

class ReportViewModel: ObservableObject {
    @Published var weeklyChartReport: [String : [dailyChartReport]] = [:]
    
}

struct ReportView: View {
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @EnvironmentObject var userTimeData: UserTimeData
    @State var weeklyChartReport: [String : [dailyChartReport]] = [:]
    @State var weeklyKeys: [String] = []
    @State var idx: Int = 0
    var body: some View {
        VStack{
            if weeklyKeys.count > 0{
                Text(weeklyKeys[self.idx])
                HStack{
                    Button {
                        print("지난주")
                        idx -= 1
                        if idx < 0 {
                            idx = 0
                        }
                    } label: {
                        Image("reportLeftButton")
                    }.disabled(idx == 0)
                    Chart {
                        ForEach(weeklyChartReport[weeklyKeys[self.idx]] ?? [dailyChartReport(date: Date().getCurrentDateAsString(), totalStudyTime: 0)]) { weekly in
                            let date = weekly.date.mmdd()
                            BarMark(
                                x: .value("date", date),
                                y: .value("time", weekly.totalStudyTime)
                            )
                        }
                    }.padding()
                    Button {
                        print("다음주")
                        idx += 1
                        if idx > 3 {
                            idx = 3
                        }
                    } label: {
                        Image("reportRightButton")
                    }.disabled(idx == 3)
                }
                Spacer()
                ForEach(weeklyChartReport[weeklyKeys[self.idx]] ?? [dailyChartReport(date: Date().getCurrentDateAsString(), totalStudyTime: 0)]) { weekly in
                    let dailyReport = self.userTimeData.studyDataes[weekly.date]
                    dailyReportView(day: weekly.date.getDaysFromString(), totalStudyTime: dailyReport?.totalStudyTime ?? 0, timerStudyTime: dailyReport?.timerStudyTime ?? 0, stopwatchStudyTime: dailyReport?.stopwatchStudyTime ?? 0, backgroundColor: .lightBlueCBD9FE)
                }
            } else {
                ProgressView()
            }
        }
        .navigationBarTitle("마이 리포트",displayMode: .inline)
        .navigationBarBackButtonHidden(true)
        .navigationBarItems(leading: Button("Main"){self.coordinator.pop()})
        .onAppear(){
            self.loadData()
        }
    }
    
    private func loadData() {
        self.userTimeData.downloadData()
        self.makeWeeklyChartReport()
    }
    
    private func makeWeeklyChartReport(){
        let data = self.userTimeData.studyDataes
        let todayDate = Date().getCurrentDateAsString()
        let threeWeeksAgoDate = Date().getWeeksAgoSunday(week: 4)
        for (key, value) in self.userTimeData.divideDataByWeeks(startDate: threeWeeksAgoDate?[0].getCurrentDateAsString() ?? Date().getCurrentDateAsString(), endDate: todayDate, data: data){
            var listDailyChartReport: [dailyChartReport] = []
            for (dKey, dValue) in value {
                listDailyChartReport.append(dailyChartReport(date: dKey, totalStudyTime: dValue.totalStudyTime))
            }
            listDailyChartReport.sort(by: {$0.date < $1.date})
            self.weeklyChartReport[key] = listDailyChartReport
            self.weeklyKeys.append(key)
            self.weeklyKeys.sort()
        }
    }
}

struct dailyReportView: View {
    var day: String
    var totalStudyTime: Int
    var timerStudyTime: Int
    var stopwatchStudyTime: Int
    var backgroundColor: Color = .lightBlueCBD9FE
    var body: some View {
        HStack{
            Text(day)
            Text(totalStudyTime.timeToText())
            VStack {
                HStack{
                    Text("타이머")
                    Text(timerStudyTime.timeToText())
                }
                HStack{
                    Text("스탑워치")
                    Text(stopwatchStudyTime.timeToText())
                }
            }
        }.background(backgroundColor)
    }
}

struct dailyChartReport: Identifiable {
    var date: String
    var totalStudyTime: Int
    var id: String{ date }
}

#Preview {
    ReportView()
}

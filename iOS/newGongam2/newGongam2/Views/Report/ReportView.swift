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
    @EnvironmentObject var userData: UserData
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
                            BarMark(
                                x: .value("date", weekly.date),
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
            } else {
                ProgressView()
            }
        }
        .navigationBarTitle("마이 리포트",displayMode: .inline)
        .navigationBarBackButtonHidden(true)
        .navigationBarItems(leading: Button("Main"){self.coordinator.pop()})
        .onAppear(){
            self.makeWeeklyChartReport()
        }
    }
    
    func makeWeeklyChartReport(){
        for (key, value) in self.userTimeData.divideDataByWeeks(startDate: userTimeData.getFirstDayOfCurrentWeek(for: userTimeData.getWeeksAgo(from: Date(), week: 3) ?? Date())?.getCurrentDateAsString() ?? Date().getCurrentDateAsString(), endDate: userTimeData.getCurrentDateAsString(), data: self.userTimeData.studyDataes){
            var listDailyChartReport: [dailyChartReport] = []
            for (dKey, dValue) in value {
                listDailyChartReport.append(dailyChartReport(date: dKey, totalStudyTime: dValue.totalStudyTime))
            }
            weeklyChartReport[key] = listDailyChartReport
            weeklyKeys.append(key)
        }
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

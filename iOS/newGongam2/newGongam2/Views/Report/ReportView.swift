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
    @EnvironmentObject var userDataManager: UserDataManager
    @State var weeklyChartReport: [String : [dailyChartReport]] = [:]
    @State var weeklyKeys: [String] = []
    @State var idx: Int = 0
    init() {
        //Use this if NavigationBarTitle is with Large Font
        UINavigationBar.appearance().largeTitleTextAttributes = [.foregroundColor: UIColor.black]
        //Use this if NavigationBarTitle is with displayMode = .inline
        UINavigationBar.appearance().titleTextAttributes = [.foregroundColor: UIColor.black]
    }
    
    var body: some View {
        ScrollView{
            VStack{
                if weeklyKeys.count > 0{
                    Text(weeklyKeys[self.idx])
                        .font(Font.system(size: 15).bold())
                        .multilineTextAlignment(.center)
                        .foregroundColor(.darkBlue414756)
                    HStack(spacing: 10){
                        Button {
                            print("지난주")
                            idx -= 1
                            if idx < 0 {
                                idx = 0
                            }
                        } label: {
                            Image("reportLeftButton")
                                .resizable()
                                .frame(width: 20, height: 20)
                        }.disabled(idx == 0)
                        Chart {
                            ForEach(weeklyChartReport[weeklyKeys[self.idx]] ?? [dailyChartReport(date: Date().getCurrentDateAsString(), totalStudyTime: 0)]) { weekly in
                                let date = weekly.date.mmdd()
                                let backgroundColor: Color = setBackgroundColor(totalStudyTime: weekly.totalStudyTime)
                                BarMark(
                                    x: .value("date", date),
                                    y: .value("time", weekly.totalStudyTime)
                                )
                                .foregroundStyle(backgroundColor)
                            }
                            RuleMark(
                                y: .value("Goal", self.userDataManager.userInfo.goalStudyTime)
                            ).lineStyle(StrokeStyle(lineWidth: 3))
                                .foregroundStyle(Color.blue5C84FF)
                            RuleMark(
                                y: .value("Average", self.userDataManager.rankRecord.averageTime)
                            ).lineStyle(StrokeStyle(lineWidth: 3))
                                .foregroundStyle(Color.redFF0000)
                        }.chartYAxis(.hidden)
                            .frame(height: 180)
                        .padding()
                        Button {
                            print("다음주")
                            idx += 1
                            if idx > 3 {
                                idx = 3
                            }
                        } label: {
                            Image("reportRightButton")
                                .resizable()
                                .frame(width: 20, height: 20)
                        }.disabled(idx == 3)
                    }
                    Spacer()
                    ForEach(weeklyChartReport[weeklyKeys[self.idx]] ?? [dailyChartReport(date: Date().getCurrentDateAsString(), totalStudyTime: 0)]) { weekly in
                        let dailyReport = self.userDataManager.recordUserStudy.studyDataes[weekly.date]
                        let backgroundColor = self.setBackgroundColor(totalStudyTime: dailyReport?.totalStudyTime ?? 0)
                        dailyReportView(day: weekly.date.getDaysFromString(), totalStudyTime: dailyReport?.totalStudyTime ?? 0, timerStudyTime: dailyReport?.timerStudyTime ?? 0, stopwatchStudyTime: dailyReport?.stopwatchStudyTime ?? 0, backgroundColor: backgroundColor)
                    }
                } else {
                    ProgressView()
                }
            }
            .background(.whiteFFFFFF)
        }
        .navigationBarTitle("마이 리포트",displayMode: .inline)
        .navigationBarBackButtonHidden(true)
        .navigationBarItems(leading: Button("Main"){self.coordinator.pop()})
        .onAppear(){
            self.loadData()
        }
        .background(.whiteFFFFFF)
    }
    
    private func loadData() {
        self.userDataManager.readStudyData()
        self.userDataManager.readRankData()
        self.makeWeeklyChartReport()
    }
    
    private func makeWeeklyChartReport(){
        let data = self.userDataManager.recordUserStudy.studyDataes
        let todayDate = Date().getCurrentDateAsString()
        let threeWeeksAgoDate = Date().getWeeksAgoSunday(week: 4)
        for (key, value) in self.userDataManager.divideDataByWeeks(startDate: threeWeeksAgoDate?[0].getCurrentDateAsString() ?? Date().getCurrentDateAsString(), endDate: todayDate, data: data){
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
    
    private func setBackgroundColor(totalStudyTime: Int) -> Color{
        if totalStudyTime >= self.userDataManager.userInfo.goalStudyTime{
            return Color.lightBlueCBD9FE
        } else if totalStudyTime >= self.userDataManager.rankRecord.averageTime {
            return Color.pinkECB9C2
        } else {
            return Color.lightGrayA5ABBD
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
        HStack(spacing: 20){
            Text(day).padding(.leading, 20)
                .font(Font.system(size: 20).bold())
                .foregroundColor(.darkBlue414756)
            Text(totalStudyTime.timeToText())
                .font(Font.system(size: 17).bold())
                .foregroundColor(.darkBlue414756)
            VStack(alignment: .leading, spacing: 5) {
                HStack{
                    Text("타이머")
                        .font(Font.system(size: 13).bold())
                        .multilineTextAlignment(.leading)
                        .foregroundColor(.darkBlue414756)
                    Spacer()
                    Text(timerStudyTime.timeToText())
                        .font(Font.system(size: 13))
                        .multilineTextAlignment(.trailing)
                        .foregroundColor(.darkBlue414756)
                }
                HStack{
                    Text("스탑워치")
                        .font(Font.system(size: 13).bold())
                        .multilineTextAlignment(.leading)
                        .foregroundColor(.darkBlue414756)
                    Spacer()
                    Text(stopwatchStudyTime.timeToText())
                        .font(Font.system(size: 13))
                        .multilineTextAlignment(.trailing)
                        .foregroundColor(.darkBlue414756)
                }
            }.padding(.trailing, 20)
                .padding(.top, 8)
                .padding(.bottom, 8)
        }.background(backgroundColor)
            .cornerRadius(10)
            .shadow(color: .black.opacity(0.25), radius: 2, x: 0, y: 4)
            .padding(.leading, 46)
            .padding(.trailing, 47)
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

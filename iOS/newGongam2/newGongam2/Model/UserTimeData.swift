//
//  UserTimeData.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import Foundation
import UIKit

enum userTimeDataType {
    case TodayStudyDataes
    case TodayStudyTimerTimes
    case TodayStudyStopwatchTimes
}

class UserTimeData: ObservableObject{
    var id: String = ""
    var name: String = ""
    var profileURL: String = ""
    var email: String = ""
    var totalStudyTime: Int = 0
    @Published var studyDataes: Dictionary<String, dailyTimerData> = [:]
    @Published var myRank = 0
    @Published var averageTime = 0
    @Published var top10User : [String : [String : Any]] = [:]
    
    init(id: String){
        self.id = id
        let today = Date().getCurrentDateAsString()
        self.studyDataes = [today:dailyTimerData()]
    }
    
    init(){
        self.id = ""
        let today = Date().getCurrentDateAsString()
        self.studyDataes = [today:dailyTimerData()]
    }
    
    func updateTimeData(stopwatch: Int, timer: Int){
        let today = Date().getCurrentDateAsString()
        self.studyDataes[today]?.stopwatchStudyTime += stopwatch
        self.studyDataes[today]?.timerStudyTime += timer
        self.studyDataes[today]?.totalStudyTime += stopwatch + timer
    }
    
    func divideDataByWeeks(startDate: String, endDate: String, data: [String: dailyTimerData]) -> [String:[String: dailyTimerData]] {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        var currentDate = dateFormatter.date(from: startDate)!
        var fixedDate:Date = dateFormatter.date(from: endDate)!
        if let endDay = dateFormatter.date(from: endDate){
            fixedDate = endDay.findSaturday() ?? Date()
        } else {
            print("Error: Unable to parse date from endDate")
        }
        
        print("fixedDate : \(fixedDate)")
        
        var weeklyData: [String:[String: dailyTimerData]] = [:]
        
        while currentDate < fixedDate {
            let weekStartDate = currentDate
            let weekEndDate = Calendar.current.date(byAdding: .day, value: 6, to: currentDate)!
            
            let weekKey = "\(dateFormatter.string(from: weekStartDate)) ~ \(dateFormatter.string(from: weekEndDate))"
            var weeklyEntry = [String: dailyTimerData]()
            // 해당 주의 데이터를 합산
            for day in 0...6 {
                let currentDay = Calendar.current.date(byAdding: .day, value: day, to: weekStartDate)!
                let currentDayKey = dateFormatter.string(from: currentDay)
                
                if let dailyData = data[currentDayKey] {
                    weeklyEntry[currentDayKey] = dailyData
                } else {
                    weeklyEntry[currentDayKey] = dailyTimerData()
                }
            }
            weeklyData[weekKey] = weeklyEntry
            
            // 다음 주로 이동
            currentDate = Calendar.current.date(byAdding: .weekOfYear, value: 1, to: currentDate)!
        }
        
        return weeklyData
        /*
         "한 주 0000.00.00 ~ 0000.00.00" :
            "하루 0000.00.00" :
                "기록 "  : Int
         */
    }
    
}
extension UserTimeData {
    func downloadUserTimeData() async {
        self.downloadData()
        self.downloadRankData()
    }
    func downloadData() {
        FirebassDataManager.shared.readUserTimeDataFromFirebase { [weak self] timeData in
            guard let timeData = timeData else {
                self?.setNewFetchTimeData()
                return
            }
            
            self?.id = timeData.id
            self?.studyDataes = timeData.studyDataes
        }
    }
    
    func downloadRankData() {
        FirebassDataManager.shared.readRankDataFromFirebase { [weak self] timeData in
            guard let timeData = timeData else {
                self?.setNewFetchRankData()
                return
            }
            
            self?.myRank = timeData.myRank
            self?.averageTime = timeData.averageTime
            self?.top10User = timeData.top10User
            if timeData.name == "" {
                self?.setNewFetchRankData()
            } else {
                self?.name = timeData.name
                self?.profileURL = timeData.profileURL
                self?.totalStudyTime = timeData.totalStudyTime
            }
        }
    }
    
    func uploadTimeData(stopwatch: Int, timer: Int){
        let formattedDate = Date().getCurrentDateAsString()
        let timeData = [
            "totalStudyTime" : stopwatch + timer,
            "stopwatchStudyTime" : stopwatch,
            "timerStudyTime" : timer
        ]
        FirebassDataManager.shared.writeUserStudyDataToFirebase(uid: self.id, date: formattedDate, data: timeData)
    }
    
    func uploadRankData() {
        let rankData = [
            "name" : self.name,
            "email" : self.email,
            "profileImageURL" : self.profileURL,
            "totalStudyTime": self.totalStudyTime
        ] as [String : Any]
        
        FirebassDataManager.shared.writeRankDataToFirebase(uid: self.id, data: rankData)
    }
    
    func uploadRankData(name: String, profileURL: String, email: String) {
        self.name = name
        self.profileURL = profileURL
        self.email = email
        let rankData = [
            "name" : self.name,
            "email" : self.email,
            "profileImageURL" : self.profileURL,
            "totalStudyTime": self.totalStudyTime
        ] as [String : Any]
        
        FirebassDataManager.shared.writeRankDataToFirebase(uid: self.id, data: rankData)
    }
    func uploadData(stopwatch: Int, timer: Int){
        self.uploadTimeData(stopwatch: stopwatch, timer: timer)
        self.uploadRankData()
    }
    
    private func setNewFetchRankData(){
        let rankData = [
            "name" : self.name,
            "email" : self.email,
            "profileImageURL" : self.profileURL,
            "totalStudyTime": self.totalStudyTime
        ] as [String : Any]
        FirebassDataManager.shared.writeRankDataToFirebase(uid: self.id, data: rankData)
    }
    
    private func setNewFetchTimeData(){
        FirebassDataManager.shared.newFetchUserTimeData { [weak self] userTimeData in
            guard let userTimeData = userTimeData else { return }

            self?.id = userTimeData.id
            self?.name = userTimeData.name
            self?.profileURL = userTimeData.profileURL
            self?.email = userTimeData.email
            self?.studyDataes = userTimeData.studyDataes
            
            FirebassDataManager.shared.writeStudyTimeDataToFirebase(uid: userTimeData.id, data: userTimeData.studyDataes)
        }
        
        print("UserTImeData id : \(self.id)")
        print("UserTimeData studyDataes : \(self.studyDataes)")
    }
}

struct rankTopUser: Codable {
    var totalStudyTime: Int = 0
    var name: String = ""
    var email: String = ""
    var profileURL: String = ""
    
    init() {
        self.totalStudyTime = 0
        self.name = ""
        self.email = ""
        self.profileURL = ""
    }
}

struct dailyTimerData: Codable{
    var totalStudyTime: Int = 0
    var timerStudyTime: Int = 0
    var stopwatchStudyTime: Int = 0
    
    init() {
        self.timerStudyTime = 0
        self.stopwatchStudyTime = 0
        self.totalStudyTime = 0
    }
    init(timerStudyTima: Int, stopwatchStudyTime: Int) {
        self.timerStudyTime = timerStudyTima
        self.stopwatchStudyTime = stopwatchStudyTime
        self.totalStudyTime = timerStudyTima + stopwatchStudyTime
    }
}

extension Date {
    var sevenDaysOut: Date {
        Calendar.autoupdatingCurrent.date(byAdding: .day, value: 7, to: self) ?? self
    }
    
    var thirtyDaysOut: Date {
        Calendar.autoupdatingCurrent.date(byAdding: .day, value: 30, to: self) ?? self
    }
}

public enum TimerType{
    case timer
    case stopWatch
}

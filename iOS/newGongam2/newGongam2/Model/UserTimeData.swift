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
        self.studyDataes = [getCurrentDateAsString():dailyTimerData()]
    }
    
    init(){
        self.id = ""
        self.studyDataes = [getCurrentDateAsString():dailyTimerData()]
    }
    
    func updateTimeData(stopwatch: Int, timer: Int){
        let today = getCurrentDateAsString()
        self.studyDataes[today]?.stopwatchStudyTime += stopwatch
        self.studyDataes[today]?.timerStudyTime += timer
        self.studyDataes[today]?.totalStudyTime += stopwatch + timer
    }
    
    func getCurrentDateAsString() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        
        let currentDate = Date()
        let dateString = dateFormatter.string(from: currentDate)
        
        return dateString
    }
    
}
extension UserTimeData {
    func downloadUserTimeData() {
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
        let formattedDate = getCurrentDateAsString()
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

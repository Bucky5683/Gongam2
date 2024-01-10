//
//  UserTimeData.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import Foundation
import UIKit

class UserTimeData: ObservableObject{
    @Published var id: String = ""
    @Published var studyDataes: Dictionary<String, dailyTimerData> = [:]
    
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
        self.studyDataes[today]?.stopwatchStudyTime = stopwatch
        self.studyDataes[today]?.timerStudyTime = timer
        self.studyDataes[today]?.totalStudyTime = stopwatch + timer
    }
    
    func getCurrentDateAsString() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        
        let currentDate = Date()
        let dateString = dateFormatter.string(from: currentDate)
        
        return dateString
    }
    
    func downloadData(){
        FirebassDataManager.shared.readUserTimeDataFromFirebase { [weak self] timeData in
            guard let timeData = timeData else {
                self?.setNewFetchTimeData()
                return
            }
            
            self?.id = timeData.id
            self?.studyDataes = timeData.studyDataes
        }
    }
    
    func uploadData(stopwatch: Int, timer: Int){
        let formattedDate = getCurrentDateAsString()
        let timeData = [
            "totalStudyTime" : stopwatch + timer,
            "stopwatchStudyTime" : stopwatch,
            "timerStudyTime" : timer
        ]
        FirebassDataManager.shared.writeUserStudyDataToFirebase(uid: self.id, date: formattedDate, data: timeData)
    }
    func setNewFetchTimeData(){
        FirebassDataManager.shared.newFetchUserTimeData { [weak self] userTimeData in
            guard let userTimeData = userTimeData else { return }

            self?.id = userTimeData.id
            self?.studyDataes = userTimeData.studyDataes
            
            FirebassDataManager.shared.writeStudyTimeDataToFirebase(uid: userTimeData.id, data: userTimeData.studyDataes)
        }
        
        print("UserTImeData id : \(self.id)")
        print("UserTimeData studyDataes : \(self.studyDataes)")
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

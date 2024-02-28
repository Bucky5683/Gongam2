//
//  User.swift
//  newGongam2
//
//  Created by 김서연 on 2/27/24.
//

import SwiftUI
import UIKit

//MARK: 데이터 모델 정의
struct RecordUserStudy {
    // 현재까지 공부한 정보
    var studyDataes: Dictionary<String, dailyTimerData> = [:]
    
    init() {
        self.studyDataes = [Date().getCurrentDateAsString():dailyTimerData()]
    }
}

struct RankRecord {
    var myRank = 0
    var totalStudyTime: Int = 0
    var averageTime = 0
    var top5User : [String : [String : Any]] = [:]
}

struct UserInfo : Codable {
    // User basic information
    var profileImageURL: String
    var name: String
    var email: String
    var goalStudyTime: Int
    var lastUpdateDate: String
    
    // Today's study information
    var todayStudyTime: Int
    var stopwatchStudyTime: Int
    var timerStudyTime: Int
    
    enum CodingKeys: String, CodingKey {
        case profileImageURL
        case name
        case email
        case goalStudyTime
        case lastUpdateDate
        case todayStudyTime
        case stopwatchStudyTime
        case timerStudyTime
    }
    
    init(profileImageURL: String = "https://firebasestorage.googleapis.com/v0/b/gongam2-ff081.appspot.com/o/example2.jpeg?alt=media&token=2b4bbe1f-9ba2-49a7-bf54-87b5ca70eddd", name: String = "", email: String = "", goalStudyTime: Int = 0, lastUpdateDate: String = "", todayStudyTime: Int = 0, stopwatchStudyTime: Int = 0, timerStudyTime: Int = 0) {
        self.profileImageURL = profileImageURL
        self.name = name
        self.email = email
        self.goalStudyTime = goalStudyTime
        self.lastUpdateDate = lastUpdateDate
        self.todayStudyTime = todayStudyTime
        self.stopwatchStudyTime = stopwatchStudyTime
        self.timerStudyTime = timerStudyTime
    }
}

//MARK: 데이터 변수 저장
class UserDataManager : ObservableObject{
    @Published var id = ""
    @Published var isNewUser: Bool = false
    @Published var userInfo = UserInfo()
    @Published var recordUserStudy = RecordUserStudy()
    @Published var rankRecord = RankRecord()
}

//MARK: 데이터 읽기/쓰기
extension UserDataManager {
    func readUserInfo() {
        FirebassDataManager.shared.getUserInfo(uid: self.id) { [weak self] userData in
            if let userData = userData {
                DispatchQueue.main.async {
                    self?.isNewUser = false
                    if userData.lastUpdateDate != Date().getCurrentDateAsString() {
                        self?.userInfo.name = userData.name
                        self?.userInfo.profileImageURL = userData.profileImageURL
                        self?.userInfo.email = userData.email
                        self?.userInfo.goalStudyTime = userData.goalStudyTime
                        self?.userInfo.todayStudyTime = 0
                        self?.userInfo.stopwatchStudyTime = 0
                        self?.userInfo.timerStudyTime = 0
                        self?.userInfo.lastUpdateDate = Date().getCurrentDateAsString()
                    } else {
                        self?.userInfo = userData
                    }
                }
            } else {
                DispatchQueue.main.async{
                    self?.isNewUser = true
                    self?.writeUserInfo()
                }
            }
        }
    }
    func writeUserInfo() {
        self.userInfo.lastUpdateDate = Date().getCurrentDateAsString()
        FirebassDataManager.shared.saveUserInfo(uid: self.id, userInfo: self)
    }
    
    func readStudyData(){
        FirebassDataManager.shared.getStudyData(uid: self.id) { [weak self] studyData in
            if let studyData = studyData {
                DispatchQueue.main.async {
                    self?.isNewUser = false
                    self?.recordUserStudy = studyData
                }
            } else {
                // 사용자 정보가 없는 경우에 대한 처리를 여기에 추가
                DispatchQueue.main.async {
                    self?.isNewUser = true
                    self?.recordUserStudy = RecordUserStudy()
                    self?.writeStudyData()
                }
            }
        }
    }
    func writeStudyData() {
        FirebassDataManager.shared.saveStudyData(uid: self.id, date: Date().getCurrentDateAsString(), studyData: self)
    }
    
    func readRankData() {
        FirebassDataManager.shared.getRank(uid: self.id) { [weak self] rankData in
            guard let rankData = rankData else {
                self?.isNewUser = true
                self?.writeRankData()
                return
            }
            
            self?.isNewUser = false
            self?.rankRecord = rankData
        }
    }
    func writeRankData() {
        FirebassDataManager.shared.saveRank(uid: self.id, rank: self)
    }
    
    //이미지 업로드
    func writeProfileImage(_ image: UIImage) {
        FirebassDataManager.shared.writeImage(uid: self.id, image: image) { url, error in
            if let url = url {
                self.userInfo.profileImageURL = url
            } else {
                guard let error = error else {
                    print("PROFILE URL ERROR: \(String(describing: error))")
                    return
                }
            }
        }
    }
}

extension UserDataManager {
    func deleteUserData() -> Bool{
        var isSuccess = true
        
        FirebassDataManager.shared.deleteAuth() { error in
            if let error = error {
                print("AUTH DELETE ERROR! \(error)")
                isSuccess = false
            } else {
                print("SUCCESS : AUTH DATA DELETE")
            }
        }
        if isSuccess {
            FirebassDataManager.shared.deleteRankData(uid: self.id) { error in
                if let error = error {
                    print("RANK DELETE ERROR! \(error)")
                    isSuccess = false
                } else {
                    print("SUCCESS : RANK DELETE")
                    self.rankRecord = RankRecord()
                }
            }
            
            FirebassDataManager.shared.deleteUserInfo(uid: self.id) { error in
                if let error = error {
                    print("USERINFO DELETE ERROR! \(error)")
                    isSuccess = false
                } else {
                    print("SUCCESS : USER INFO DELETE")
                    self.userInfo = UserInfo()
                }
            }
            
            FirebassDataManager.shared.deleteStudyData(uid: self.id) { error in
                if let error = error {
                    print("STUDYDATAES DELETE ERROR! \(error)")
                    isSuccess = false
                } else {
                    print("SUCCESS : STUDY DATES DELETE")
                    self.recordUserStudy = RecordUserStudy()
                }
            }
            FirebassDataManager.shared.deleteImage(uid: self.id) { error in
                if let error = error {
                    print("IMAGE DELETE ERROR! \(error)")
                    isSuccess = false
                } else {
                    print("SUCCESS : PROFILE IMAGE DELETE")
                }
            }
        }
        
        if isSuccess {
            self.id = ""
            self.isNewUser = true
        }
        
        return isSuccess
    }
}

//MARK: 데이터 변환 함수
extension UserDataManager {
    func dictionaryRepresentation(_ type: UserDataTypeEnum) -> [String: Any] {
        switch(type){
        case .UserInfo: // 유저 정보
            return [
                "profileImageURL": self.userInfo.profileImageURL,
                "name": self.userInfo.name,
                "email": self.userInfo.email,
                "goalStudyTime": self.userInfo.goalStudyTime,
                "lastUpdateDate": self.userInfo.lastUpdateDate,
                "todayStudyTime": self.userInfo.todayStudyTime,
                "stopwatchStudyTime": self.userInfo.stopwatchStudyTime,
                "timerStudyTime": self.userInfo.timerStudyTime
            ]
            
        case .StudyDataes:  //오늘 공부한 데이터 저장
            let timeData = [
                "totalStudyTime" : self.userInfo.stopwatchStudyTime + self.userInfo.timerStudyTime,
                "stopwatchStudyTime" : self.userInfo.stopwatchStudyTime,
                "timerStudyTime" : self.userInfo.timerStudyTime
            ]
            return timeData
        case .Rank: // 랭크 데이터 저장
            let rankData = [
                "name" : self.userInfo.name,
                "email" : self.userInfo.email,
                "profileImageURL" : self.userInfo.profileImageURL,
                "totalStudyTime": self.rankRecord.totalStudyTime
            ] as [String : Any]
            return rankData
        }
    }
}

//MARK: TimeData 함수
extension UserDataManager {
    // today time data 갱신
    func updateTimeData(stopwatch: Int, timer: Int){
        let today = Date().getCurrentDateAsString()
        self.recordUserStudy.studyDataes[today]?.stopwatchStudyTime += stopwatch
        self.recordUserStudy.studyDataes[today]?.timerStudyTime += timer
        self.recordUserStudy.studyDataes[today]?.totalStudyTime += stopwatch + timer
        
        self.rankRecord.totalStudyTime 
    }
    
    //한 주 데이터 기록 갱신
    /*
     "한 주 0000.00.00 ~ 0000.00.00" :
        "하루 0000.00.00" :
            "기록 "  : Int
     */
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
    }
}

//MARK: 기타 모듈들
enum UserDataTypeEnum{
    case StudyDataes
    case Rank
    case UserInfo
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

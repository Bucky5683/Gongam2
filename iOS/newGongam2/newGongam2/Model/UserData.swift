//
//  UserData.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import Foundation
import UIKit

enum userDataType{
    case profileImageURL
    case name
    case todayStudyTime
    case goalStudyTime
    case stopwatchStudyTime
    case timerStudyTime
    case lastUpdateDate
    case email
}

class UserData: ObservableObject, Codable{
    @Published var id = ""
    @Published var profileImageURL: String = "https://firebasestorage.googleapis.com/v0/b/gongam2-ff081.appspot.com/o/example2.jpeg?alt=media&token=2b4bbe1f-9ba2-49a7-bf54-87b5ca70eddd"
    @Published var name: String = ""
    @Published var email: String = ""
    @Published var todayStudyTime: Int = 0
    @Published var goalStudyTime: Int = 0
    @Published var stopwatchStudyTime: Int = 0
    @Published var timerStudyTime: Int = 0
    @Published var lastUpdateDate: String = ""
    
    enum CodingKeys: String, CodingKey {
        case profileImageURL
        case name
        case todayStudyTime
        case goalStudyTime
        case stopwatchStudyTime
        case timerStudyTime
        case lastUpdateDate
        case email
    }
    init() {}
    required init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        profileImageURL = try container.decode(String.self, forKey: .profileImageURL)
        name = try container.decode(String.self, forKey: .name)
        todayStudyTime = try container.decode(Int.self, forKey: .todayStudyTime)
        goalStudyTime = try container.decode(Int.self, forKey: .goalStudyTime)
        stopwatchStudyTime = try container.decode(Int.self, forKey: .stopwatchStudyTime)
        timerStudyTime = try container.decode(Int.self, forKey: .timerStudyTime)
        lastUpdateDate = try container.decode(String.self, forKey: .lastUpdateDate)
        email = try container.decode(String.self, forKey: .email)
    }

    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(profileImageURL, forKey: .profileImageURL)
        try container.encode(name, forKey: .name)
        try container.encode(todayStudyTime, forKey: .todayStudyTime)
        try container.encode(goalStudyTime, forKey: .goalStudyTime)
        try container.encode(stopwatchStudyTime, forKey: .stopwatchStudyTime)
        try container.encode(timerStudyTime, forKey: .timerStudyTime)
        try container.encode(lastUpdateDate, forKey: .lastUpdateDate)
        try container.encode(email, forKey: .email)
    }
    
}

extension UserData {
    func downloadUserData(){
        FirebassDataManager.shared.readUserDataFromFirebase { [weak self] userData in
            guard let userData = userData else {
                self?.setnewUserData()
                return
            }
            self?.id = userData.id
            self?.name = userData.name
            self?.email = userData.email
            if userData.profileImageURL == "" {
                self?.profileImageURL = "https://firebasestorage.googleapis.com/v0/b/gongam2-ff081.appspot.com/o/example2.jpeg?alt=media&token=2b4bbe1f-9ba2-49a7-bf54-87b5ca70eddd"
            } else{
                self?.profileImageURL = userData.profileImageURL
            }
            self?.todayStudyTime = userData.todayStudyTime
            self?.goalStudyTime = userData.goalStudyTime
            self?.stopwatchStudyTime = userData.stopwatchStudyTime
            self?.timerStudyTime = userData.timerStudyTime
            self?.lastUpdateDate = userData.lastUpdateDate
        }
        
        print("UserData Name : \(self.name)")
        print("UserData Email : \(self.email)")
        print("UserData profileImageURL : \(self.profileImageURL)")
        print("UserData todayStudyTime : \(self.todayStudyTime)")
        print("UserData goalStudyTime : \(self.goalStudyTime)")
        print("UserData lastUpdateDate : \(self.lastUpdateDate)")
        
    }
    
    func uploadUserData(){
        self.lastUpdateDate = getCurrentDateAsString()
        let data = [
            "name" : self.name,
            "todayStudyTime" : self.todayStudyTime,
            "goalStudyTime" : self.goalStudyTime,
            "stopwatchStudyTime" : self.stopwatchStudyTime,
            "timerStudyTime" : self.timerStudyTime,
            "lastUpdateDate" : self.lastUpdateDate,
            "profileImageURL" : self.profileImageURL,
            "email" : self.email
        ] as [String : Any]
        FirebassDataManager.shared.writeUserDataToFirebase(uid: self.id, data: data)
        
    }
    
    func setnewUserData(){
        FirebassDataManager.shared.newFetchUserData { [weak self] userData in
            guard let userData = userData else { return }

            self?.id = userData.id
            self?.name = userData.name
            self?.profileImageURL = userData.profileImageURL
            self?.email = userData.email
            
            self?.lastUpdateDate = userData.getCurrentDateAsString()
            let data = [
                "name" : userData.name,
                "todayStudyTime" : userData.todayStudyTime,
                "goalStudyTime" : userData.goalStudyTime,
                "stopwatchStudyTime" : userData.stopwatchStudyTime,
                "timerStudyTime" : userData.timerStudyTime,
                "lastUpdateDate" : userData.getCurrentDateAsString(),
                "profileImageURL" : userData.profileImageURL,
                "email" : userData.email
            ]
            FirebassDataManager.shared.writeNewUserDataToFirebase(uid: userData.id, data: data)
            FirebassDataManager.shared.writeRankDataToFirebase(uid: userData.id, data: userData.todayStudyTime)
        }
    }
}
extension UserData {
    func toJSON() -> [String: Any] {
        let encoder = JSONEncoder()
        encoder.keyEncodingStrategy = .convertToSnakeCase  // 필요에 따라 설정
        do {
            let jsonData = try encoder.encode(self)
            let jsonObject = try JSONSerialization.jsonObject(with: jsonData, options: [])
            guard let jsonDict = jsonObject as? [String: Any] else {
                fatalError("Invalid JSON format")
            }
            return jsonDict
        } catch {
            fatalError("Error encoding UserData to JSON: \(error.localizedDescription)")
        }
    }
    
    func getCurrentDateAsString() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        
        let currentDate = Date()
        let dateString = dateFormatter.string(from: currentDate)
        
        return dateString
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

public enum weeklyDay{
    case Mon
    case Tue
    case Wen
    case Thu
    case Fri
    case Sat
    case Sun
}

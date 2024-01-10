//
//  FirebassDataManager.swift
//  newGongam2
//
//  Created by 김서연 on 1/9/24.
//

import Foundation
import Firebase
import FirebaseDatabaseSwift
import FirebaseAuth

class FirebassDataManager: ObservableObject{
    static let shared = FirebassDataManager()
    private init() {}
    
//    var userData = UserData()
//    var userStudyData = UserTimeData()
    
    func newFetchUserData(completion: @escaping (UserData?) -> Void) {
        guard let currentUser = Auth.auth().currentUser else {
            completion(nil)
            return
        }
        
        let userData = UserData()
        userData.id = currentUser.uid
        userData.name = currentUser.displayName ?? ""
        userData.profileImageURL = currentUser.photoURL?.absoluteString ?? ""
        userData.email = currentUser.email ?? ""

        print("userData.id = \(userData.id)")
        completion(userData)
    }
    
    func newFetchUserTimeData(completion: @escaping (UserTimeData?) -> Void) {
        guard let currentUser = Auth.auth().currentUser else {
            completion(nil)
            return
        }
        let userStudyData = UserTimeData()
        userStudyData.id = currentUser.uid

        print("userStudyData.id = \(userStudyData.id)")
        completion(userStudyData)
    }
    
    func readUserTimeDataFromFirebase(completion: @escaping (UserTimeData?) -> Void) {
        guard let currentUser = Auth.auth().currentUser else {
            completion(nil)
            return
        }
        
        let uid = currentUser.uid
        let ref = Database.database().reference().child("StudyDataes").child(uid)
        
        ref.getData{ (error, snapshot) in
            if let error = error {
                print("Error getting data: \(error.localizedDescription)")
                return
            }
            
            guard let dataDict = snapshot?.value as? [String:[String:Int]] else {
                print("Failed to cast snapshot value to [String: [String: Int]]")
                completion(nil)
                return
            }
            let userStudyData = UserTimeData()
            userStudyData.id = uid
            userStudyData.studyDataes = dataDict.mapValues { dailyData in
                return dailyTimerData(
                    timerStudyTima: dailyData["timerStudyTime"] ?? 0,
                    stopwatchStudyTime: dailyData["stopwatchStudyTime"] ?? 0
                )
            }
            completion(userStudyData)
        }
    }
    
    func readUserDataFromFirebase(completion: @escaping (UserData?) -> Void) {
        guard let currentUser = Auth.auth().currentUser else {
            completion(nil)
            return
        }
        
        let uid = currentUser.uid
        let ref = Database.database().reference().child("Users").child(uid)
        // 'your_node'에 있는 데이터 읽어오기
        ref.getData(completion:  { error, snapshot in
            guard error == nil else {
              print(error!.localizedDescription)
              return
            }
            if let dataDict = snapshot?.value as? [String: Any] {
                do {
                    // Firebase 데이터를 UserData 모델로 변환
                    let jsonData = try JSONSerialization.data(withJSONObject: dataDict)
                    let decoder = JSONDecoder()
                    let decodedUserData = try decoder.decode(UserData.self, from: jsonData)
                    decodedUserData.id = uid
                    decodedUserData.lastUpdateDate = decodedUserData.getCurrentDateAsString()
                    completion(decodedUserData)
                } catch {
                    print("Error decoding data: \(error.localizedDescription)")
                }
            } else {
                completion(nil)
                return
            }
        })
    }
    
    func writeUserStudyDataToFirebase(uid:String, date: String, data: Dictionary<String,Any>){
        let ref = Database.database().reference().child("Users").child(uid).child("studyData").child(date)
        
        ref.updateChildValues(data) { error, _ in
            if let error = error{
                print("Error uploading data: \(error.localizedDescription)")
            } else {
                print("Data uploaded successfully to key")
            }
        }
    }
    
    func writeNewUserDataToFirebase(uid: String, data: Dictionary<String, Any>){
        let ref = Database.database().reference().child("Users").child(uid)
        
        ref.updateChildValues(data) { error, _ in
            if let error = error{
                print("Error uploading data: \(error.localizedDescription)")
            } else {
                print("Data uploaded successfully to key")
            }
        }
    }
    
    func writeUserDataToFirebase(uid: String, data: Dictionary<String, Any>){   //기존에 키값이 있는 경우
        let ref = Database.database().reference().child("Users").child(uid)
        
        ref.updateChildValues(data) { error, _ in
            if let error = error{
                print("Error uploading data: \(error.localizedDescription)")
            } else {
                print("Data uploaded successfully to key")
            }
        }
    }
    
    func writeRankDataToFirebase(uid:String, data: Int){
        let ref = Database.database().reference()
        ref.child("Rank").child(uid).setValue(data){ error, _ in
            if let error = error{
                print("Error uploading data: \(error.localizedDescription)")
            } else {
                print("Data uploaded successfully to key")
            }
        }
    }
    func writeStudyTimeDataToFirebase(uid:String, data: Dictionary<String, dailyTimerData>){
        let ref = Database.database().reference()
        let compactMappedDictionary = data.mapValues { dailyTimerData in
            return [
                "totalStudyTime": dailyTimerData.totalStudyTime,
                "timerStudyTime": dailyTimerData.timerStudyTime,
                "stopwatchStudyTime": dailyTimerData.stopwatchStudyTime
            ]
        }
        
        ref.child("StudyDataes").child(uid).setValue(compactMappedDictionary){ error, _ in
            if let error = error{
                print("Error uploading data: \(error.localizedDescription)")
            } else {
                print("Data uploaded successfully to key")
            }
        }
    }
}

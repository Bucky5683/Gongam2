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
import FirebaseStorage

class FirebassDataManager: ObservableObject{
    static let shared = FirebassDataManager()
    private init() {}
    
    let database = Database.database().reference()
    
    private let encoder = JSONEncoder() // (2)
    private let decoder = JSONDecoder() // (2)
    private var storage = Storage.storage()
}

extension FirebassDataManager {
    // MARK: - Auth Info
    func deleteAuth(completion: @escaping (Error?) -> Void){
        let user = Auth.auth().currentUser
        user?.delete() { error in
            completion(error)
        }
    }
    
    // MARK: - Users CRUD
    func saveUserInfo(uid: String, userInfo: UserDataManager) {
        let userRef = database.child("Users").child(uid)
        userRef.setValue(userInfo.dictionaryRepresentation(.UserInfo))
    }
    
    func getUserInfo(uid: String, completion: @escaping (UserInfo?) -> Void) {
        let userRef = database.child("Users").child(uid)
        userRef.observeSingleEvent(of: .value, with: { snapshot  in
            guard let userData = snapshot.value as? [String: Any] else {
                completion(nil)
                return
            }
            
            do {
                let userinfo = try JSONSerialization.data(withJSONObject: userData)
                let user = try self.decoder.decode(UserInfo.self, from: userinfo)
                completion(user)
            } catch {
                print("an error occurred", error)
            }
        })
    }
    
    func deleteUserInfo(uid: String, completion: @escaping (Error?) -> Void) {
        let userRef = database.child("Users").child(uid)
        userRef.removeValue(completionBlock: { error, _ in
            completion(error)
        })
    }
    
    // MARK: - StudyDataes CRUD
    func saveStudyData(uid: String, date: String, studyData: UserDataManager) {
        let studyDataRef = database.child("StudyDataes").child(uid).child(date)
        studyDataRef.setValue(studyData.dictionaryRepresentation(.StudyDataes))
    }
    
    func getStudyData(uid: String, completion: @escaping (RecordUserStudy?) -> Void) {
        let studyDataRef = database.child("StudyDataes").child(uid)
        studyDataRef.observeSingleEvent(of: .value, with: { snapshot in
            guard let dataDict = snapshot.value as? [String:[String:Int]] else {
                print("Failed to cast snapshot value to [String: [String: Int]]")
                completion(nil)
                return
            }
            var userStudyData = RecordUserStudy()
            userStudyData.studyDataes = dataDict.mapValues { dailyData in
                return dailyTimerData(
                    timerStudyTima: dailyData["timerStudyTime"] ?? 0,
                    stopwatchStudyTime: dailyData["stopwatchStudyTime"] ?? 0
                )
            }
            completion(userStudyData)
        })
    }
    
    func deleteStudyData(uid: String, completion: @escaping (Error?) -> Void) {
        let studyDataRef = database.child("StudyDataes").child(uid)
        studyDataRef.removeValue(completionBlock: { error, _ in
            completion(error)
        })
    }
    
    // MARK: - Rank CRUD
    func saveRank(uid: String, rank: UserDataManager) {
        let rankRef = database.child("Rank").child(uid)
        rankRef.setValue(rank.dictionaryRepresentation(.Rank))
    }
    
    func getRank(uid: String, completion: @escaping (RankRecord?) -> Void) {
        let rankRef = database.child("Rank")
        rankRef.observeSingleEvent(of: .value,  with: { snapshot in
            guard let rankDict = snapshot.value as? [String:[String:Any]] else {
                completion(nil)
                return
            }
            var rank = RankRecord()
            var findMyData: Bool = false
            var myRank = 1
            var sum = 0
            var idx = 0
            var top5User: [String : [String:Any]] = [:]
            let sortDataDict = rankDict.sorted(by: {
              ($0.value["totalStudyTime"] as? Int ?? 0) > ($1.value["totalStudyTime"] as? Int ?? 0)
            })
            for (key, value) in sortDataDict {
              print("Rank Data value: \(value)")
            
              sum += value["totalStudyTime"] as! Int
            
              if key == uid {
                  rank.totalStudyTime = value["totalStudyTime"] as! Int
                  findMyData = true
              }
              if findMyData == false {
                  myRank += 1
              }
              if idx < 6 {
                  top5User[key] = value
              }
              if findMyData == true && idx >= 6 {
                  break
              }
              idx += 1
            }
            let average = sum/rankDict.count
            
            rank.myRank = myRank
            rank.averageTime = average
            rank.top5User = top5User
            completion(rank)
        })
    }
    
    func deleteRankData(uid: String, completion: @escaping (Error?) -> Void) {
        let rankRef = database.child("Rank").child(uid)
        rankRef.removeValue(completionBlock: { error, _ in
            completion(error)
        })
    }
}

extension FirebassDataManager {
    func writeImage(uid: String, image: UIImage, completion: @escaping (String?, Error?) -> Void) {
        let imagesRef = storage.reference().child("images/\(uid)")
        let data = image.jpegData(compressionQuality: 0.1)
        let metadata = StorageMetadata()
        metadata.contentType = "image/jpeg"
            
        imagesRef.putData(data!, metadata: metadata) { (metadata, error) in
            if let error = error {
                print("IMAGE WRITE ERROR : \(error)")
                completion(nil, error)
                return
            }
                
            imagesRef.downloadURL { (url, error) in
                if let error = error {
                    print("ERROR! : CAN'T GET URL \(error)")
                    completion(nil, error)
                } else if let downloadURL = url {
                    let imageURL = downloadURL.absoluteString
                    completion(imageURL, nil)
                }
            }
        }
    }
    
    func deleteImage(uid: String, completion: @escaping (Error?) -> Void) {
        let imagesRef = storage.reference().child("images/\(uid)")
        imagesRef.delete { error in
            if let error = error {
                print("Error removing image from storage\n\(error.localizedDescription)")
                completion(error)
            } else {
                print("IMAGE SUCCESS DELETE")
                completion(nil)
            }
        }
    }
}

//extension FirebassDataManager {
//    func newFetchUserData(completion: @escaping (UserData?) -> Void) {
//        guard let currentUser = Auth.auth().currentUser else {
//            completion(nil)
//            return
//        }
//        
//        let userData = UserData()
//        userData.id = currentUser.uid
//        userData.name = currentUser.displayName ?? ""
//        userData.profileImageURL = currentUser.photoURL?.absoluteString ?? "https://firebasestorage.googleapis.com/v0/b/gongam2-ff081.appspot.com/o/example2.jpeg?alt=media&token=2b4bbe1f-9ba2-49a7-bf54-87b5ca70eddd"
//        userData.email = currentUser.email ?? ""
//
//        print("userData.id = \(userData.id)")
//        completion(userData)
//    }
//    
//    func newFetchUserTimeData(completion: @escaping (UserTimeData?) -> Void) {
//        guard let currentUser = Auth.auth().currentUser else {
//            completion(nil)
//            return
//        }
//        let userStudyData = UserTimeData()
//        userStudyData.id = currentUser.uid
//        userStudyData.name = currentUser.displayName ?? ""
//        userStudyData.profileURL = currentUser.photoURL?.absoluteString ?? "https://firebasestorage.googleapis.com/v0/b/gongam2-ff081.appspot.com/o/example2.jpeg?alt=media&token=2b4bbe1f-9ba2-49a7-bf54-87b5ca70eddd"
//        userStudyData.email = currentUser.email ?? ""
//
//        print("userStudyData.id = \(userStudyData.id)")
//        completion(userStudyData)
//    }
//}
//
//extension FirebassDataManager {
//    //MARK: Firebase에 읽기
//    func readUserTimeDataFromFirebase(completion: @escaping (UserTimeData?) -> Void) {
//        guard let currentUser = Auth.auth().currentUser else {
//            completion(nil)
//            return
//        }
//        
//        let uid = currentUser.uid
//        let ref = database.child("StudyDataes").child(uid)
//        
//        ref.getData{ (error, snapshot) in
//            if let error = error {
//                print("Error getting data: \(error.localizedDescription)")
//                return
//            }
//            
//            guard let dataDict = snapshot?.value as? [String:[String:Int]] else {
//                print("Failed to cast snapshot value to [String: [String: Int]]")
//                completion(nil)
//                return
//            }
//            let userStudyData = UserTimeData()
//            userStudyData.id = uid
//            userStudyData.studyDataes = dataDict.mapValues { dailyData in
//                return dailyTimerData(
//                    timerStudyTima: dailyData["timerStudyTime"] ?? 0,
//                    stopwatchStudyTime: dailyData["stopwatchStudyTime"] ?? 0
//                )
//            }
//            completion(userStudyData)
//        }
//    }
//    
//    func readUserDataFromFirebase(completion: @escaping (UserData?) -> Void) {
//        guard let currentUser = Auth.auth().currentUser else {
//            completion(nil)
//            return
//        }
//        
//        let uid = currentUser.uid
//        let ref = database.child("Users").child(uid)
//        // 'your_node'에 있는 데이터 읽어오기
//        ref.getData(completion:  { error, snapshot in
//            guard error == nil else {
//              print(error!.localizedDescription)
//              return
//            }
//            if let dataDict = snapshot?.value as? [String: Any] {
//                do {
//                    // Firebase 데이터를 UserData 모델로 변환
//                    let jsonData = try JSONSerialization.data(withJSONObject: dataDict)
//                    let decoder = JSONDecoder()
//                    let decodedUserData = try decoder.decode(UserData.self, from: jsonData)
//                    decodedUserData.id = uid
//                    completion(decodedUserData)
//                } catch {
//                    print("Error decoding data: \(error.localizedDescription)")
//                }
//            } else {
//                completion(nil)
//                return
//            }
//        })
//    }
//    
//    func readRankDataFromFirebase(completion: @escaping (UserTimeData?) -> Void){
//        guard let currentUser = Auth.auth().currentUser else {
//            completion(nil)
//            return
//        }
//        
//        let allMembersTimeDataQuery = database.child("Rank").queryOrdered(byChild: "totalStudyTime")
//        allMembersTimeDataQuery.getData { (error, snapshot) in
//            if let error = error {
//                print("Error getting data: \(error.localizedDescription)")
//                completion(nil)
//                return
//            }
//            
//            guard let dataDict = snapshot?.value as? [String:[String:Any]] else {
//                print("Failed to cast snapshot value to [String: [String: Any]]")
//                completion(nil)
//                return
//            }
//            
//            let userStudyData = UserTimeData()
//            var findMyData: Bool = false
//            var myRank = 1
//            var sum = 0
//            var idx = 0
//            var top5User: [String : [String:Any]] = [:]
//            let sortDataDict = dataDict.sorted(by: {
//                ($0.value["totalStudyTime"] as? Int ?? 0) > ($1.value["totalStudyTime"] as? Int ?? 0)
//            })
//            for (key, value) in sortDataDict {
//                print("Rank Data value: \(value)")
//                
//                sum += value["totalStudyTime"] as! Int
//                
//                if key == currentUser.uid {
//                    userStudyData.name = value["name"] as! String
//                    userStudyData.email = value["email"] as! String
//                    userStudyData.profileURL = value["profileURL"] as? String ?? "https://firebasestorage.googleapis.com/v0/b/gongam2-ff081.appspot.com/o/example2.jpeg?alt=media&token=2b4bbe1f-9ba2-49a7-bf54-87b5ca70eddd"
//                    userStudyData.totalStudyTime = value["totalStudyTime"] as! Int
//                    findMyData = true
//                }
//                if findMyData == false {
//                    myRank += 1
//                }
//                if idx < 6 {
//                    top5User[key] = value
//                }
//                if findMyData == true && idx >= 6 {
//                    break
//                }
//                idx += 1
//            }
//            let average = sum/dataDict.count
//            
//            userStudyData.myRank = myRank
//            userStudyData.averageTime = average
//            userStudyData.top10User = top5User
//            completion(userStudyData)
//        }
//    }
//
//}
//
//extension FirebassDataManager {
//    //MARK: Firebase에 쓰기
//    
//    func writeUserStudyDataToFirebase(uid:String, date: String, data: Dictionary<String,Any>){
//        let ref = Database.database().reference().child("StudyDataes").child(uid).child(date)
//        
//        ref.updateChildValues(data) { error, _ in
//            if let error = error{
//                print("Error uploading data: \(error.localizedDescription)")
//            } else {
//                print("Data uploaded successfully to key")
//            }
//        }
//    }
//    
//    func writeNewUserDataToFirebase(uid: String, data: Dictionary<String, Any>){
//        // 신규 유저 데이터 생성
//        let ref = database.child("Users").child(uid)
//        
//        ref.updateChildValues(data) { error, _ in
//            if let error = error{
//                print("Error uploading data: \(error.localizedDescription)")
//            } else {
//                print("Data uploaded successfully to key")
//            }
//        }
//    }
//    
//    func writeUserDataToFirebase(uid: String, data: Dictionary<String, Any>){
//        // 기존 유저 데이터 생성
//        let ref = database.child("Users").child(uid)
//        
//        ref.updateChildValues(data) { error, _ in
//            if let error = error{
//                print("Error uploading data: \(error.localizedDescription)")
//            } else {
//                print("Data uploaded successfully to key")
//            }
//        }
//    }
//    
//    func writeUserDataPartlyToFirebase(uid: String, type: userDataType, data: Any){
//        let ref = database.child("Users").child(uid)
//        switch(type){
//        case .profileImageURL:
//            ref.child("profileImageURL").setValue(data)
//        case .name:
//            ref.child("name").setValue(data)
//        case .todayStudyTime:
//            ref.child("todayStudyTime").setValue(data)
//        case .goalStudyTime:
//            ref.child("goalStudyTime").setValue(data)
//        case .stopwatchStudyTime:
//            ref.child("stopwatchStudyTime").setValue(data)
//        case .timerStudyTime:
//            ref.child("timerStudyTime").setValue(data)
//        case .lastUpdateDate:
//            ref.child("lastUpdateDate").setValue(data)
//        case .email:
//            ref.child("email").setValue(data)
//        }
//    }
//    
//    func writeRankDataToFirebase(uid:String, data: [String:Any]){
//        // Rank 데이터 쓰기
//        let ref = database.child("Rank").child(uid)
//        ref.setValue(data){ error, _ in
//            if let error = error{
//                print("Error uploading data: \(error.localizedDescription)")
//            } else {
//                print("Data uploaded successfully to key")
//            }
//        }
//    }
//    func writeStudyTimeDataToFirebase(uid:String, data: Dictionary<String, dailyTimerData>){
//        // 유저 공부 데이터 쓰기
//        let ref = database.child("StudyDataes").child(uid)
//        let compactMappedDictionary = data.mapValues { dailyTimerData in
//            return [
//                "totalStudyTime": dailyTimerData.totalStudyTime,
//                "timerStudyTime": dailyTimerData.timerStudyTime,
//                "stopwatchStudyTime": dailyTimerData.stopwatchStudyTime
//            ]
//        }
//        
//        ref.setValue(compactMappedDictionary){ error, _ in
//            if let error = error{
//                print("Error uploading data: \(error.localizedDescription)")
//            } else {
//                print("Data uploaded successfully to key")
//            }
//        }
//    }
//    //MARK: 사용자 관리
//    func deleteData(uid: String) {
//        let user = Auth.auth().currentUser
//        self.database.child("StudyDataes").child(uid).removeValue()
//        self.database.child("Users").child(uid).removeValue()
//        self.database.child("Rank").child(uid).removeValue()
//        
//        user?.delete() { error in
//            if let error = error {
//                // An error happened.
//                print("ERROR USER DELETE : \(error)")
//            } else {
//                // Account deleted.
//                print("Account deleted Success")
//            }
//        }
//    }
//}

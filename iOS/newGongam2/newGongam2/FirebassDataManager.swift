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

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
    
    var userData = UserData()
    
    func newFetchUserData(completion: @escaping (UserData?) -> Void) {
        guard let currentUser = Auth.auth().currentUser else {
            completion(nil)
            return
        }
        userData.id = currentUser.uid
        userData.name = currentUser.displayName ?? ""
        userData.profileImageURL = currentUser.photoURL?.absoluteString ?? ""

        completion(userData)
    }
    
    func readUserDataFromFirebase(uid: String,completion: @escaping (UserData?) -> Void) {
        guard let currentUser = Auth.auth().currentUser else {
            completion(nil)
            return
        }
        let ref = Database.database().reference()
        let uid = currentUser.uid
        
        // 'your_node'에 있는 데이터 읽어오기
        ref.child("Users").child("\(uid)").getData(completion:  { error, snapshot in
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
                    completion(decodedUserData)
                } catch {
                    print("Error decoding data: \(error.localizedDescription)")
                }
            } else {
                completion(nil)
            }
        })
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
        let ref = Database.database().reference()
        guard let userKey = ref.child("Users").child(uid).childByAutoId().key else { return }
        let childUpdate = ["/Users/\(uid)/\(userKey)" : data]
        ref.updateChildValues(childUpdate)
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
}

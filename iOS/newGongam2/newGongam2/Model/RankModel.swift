//
//  RankModel.swift
//  newGongam2
//
//  Created by 김서연 on 1/12/24.
//

import Foundation

struct RankerUser : Hashable{
    let id = UUID()
    var name: String
    var totalStudyTime: Int
    var profileURL: String
    var rank: Int = 0
    
    init(name: String, totalStudyTime: Int, profileURL: String) {
        self.name = name
        self.totalStudyTime = totalStudyTime
        self.profileURL = profileURL
    }
    init() {
        self.name = ""
        self.totalStudyTime = -1
        self.profileURL = ""
        self.rank = 99999999
    }
}

//
//  RankView.swift
//  newGongam2
//
//  Created by 김서연 on 1/10/24.
//

import SwiftUI

struct RankView: View {
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @EnvironmentObject var userTimeData: UserTimeData
    @State var top5UsersArray: [RankerUser] = []
    
    var body: some View {
        NavigationView{
            VStack{
                VStack{
                    HStack{
                        Text("나의 등수")
                        Text("\(self.userTimeData.myRank)")
                    }
                    HStack{
                        Text("이번주 공부시간")
                        Text("\(self.userTimeData.totalStudyTime.timeToText())")
                    }
                }
                Text("이번 주 Top 5")
                if self.top5UsersArray.count > 0{
                    RankerView(rankerProfile: self.top5UsersArray[0])
                    RankerView(rankerProfile: self.top5UsersArray[1])
                    RankerView(rankerProfile: self.top5UsersArray[2])
                    RankerView(rankerProfile: self.top5UsersArray[3])
                    RankerView(rankerProfile: self.top5UsersArray[4])
                }
            }.onAppear(){
                self.loadData()
            }
        }.navigationBarHidden(true)
    }
    
    private func loadData(){
        self.userTimeData.downloadRankData()
        self.makeTop5Users(rankers: userTimeData.top10User)
        print(self.top5UsersArray)
    }
    
    private func makeTop5Users(rankers: [String:[String:Any]]){
        var top5: [RankerUser] = []
        for (_, value) in rankers {
            top5.append(RankerUser(name: value["name"] as! String, totalStudyTime: value["totalStudyTime"] as! Int, profileURL: value["profileImageURL"] as! String))
        }
        if top5.count < 5 {
            for _ in 1...(5 - top5.count) {
                top5.append(RankerUser())
            }
        }
        top5.sort{ $0.totalStudyTime > $1.totalStudyTime }
        
        for index in top5.indices {
            top5[index].rank = index + 1
            if top5[index].name == "" {
                top5[index].rank = 99999
            }
        }
        
        if self.top5UsersArray.elementsEqual(top5){
            return
        } else {
            self.top5UsersArray = top5
        }
    }
}

struct RankerView: View {
    @State var rankerProfile: RankerUser
    
    var body: some View {
        HStack{
            switch(rankerProfile.rank){
            case 1:
                AsyncImage(url: URL(string: rankerProfile.profileURL)){ image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }
                VStack{
                    Text(rankerProfile.name)
                    Text(rankerProfile.totalStudyTime.timeToText())
                }
                Spacer()
                Text("1st")
            case 2:
                AsyncImage(url: URL(string: rankerProfile.profileURL)){ image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }
                VStack{
                    Text(rankerProfile.name)
                    Text(rankerProfile.totalStudyTime.timeToText())
                }
                Spacer()
                Text("2nd")
            case 3:
                AsyncImage(url: URL(string: rankerProfile.profileURL)){ image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }
                VStack{
                    Text(rankerProfile.name)
                    Text(rankerProfile.totalStudyTime.timeToText())
                }
                Spacer()
                Text("3rd")
            case 4:
                AsyncImage(url: URL(string: rankerProfile.profileURL)){ image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }
                Text(rankerProfile.name)
                Text(rankerProfile.totalStudyTime.timeToText())
                Spacer()
                Text("4th")
            case 5:
                AsyncImage(url: URL(string: rankerProfile.profileURL)){ image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }
                Text(rankerProfile.name)
                Text(rankerProfile.totalStudyTime.timeToText())
                Spacer()
                Text("5th")
            default:
                Spacer()
            }
            
        }
    }
}

#Preview {
    RankView()
}

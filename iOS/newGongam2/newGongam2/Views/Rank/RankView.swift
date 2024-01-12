//
//  RankView.swift
//  newGongam2
//
//  Created by 김서연 on 1/10/24.
//

import SwiftUI

class RankViewModel: ObservableObject {
    @Published var top5UsersArray: [RankerUser] = []
    
    func makeTop5Users(rankers: [String:[String:Any]]){
        for (_, value) in rankers {
            self.top5UsersArray.append(RankerUser(name: value["name"] as! String, totalStudyTime: value["totalStudyTime"] as! Int, profileURL: value["profileURL"] as? String ?? "https://firebasestorage.googleapis.com/v0/b/gongam2-ff081.appspot.com/o/example2.jpeg?alt=media&token=2b4bbe1f-9ba2-49a7-bf54-87b5ca70eddd"))
        }
        self.top5UsersArray.sort{ $0.totalStudyTime > $1.totalStudyTime }
        
        for index in top5UsersArray.indices {
            top5UsersArray[index].rank = index + 1
        }
    }
}

struct RankView: View {
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @EnvironmentObject var userTimeData: UserTimeData
    @ObservedObject var viewModel: RankViewModel = RankViewModel()
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
                        Text("\(self.userTimeData.totalStudyTime)")
                    }
                }
                Text("이번 주 Top 5")
                ForEach(self.viewModel.top5UsersArray, id: \.rank){ user in
                    RankerView(rankerProfile: user).padding()
                }
            }.onAppear(){
                self.userTimeData.downloadRankData()
                self.viewModel.makeTop5Users(rankers: userTimeData.top10User)
                print(self.viewModel.top5UsersArray)
            }
        }.navigationBarHidden(true)
    }
}

struct RankerView: View {
    var rankerProfile: RankerUser
    
    var body: some View {
        HStack{
            switch(rankerProfile.rank){
            case 1:
                AsyncImage(url: URL(string: rankerProfile.profileURL))
                VStack{
                    Text(rankerProfile.name)
                    Text(rankerProfile.totalStudyTime.timeToText())
                }
                Spacer()
                Text("1st")
            case 2:
                AsyncImage(url: URL(string: rankerProfile.profileURL))
                VStack{
                    Text(rankerProfile.name)
                    Text(rankerProfile.totalStudyTime.timeToText())
                }
                Spacer()
                Text("2nd")
            case 3:
                AsyncImage(url: URL(string: rankerProfile.profileURL))
                VStack{
                    Text(rankerProfile.name)
                    Text(rankerProfile.totalStudyTime.timeToText())
                }
                Spacer()
                Text("3rd")
            case 4:
                AsyncImage(url: URL(string: rankerProfile.profileURL))
                Text(rankerProfile.name)
                Text(rankerProfile.totalStudyTime.timeToText())
                Spacer()
                Text("4th")
            case 5:
                AsyncImage(url: URL(string: rankerProfile.profileURL))
                Text(rankerProfile.name)
                Text(rankerProfile.totalStudyTime.timeToText())
                Spacer()
                Text("5th")
            default:
                Text("ERROR!")
            }
            
        }
    }
}

#Preview {
    RankView()
}

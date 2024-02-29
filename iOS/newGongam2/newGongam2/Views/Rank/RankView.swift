//
//  RankView.swift
//  newGongam2
//
//  Created by 김서연 on 1/10/24.
//

import SwiftUI

struct RankView: View {
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @EnvironmentObject var userDataManager: UserDataManager
    @State var top5UsersArray: [RankerUser] = []
    
    init() {
        //Use this if NavigationBarTitle is with Large Font
        UINavigationBar.appearance().largeTitleTextAttributes = [.foregroundColor: UIColor.black]

        //Use this if NavigationBarTitle is with displayMode = .inline
        UINavigationBar.appearance().titleTextAttributes = [.foregroundColor: UIColor.black]
    }
    
    var body: some View {
        VStack(spacing: 30){
            //Rectangle().foregroundColor(.white).frame(height: 88)
            VStack(spacing: 10){
                HStack(alignment: .bottom){
                    Text("나의 등수")
                        .font(Font.system(size: 17))
                        .foregroundColor(.white)
                    Spacer()
                    Text("\(self.userDataManager.rankRecord.myRank)")
                        .font(Font.system(size: 32).weight(.semibold))
                        .multilineTextAlignment(.trailing)
                        .foregroundColor(.white)
                }.padding(.leading, 19)
                    .padding(.top, 37)
                    .padding(.trailing,19)
                HStack{
                    Text("이번주 공부시간")
                        .font(Font.system(size: 14))
                        .foregroundColor(.white)
                    Spacer()
                    Text("\(self.userDataManager.rankRecord.totalStudyTime.timeToText())")
                        .font(Font.system(size: 14))
                        .multilineTextAlignment(.trailing)
                        .foregroundColor(.white)
                }.padding(.leading, 19)
                    .padding(.bottom, 38)
                    .padding(.trailing,19)
            }.background(.darkBlue414756)
            .frame(height: 133)
            .cornerRadius(10)
            .padding(.top, 30)
            .shadow(color: .black.opacity(0.25), radius: 2, x: 0, y: 4)
            
            HStack{
                Text("이번 주 Top 5").font(Font.system(size: 18).bold()).foregroundColor(.darkBlue414756)
                Spacer()
            }
            .padding(.leading, 15)
            .padding(.trailing, 15)
            if self.top5UsersArray.count > 0{
                VStack(spacing: 8){
                    RankerView(rankerProfile: self.top5UsersArray[0])
                    RankerView(rankerProfile: self.top5UsersArray[1])
                    RankerView(rankerProfile: self.top5UsersArray[2])
                    RankerView(rankerProfile: self.top5UsersArray[3])
                    RankerView(rankerProfile: self.top5UsersArray[4])
                }
                .padding(.leading, 15)
                .padding(.trailing, 15)
            }
            Spacer()
        }
        .padding(.leading, 22)
        .padding(.trailing, 22)
        .onAppear(){
            self.loadData()
            self.makeTop5Users(rankers: self.userDataManager.rankRecord.top5User)
            print(self.top5UsersArray)
        }.background(.white)
        .navigationBarTitle("랭킹",displayMode: .inline)
        .foregroundColor(.black)
        .navigationBarBackButtonHidden(true)
        .navigationBarItems(leading:
                                Button{
                                    self.coordinator.pop()
                                } label: {
                                    Text("Main")
                                        .foregroundColor(.black)
                                })
    }
    
    private func loadData(){
        self.userDataManager.writeRankData()
        self.userDataManager.readRankData()
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
        
        if self.top5UsersArray != top5 { // Check if top5UsersArray is different from top5
            self.top5UsersArray = top5
        }
    }
}

struct RankerView: View {
    @State var rankerProfile: RankerUser
    
    var body: some View {
        HStack(spacing: 10){
            switch(rankerProfile.rank){
            case 1:
                AsyncImage(url: URL(string: rankerProfile.profileURL)){ image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }.frame(width: 50, height: 50)
                    .cornerRadius(50)
                    .padding(.top, 22)
                    .padding(.bottom, 22)
                    .padding(.leading, 22)
                VStack(alignment: .leading){
                    Text(rankerProfile.name)
                        .font(Font.system(size: 15)).foregroundColor(.darkBlue414756)
                        .multilineTextAlignment(.leading)
                    Text(rankerProfile.totalStudyTime.timeToText())
                        .font(Font.system(size: 15)).foregroundColor(.darkBlue414756)
                        .multilineTextAlignment(.leading)
                }
                Spacer()
                Text("1st")
                    .font(Font.system(size: 20).bold())
                    .multilineTextAlignment(.trailing)
                    .foregroundColor(.darkBlue414756)
                    .padding(.trailing, 22)
            case 2:
                AsyncImage(url: URL(string: rankerProfile.profileURL)){ image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }.frame(width: 40, height: 40)
                    .cornerRadius(40)
                    .padding(.top, 13)
                    .padding(.bottom, 13)
                    .padding(.leading, 25)
                VStack(alignment: .leading){
                    Text(rankerProfile.name)
                        .font(Font.system(size: 15)).foregroundColor(.darkBlue414756)
                        .multilineTextAlignment(.leading)
                    Text(rankerProfile.totalStudyTime.timeToText())
                        .font(Font.system(size: 15)).foregroundColor(.darkBlue414756)
                        .multilineTextAlignment(.leading)
                }
                Spacer()
                Text("2nd")
                    .font(Font.system(size: 16).bold())
                    .multilineTextAlignment(.trailing)
                    .foregroundColor(.darkBlue414756)
                    .padding(.trailing, 23)
            case 3:
                AsyncImage(url: URL(string: rankerProfile.profileURL)){ image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }.frame(width: 40, height: 40)
                    .cornerRadius(40)
                    .padding(.top, 12)
                    .padding(.bottom, 12)
                    .padding(.leading, 25)
                VStack(alignment: .leading){
                    Text(rankerProfile.name)
                        .font(Font.system(size: 15))
                        .foregroundColor(.darkBlue414756)
                        .multilineTextAlignment(.leading)
                    Text(rankerProfile.totalStudyTime.timeToText())
                        .font(Font.system(size: 15))
                        .foregroundColor(.darkBlue414756)
                        .multilineTextAlignment(.leading)
                }
                Spacer()
                Text("3rd")
                    .font(Font.system(size: 16).bold())
                    .multilineTextAlignment(.trailing)
                    .foregroundColor(.darkBlue414756)
                    .padding(.trailing, 23)
            case 4:
                AsyncImage(url: URL(string: rankerProfile.profileURL)){ image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }.frame(width: 30, height: 30)
                    .cornerRadius(40)
                    .padding(.top, 10)
                    .padding(.bottom, 10)
                    .padding(.leading, 27)
                Text(rankerProfile.name)
                    .font(Font.system(size: 15)).foregroundColor(.darkBlue414756)
                    .multilineTextAlignment(.leading)
                Text(rankerProfile.totalStudyTime.timeToText())
                    .font(Font.system(size: 15)).foregroundColor(.darkBlue414756)
                    .multilineTextAlignment(.leading)
                Spacer()
                Text("4th")
                    .font(Font.system(size: 15).bold())
                    .multilineTextAlignment(.trailing)
                    .foregroundColor(.darkBlue414756)
                    .padding(.trailing, 26)
            case 5:
                AsyncImage(url: URL(string: rankerProfile.profileURL)){ image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }.frame(width: 30, height: 30)
                    .cornerRadius(40)
                    .padding(.top, 10)
                    .padding(.bottom, 10)
                    .padding(.leading, 27)
                Text(rankerProfile.name)
                    .font(Font.system(size: 15)).foregroundColor(.darkBlue414756)
                    .multilineTextAlignment(.leading)
                Text(rankerProfile.totalStudyTime.timeToText())
                    .font(Font.system(size: 15)).foregroundColor(.darkBlue414756)
                    .multilineTextAlignment(.leading)
                Spacer()
                Text("5th")
                    .font(Font.system(size: 15).bold())
                    .multilineTextAlignment(.trailing)
                    .foregroundColor(.darkBlue414756)
                    .padding(.trailing, 26)
            default:
                Spacer()
            }
            
        }.background(.whiteFFFFFF)
            .cornerRadius(10)
            .shadow(color: .black.opacity(0.25), radius: 2, x: 0, y: 4)
    }
}

#Preview {
    RankView()
}

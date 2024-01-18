//
//  MainView.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import SwiftUI
import PopupView

// MARK: 메인화면 뷰모델
/*
 메인화면 기능정리
 1. 메인 헤더
    - 목표시간에 현재 공부한 시간을 빼, 남은 시간이 있는지 확인하고 있다면, 그 시간을 출력하기 ✅
 2. 랭킹
    - 유저의 평균시간 데이터베이스에서 받아오기 ✅
    - 평균시간에서 현재 공부한 시간을 뺴, 남은 시간을 출력 ✅
    - 평균시간보다 유저가 공부한 시간이 적으면 평균공부시간보다 낮게, 공부한 시간이 많으면 평균 공부시간보다 높게 View 설정 ✅
    - 유저의 등수가 어느정도인지, 데이터베이스에서 받아오기(만약 1000등 이상이면 999+로 출력)✅
 3. 마이 리포트
    - 유저의 한주 데이터를 받아옴✅
        - 만약 수요일(1/11)이라고 해도, 그 주의 일요일(1/7)부터 토요일(1/13)까지의 데이터 불러옴✅
        - 목요일,금요일, 토요일 데이터가 없으면 회색으로 "-" 처리✅
 4. 프로필
    - 유저의 프로필 사진 출력 ✅
    - 터치 시, 프로필 Popup이 뜸 ✅
    - 유저의 이름, 이메일, 목표공부시간 출력 ✅
    - Popup의 프로필 사진 터치 시, 프로필 수정 화면으로 이동 ✅
    - 도움말, 이동약관은 Web브라우저로 이동하도록 설정
 */
class MainViewModel: ObservableObject {
    @Published var thisWeekDataes : [String:Int] = [:]
    @Published var thisWeeks: [MyReportGridItem] = []
    @Published var average: Int = 0
    
    //메인 헤더
    func calculateReMainTime(goalTime: Int, todayStudyTime: Int) -> Int{ //목표시간에서 현재 공부한 시간을 빼, 0보다 크면 남은 시간을, 아니면 -1을 반환
        let remainTime = goalTime - todayStudyTime
        if remainTime > 0{
            return remainTime
        } else {
            return -1
        }
    }
    
    func makeWeeklyChartReport(userTimeData: UserTimeData, userData: UserData){
        let data = userTimeData.studyDataes
        let todayDate = Date().getCurrentDateAsString()
        let thisWeek = Date().getWeeksAgoSunday(week: 1)
        var sum = 0
        for (_, value) in userTimeData.divideDataByWeeks(startDate: thisWeek?[0].getCurrentDateAsString() ?? Date().getCurrentDateAsString(), endDate: todayDate, data: data){
            for (dKey, dValue) in value {
                self.thisWeekDataes[dKey] = dValue.totalStudyTime - userData.goalStudyTime
                sum += dValue.totalStudyTime
            }
        }
        var arrayReport: [MyReportGridItem] = []
        
        for (key, value) in self.thisWeekDataes {
            arrayReport.append(MyReportGridItem(weeklys: key.getDaysFromString(), date: key, studyTime: value))
        }
        arrayReport.sort(by: {$0.date < $1.date})
        self.average = Int(sum/7)
        self.thisWeeks = arrayReport
    }
}

// MARK: 메인화면 뷰
struct MainView: View {
    @EnvironmentObject var userData: UserData
    @EnvironmentObject var userTimeData: UserTimeData
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @State var viewModel: MainViewModel = MainViewModel()
    @State var showingPopup = false

    init() {
        UINavigationBar.appearance().titleTextAttributes = [.foregroundColor: UIColor.whiteFFFFFF]
        UINavigationBar.appearance().largeTitleTextAttributes = [.foregroundColor: UIColor.whiteFFFFFF]
    }
    
    var body: some View {
        GeometryReader { geometry in
            ZStack{
                HStack{
                    ScrollView{
                        VStack{
                            MainHeaderView(viewModel: $viewModel)
                                .background(Color.darkBlue414756)
                                .frame(width: geometry.size.width, height: 280)
                                .padding(.bottom, 15)
                            //MARK: 타이머
                            VStack(spacing: 15) {
                                HStack{
                                    Text("타이머")
                                        .font(Font.system(size: 18))
                                        .bold()
                                        .underline(true, color: .darkBlue414756)
                                        .foregroundColor(.darkBlue414756)
                                        .padding(.bottom, 8)
                                    Spacer()
                                }
                                TimersButtonView(isTimer: true)
                                TimersButtonView(isTimer: false)
                                    .padding(.bottom, 35)
                            }
                            .padding(.leading, 40)
                            .padding(.trailing, 40)
                            .padding(.bottom, 15)
                            
                            //MARK: 랭크
                            VStack(spacing: 15){
                                MainSubHeaderView(isRank: true)
                                    .padding(.leading, 40)
                                    .padding(.trailing, 40)
                                    .padding(.bottom, 15)
                                Button {
                                    coordinator.push(.rank)
                                } label: {
                                    MainRankChartView()
                                }
                                if (userTimeData.averageTime - userTimeData.totalStudyTime) < 0 {
                                    HStack(alignment: .bottom){
                                        Text("평균보다")
                                            .font(Font.system(size: 12).weight(.medium))
                                            .foregroundColor(.darkBlue414756)
                                        Text((userTimeData.totalStudyTime - userTimeData.averageTime).timeToText())
                                            .font(Font.system(size: 15))
                                            .bold()
                                            .foregroundColor(.blue5C84FF)
                                        Text("만큼 덜 공부했어요!")
                                            .font(Font.system(size: 15).weight(.medium))
                                            .foregroundColor(.darkBlue414756)
                                    }.padding(.bottom, 30)
                                        .padding(.leading, 40)
                                        .padding(.trailing, 40)
                                } else if (userTimeData.averageTime - userTimeData.totalStudyTime) > 0 {
                                    HStack(alignment: .bottom){
                                        Text("평균보다")
                                            .font(Font.system(size: 12).weight(.medium))
                                            .foregroundColor(.darkBlue414756)
                                        Text((userTimeData.averageTime - userTimeData.totalStudyTime).timeToText())
                                            .font(Font.system(size: 15))
                                            .bold()
                                            .foregroundColor(.redFF0000)
                                        Text("만큼 더 공부했어요!")
                                            .font(Font.system(size: 12).weight(.medium))
                                            .foregroundColor(.darkBlue414756)
                                    }
                                    .padding(.bottom, 30)
                                    .padding(.leading, 40)
                                    .padding(.trailing, 40)
                                } else {
                                    Text("평균만큼 공부했어요")
                                        .font(Font.system(size: 12).weight(.medium))
                                        .foregroundColor(.darkBlue414756)
                                        .padding(.bottom, 30)
                                        .padding(.leading, 40)
                                        .padding(.trailing, 40)
                                }
                            }
                            .padding(.bottom, 15)
                            
                            //MARK: 마이리포트
                            VStack(spacing: 30){
                                MainSubHeaderView(isRank: false)
                                Button {
                                    coordinator.push(.myReport)
                                } label: {
                                    MainMyReportGridView(viewModel: $viewModel)
                                }.padding(.bottom, 20)
                            }.padding(.leading, 40)
                                .padding(.trailing, 40)
                                .padding(.bottom, 15)
                        }
                    }
                }
            }
            .background(.whiteFFFFFF)
            .popup(isPresented: $showingPopup){
                VStack(spacing: 20){
                    HStack(alignment: .bottom, spacing: 12){
                        VStack(alignment: .leading, spacing: 5){
                            Text(userData.name)
                                .font(Font.system(size: 15).weight(.bold))
                                .foregroundColor(.black)
                            Text(userData.email)
                                .lineLimit(1)
                                .truncationMode(.tail)
                                .font(Font.system(size: 12).weight(.medium))
                                .foregroundColor(.black)
                            HStack{
                                Text("목표 공부시간")
                                    .font(Font.system(size: 12).weight(.medium))
                                    .underline(true, color: .black)
                                    .foregroundColor(.black)
                                Text(userData.goalStudyTime.timeToText())
                                    .font(Font.system(size: 12).weight(.medium))
                                    .foregroundColor(.black)
                            }
                        }
                        .padding(.leading, 15)
                        Button {
                            coordinator.isProfileEdit = true
                            coordinator.push(.setProfile)
                        } label: {
                            AsyncImage(url: URL(string: userData.profileImageURL)){ image in
                                image.resizable()
                            } placeholder: {
                                ProgressView()
                            }.frame(width: 80, height: 80)
                                .cornerRadius(80)
                                .padding(.leading, 15)
                                .padding(.trailing, 15)
                                .padding(.top, 15)
                        }
                    }
                    VStack(spacing: 8){
                        Button {
                            print("Clicked Helper!!")
                        } label: {
                            HStack(spacing: 7){
                                Image("HelperImage")
                                    .resizable()
                                    .frame(width: 30, height: 30)
                                    .padding(.leading, 12)
                                Text("도움말")
                                    .font(Font.system(size: 15).weight(.medium))
                                    .foregroundColor(.black)
                                Spacer()
                            }
                            .frame(height: 44)
                            .background(.white)
                        }
                        .cornerRadius(10)
                            .shadow(color: .black.opacity(0.25), radius: 2, x: 0, y: 4)
                            .padding(.leading, 15)
                            .padding(.trailing, 15)
                        Button {
                            print("Clicked 이용약관!!")
                        } label: {
                            HStack(spacing: 7){
                                Image("HelperImage")
                                    .resizable()
                                    .frame(width: 30, height: 30)
                                    .padding(.leading, 12)
                                Text("이용약관")
                                    .font(Font.system(size: 15).weight(.medium))
                                    .foregroundColor(.black)
                                Spacer()
                            }
                            .frame(height: 44)
                                .background(.white)
                        }.cornerRadius(10)
                            .shadow(color: .black.opacity(0.25), radius: 2, x: 0, y: 4)
                            .padding(.leading, 15)
                            .padding(.trailing, 15)
                    }
                    .padding(.bottom, 15)
                }
                .background(.whiteFFFFFF)
                .frame(width: 270)
                    .cornerRadius(10)
                
            } customize: {
                $0
                    .type(.floater())
                    .position(.topTrailing)
                    .animation(.snappy)
                    .closeOnTapOutside(true)
                    .backgroundColor(.black.opacity(0.5))
                    .isOpaque(true)
            }.onChange(of: coordinator.isProfileEdit){
                showingPopup = !coordinator.isProfileEdit
            }
            .navigationBarTitle("",displayMode: .inline)
            .navigationBarBackButtonHidden(true)
            .navigationBarItems(leading: Text(""), trailing: Button{
                print("MainHeader Profile Image Clicked!")
                showingPopup = true
            }label: {
                AsyncImage(url: URL(string: userData.profileImageURL)){ image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }.frame(width: 30, height: 30)
                    .cornerRadius(30)
            })
        }.edgesIgnoringSafeArea(.top)
    }
}

struct TimersButtonView: View {
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    var isTimer: Bool
    var body: some View {
        HStack{
            if isTimer{
                Text("⏰")
                    .padding(.leading, 20)
                Text("타이머")
                    .font(Font.system(size: 15))
                    .fontWeight(.regular)
                    .foregroundColor(.darkBlue414756)
                    .multilineTextAlignment(.leading)
                    .frame(width: 60)
                    .padding(.leading, 10)
                Spacer()
                Button{
                    coordinator.push(.timer)
                } label: {
                    HStack{
                        Text("GO")
                            .font(Font.system(size: 15))
                            .fontWeight(.regular)
                            .foregroundColor(.whiteFFFFFF)
                        Image("goButtonIcon")
                            .resizable()
                            .frame(width: 15, height: 15)
                    }
                }.frame(width: 80, height: 48)
                .background(.darkBlue414756)
            } else {
                Text("⏱️")
                    .padding(.leading, 20)
                Text("스톱워치")
                    .font(Font.system(size: 15))
                    .fontWeight(.regular)
                    .foregroundColor(.darkBlue414756)
                    .multilineTextAlignment(.leading)
                    .frame(width: 60)
                    .padding(.leading, 10)
                Spacer()
                Button{
                    coordinator.push(.stopwatch)
                } label: {
                    HStack{
                        Text("GO")
                            .font(Font.system(size: 15))
                            .fontWeight(.regular)
                            .foregroundColor(.whiteFFFFFF)
                        Image("goButtonIcon")
                            .resizable()
                            .frame(width: 15, height: 15)
                    }
                }.frame(width: 80, height: 48)
                    .background(.darkBlue414756)
            }
        }.background(.whiteFFFFFF)
            .cornerRadius(10)
        .shadow(color: .black.opacity(0.1), radius: 5, x: 0, y: 4)
    }
}

struct MainSubHeaderView: View {
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    var isRank: Bool = false
    var body: some View {
        HStack(alignment: .bottom){
            if isRank {
                Text("랭크")
                    .font(Font.system(size: 18))
                    .bold()
                    .underline(true, color: .darkBlue414756)
                    .foregroundColor(.darkBlue414756)
            } else {
                Text("마이 리포트")
                    .font(Font.system(size: 18))
                    .bold()
                    .underline(true, color: .darkBlue414756)
                    .foregroundColor(.darkBlue414756)
            }
            Spacer()
            if isRank {
                Button{
                    coordinator.push(.rank)
                } label: {
                    Text("더보기 >")
                        .font(Font.system(size: 12).weight(.regular))
                        .foregroundColor(.lightGrayA5ABBD)
                }
            } else {
                Button{
                    coordinator.push(.myReport)
                } label: {
                    Text("더보기 >")
                        .font(Font.system(size: 12).weight(.regular))
                        .foregroundColor(.lightGrayA5ABBD)
                }
            }
        }
        .padding(.top,15)
    }
}

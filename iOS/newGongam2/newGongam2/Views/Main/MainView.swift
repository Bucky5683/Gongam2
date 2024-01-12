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
    - 유저의 평균시간 데이터베이스에서 받아오기
    - 평균시간에서 현재 공부한 시간을 뺴, 남은 시간을 출력
    - 평균시간보다 유저가 공부한 시간이 적으면 평균공부시간보다 낮게, 공부한 시간이 많으면 평균 공부시간보다 높게 View 설정
    - 유저의 등수가 어느정도인지, 데이터베이스에서 받아오기(만약 1000등 이상이면 999+로 출력)
 3. 마이 리포트
    - 유저의 한주 데이터를 받아옴
        - 만약 수요일(1/11)이라고 해도, 그 주의 일요일(1/7)부터 토요일(1/13)까지의 데이터 불러옴
        - 목요일,금요일, 토요일 데이터가 없으면 회색으로 "-" 처리
 4. 프로필
    - 유저의 프로필 사진 출력 ✅
    - 터치 시, 프로필 Popup이 뜸 ✅
    - 유저의 이름, 이메일, 목표공부시간 출력 ✅
    - Popup의 프로필 사진 터치 시, 프로필 수정 화면으로 이동 ✅
    - 도움말, 이동약관은 Web브라우저로 이동하도록 설정
 */
class MainViewModel: ObservableObject {
    
    //메인 헤더
    func calculateReMainTime(goalTime: Int, todayStudyTime: Int) -> Int{ //목표시간에서 현재 공부한 시간을 빼, 0보다 크면 남은 시간을, 아니면 -1을 반환
        let remainTime = goalTime - todayStudyTime
        if remainTime > 0{
            return remainTime
        } else {
            return -1
        }
    }
    
    
}

// MARK: 메인화면 뷰
struct MainView: View {
    @EnvironmentObject var userData: UserData
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @State var viewModel: MainViewModel = MainViewModel()
    @State var showingPopup = false
    
    var body: some View {
        NavigationView{
            ZStack{
                VStack{
                    Button{
                        print("MainHeader Profile Image Clicked!")
                        showingPopup = true
                    }label: {
                        AsyncImage(url: URL(string: userData.profileImageURL)){ image in
                            image.resizable()
                        } placeholder: {
                            ProgressView()
                        }.frame(width: 30, height: 30)
                    }
                    MainHeaderView(viewModel: $viewModel)
                    //MARK: 타이머
                    VStack {
                        Text("타이머")
                        TimersButtonView(isTimer: true)
                        TimersButtonView(isTimer: false)
                    }
                    
                    //MARK: 랭크
                    VStack{
                        MainSubHeaderView(isRank: true)
                        Button {
                            coordinator.push(.rank)
                        } label: {
                            MainRankChartView()
                        }
                        Text("평균보다 만큼 덜 공부했어요")
                    }
                    
                    //MARK: 마이리포트
                    VStack{
                        MainSubHeaderView(isRank: false)
                        Button {
                            coordinator.push(.myReport)
                        } label: {
                            MainMyReportGridView()
                        }
                        Text("이번 주에 평균 만큼 공부했어요!")
                    }
                }
            }.popup(isPresented: $showingPopup){
                VStack{
                    HStack{
                        VStack{
                            Text(userData.name)
                            Text(userData.email)
                            Text("목표 공부시간 \(userData.goalStudyTime.timeToText())")
                        }
                        Button {
                            coordinator.isProfileEdit = true
                            coordinator.push(.setProfile)
                        } label: {
                            AsyncImage(url: URL(string: userData.profileImageURL)){ image in
                                image.resizable()
                            } placeholder: {
                                ProgressView()
                            }.frame(width: 80, height: 80)
                        }
                    }
                    Button {
                        print("Clicked Helper!!")
                    } label: {
                        Text("도움말")
                    }
                    Button {
                        print("Clicked 이용약관!!")
                    } label: {
                        Text("이용약관")
                    }
                }.background(.whiteFFFFFF)
            } customize: {
                $0
                    .type(.floater())
                    .position(.topTrailing)
                    .animation(.snappy)
                    .closeOnTapOutside(true)
                    .backgroundColor(.black.opacity(0.5))
            }
        }
        .background(.whiteFFFFFF, ignoresSafeAreaEdges: .all)
        .navigationBarHidden(true)
    }
}


struct TimersButtonView: View {
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    var isTimer: Bool
    var body: some View {
        HStack{
            if isTimer{
                Text("⏰")
                Text("타이머")
                Button{
                    coordinator.push(.timer)
                } label: {
                    HStack{
                        Text("GO")
                        Image("goButtonIcon")
                    }
                }
            } else {
                Text("⏱️")
                Text("스톱워치")
                Button{
                    coordinator.push(.stopwatch)
                } label: {
                    HStack{
                        Text("GO")
                        Image("goButtonIcon")
                    }
                }
            }
        }
    }
}

struct MainSubHeaderView: View {
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    var isRank: Bool = false
    var body: some View {
        HStack{
            Spacer()
            if isRank {
                Text("랭크")
            } else {
                Text("마이 리포트")
            }
            Spacer()
            if isRank {
                Button{
                    coordinator.push(.rank)
                } label: {
                    Text("더보기 >")
                }
            } else {
                Button{
                    coordinator.push(.myReport)
                } label: {
                    Text("더보기 >")
                }
            }
            Spacer()
        }
    }
}

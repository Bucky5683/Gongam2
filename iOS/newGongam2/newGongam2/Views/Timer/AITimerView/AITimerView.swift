//
//  AITimerView.swift
//  newGongam2
//
//  Created by 김서연 on 1/29/24.
//

import SwiftUI
import UIKit
import AVFoundation
import Vision

struct AITimerView: View {
    @EnvironmentObject var userDataManager: UserDataManager
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @ObservedObject private var viewModel = AITimerViewModel()
    
    var body: some View {
        VStack(spacing: 20){
            MySwiftUIView(viewModel: viewModel)
            HStack {
                VStack{
                    Text("\(String(format: "%02d", self.viewModel.hours))")
                        .font(Font.system(size: 48).bold())
                        .foregroundColor(.whiteFFFFFF)
                    Text("시간")
                        .font(Font.system(size: 15).bold())
                        .foregroundColor(.whiteFFFFFF)
                }
                Text(":")
                VStack{
                    Text("\(String(format: "%02d", self.viewModel.minutes))")
                        .font(Font.system(size: 48).bold())
                        .foregroundColor(.whiteFFFFFF)
                    Text("분")
                        .font(Font.system(size: 15).bold())
                        .foregroundColor(.whiteFFFFFF)
                }
                Text(":")
                VStack{
                    Text("\(String(format: "%02d", self.viewModel.seconds))")
                        .font(Font.system(size: 48).bold())
                        .foregroundColor(.whiteFFFFFF)
                    Text("초")
                        .font(Font.system(size: 15).bold())
                        .foregroundColor(.whiteFFFFFF)
                }
            }
            HStack{
                Text("오늘 목표")
                    .font(Font.system(size: 18).bold())
                    .underline()
                    .foregroundColor(.white)
                Spacer()
                Text("\(self.userDataManager.userInfo.goalStudyTime.timeToText())")
                    .font(Font.system(size: 18))
                    .multilineTextAlignment(.trailing)
                    .foregroundColor(.white)
            }
            .padding(.leading, 67)
            .padding(.trailing, 64)
            ZStack {
                if self.viewModel.isStarted {
                    Button {
                        self.viewModel.isStarted = false
                    } label: {
                        Text("START")
                            .font(Font.system(size: 18).bold())
                            .multilineTextAlignment(.center)
                            .foregroundColor(.white)
                    }.disabled(self.viewModel.isStarted == false)
                        .frame(width: 80, height: 80)
                        .background(.blue5C84FF)
                        .cornerRadius(80)
                    
                } else {
                    Button {
                        self.viewModel.stopTimer()
                        self.viewModel.timerFinished = true
                        self.viewModel.updateFirebase(userDataManager: self.userDataManager, isTimerFinished: self.viewModel.timerFinished)
                        self.viewModel.isStarted = true
                    } label: {
                        Text("STOP")
                            .font(Font.system(size: 18).bold())
                            .multilineTextAlignment(.center)
                            .foregroundColor(.white)
                    }.disabled(self.viewModel.isStarted == true)
                        .frame(width: 80, height: 80)
                        .background(.redFF0000)
                        .cornerRadius(80)
                }
            }
            Spacer()
        }
        .background(.darkBlue414756)
        .navigationBarTitle("AI 스톱워치",displayMode: .inline)
        .navigationBarBackButtonHidden(true)
        .navigationBarItems(leading:
            Button{
                self.coordinator.pop()
            } label: {
                Text("Main")
                .foregroundColor(.white)
            }
        )
    }
}

struct MySwiftUIView: UIViewControllerRepresentable {
    @ObservedObject var viewModel: AITimerViewModel
    func makeUIViewController(context: Context) -> VisionObjectRecognitionViewController {
        let storyboard = UIStoryboard(name: "AITimer", bundle: nil)
        if let vc = storyboard.instantiateViewController(withIdentifier: "VisionObjectRecognitionViewController") as? VisionObjectRecognitionViewController {
            vc.viewModel = viewModel
            return vc
        } else {
            fatalError("Failed to instantiate VisionObjectRecognitionViewController")
        }
    }
    
    func updateUIViewController(_ uiViewController: VisionObjectRecognitionViewController, context: Context) {
        if uiViewController.viewModel != viewModel {
            uiViewController.viewModel = viewModel
        }
    }
    
    typealias UIViewControllerType = VisionObjectRecognitionViewController
    
}

#Preview {
    AITimerView()
}

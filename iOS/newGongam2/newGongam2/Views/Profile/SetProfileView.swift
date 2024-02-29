//
//  SetProfileView.swift
//  newGongam2
//
//  Created by 김서연 on 1/8/24.
//

import SwiftUI
import UIKit

class SetProfileViewModel : ObservableObject{
    @Published var hours: Int = 0
    @Published var minutes: Int = 0
    @Published var seconds: Int = 0
    @Published var isStarted: Bool = true
    @Published var timerFinished: Bool = false
    @Published var goalTime: Int = 0
    
    func initGoalTime(goalTime: Int) {
        self.goalTime = goalTime
        self.seconds = goalTime % 60
        self.minutes = (goalTime - self.seconds) % 3600
        self.hours = (goalTime - self.seconds - self.minutes)
    }
    
    func downGestureTime(type: timeType){
        switch(type){
        case .hour:
            self.hours -= 3600
            if self.hours < 0 {
                self.hours = 0
            }
        case .minute:
            self.minutes -= 60
            if self.minutes < 0 {
                self.minutes = 0
            }
        case .second:
            self.seconds -= 1
            if self.seconds < 0 {
                self.seconds = 0
            }
        }
        self.goalTime = self.hours + self.minutes + self.seconds
        print(self.goalTime)
    }
    
    func upGestureTime(type: timeType){
        switch(type){
        case .hour:
            self.hours += 3600
            if self.hours > 24 * 3600 {
                self.hours = 24 * 3600
            }
        case .minute:
            self.minutes += 60
            if self.minutes >= 3600 {
                self.minutes = 3540
            }
        case .second:
            self.seconds += 1
            if self.seconds >= 60 {
                self.seconds = 59
            }
        }
        self.goalTime = self.hours + self.minutes + self.seconds
        print(self.goalTime)
    }
}

struct SetProfileView: View {
    @EnvironmentObject var userDataManager: UserDataManager
    @Environment(NavigationCoordinator.self) var coordinator: NavigationCoordinator
    @State private var isDragging = false
    @State var name: String = ""
    @State var email: String = ""
    @State var goalTime: String = ""
    @State var gotoMainView: Bool = false
    @State private var previousTranslation: CGFloat = 0
    @ObservedObject var viewModel: SetProfileViewModel = SetProfileViewModel()
    @State var isEditProfile: Bool = false
    @State var isDeleteSuccess: Bool = false
    @State var showModal: Bool = false
    @State var selectedUIImage: UIImage?
    @State var image: Image?
    
    func loadImage() {
        if let selectedImage = selectedUIImage{
            image = Image(uiImage: selectedImage)
        } else {
            return
        }
    }
    
    var body: some View {
            ScrollView{
                VStack(spacing: 20){
                    Spacer()
                    Button{
                        print(self.userDataManager.id)
                        print(self.userDataManager.userInfo.name)
                        showModal.toggle()
                    } label: {
                        if let profileImage = self.image {
                            profileImage.resizable()
                        } else {
                            AsyncImage(url: URL(string: self.userDataManager.userInfo.profileImageURL)){ image in
                                image.resizable()
                            } placeholder: {
                                ProgressView()
                            }
                        }
                    }.frame(width: 150, height: 150)
                        .cornerRadius(150)
                        .padding(.top, 45)
                        .shadow(color: .black.opacity(0.25), radius: 2, x: 0, y: 4)
                        .sheet(isPresented: self.$showModal, onDismiss: { loadImage() }){
                            ImagePickerView(image: $selectedUIImage)
                        }
                    HStack{
                        Text("닉네임")
                            .font(Font.system(size: 15).bold()).multilineTextAlignment(.leading).foregroundColor(.black)
                            .padding(.leading, 38)
                        Spacer()
                    }
                    HStack{
                        Spacer()
                        if self.userDataManager.userInfo.name != ""{
                            TextField(self.userDataManager.userInfo.name, text: $name)
                                .font(.system(size: 15))
                                .multilineTextAlignment(.leading)
                                .frame(width: 317,height: 50)
                                .padding(.leading, 10)
                                .padding(.trailing, 10)
                                .background(Color.lightGrayA5ABBD)
                                .cornerRadius(10)
                        } else{
                            TextField("Enter your name", text: $name)
                                .font(.system(size: 15))
                                .multilineTextAlignment(.leading)
                                .frame(width: 317,height: 50)
                                .padding(.leading, 10)
                                .padding(.trailing, 10)
                                .background(Color.lightGrayA5ABBD)
                                .cornerRadius(10)
                        }
                        Spacer()
                    }
                    HStack{
                        Text("이메일")
                            .font(Font.system(size: 15).bold()).multilineTextAlignment(.leading).foregroundColor(.black)
                            .padding(.leading, 38)
                        Spacer()
                    }
                    HStack{
                        Spacer()
                        if self.userDataManager.userInfo.email != "" {
                            TextField(self.userDataManager.userInfo.email, text: $email)
                                .font(.system(size: 15))
                                .multilineTextAlignment(.leading)
                                .keyboardType(.emailAddress)
                                .frame(width: 317,height: 50)
                                .padding(.leading, 10)
                                .padding(.trailing, 10)
                                .background(Color.lightGrayA5ABBD)
                                .cornerRadius(10)
                        } else{
                            TextField("Enter your email", text: $email)
                                .font(.system(size: 15))
                                .multilineTextAlignment(.leading)
                                .keyboardType(.emailAddress)
                                .frame(width: 317,height: 50)
                                .padding(.leading, 10)
                                .padding(.trailing, 10)
                                .background(Color.lightGrayA5ABBD)
                                .cornerRadius(10)
                        }
                        Spacer()
                    }
                    if isEditProfile {
                        HStack{
                            Text("목표 공부시간")
                                .font(Font.system(size: 15).bold()).multilineTextAlignment(.leading).foregroundColor(.black)
                                .padding(.leading, 38)
                            Spacer()
                        }
                        HStack {
                            Spacer()
                            VStack{
                                Text("\(String(format: "%02d", self.viewModel.hours/3600))")
                                    .font(Font.system(size: 48).bold())
                                    .multilineTextAlignment(.center)
                                    .foregroundColor(.darkBlue414756)
                                    .padding()
                                    .gesture(
                                        DragGesture()
                                            .onChanged { value in
                                                self.isDragging = true
                                                let translation = value.translation.height
                                                // 드래그 방향에 따라 숫자를 증가 또는 감소
                                                if self.previousTranslation - translation > 10{
                                                    self.viewModel.downGestureTime(type: .hour)
                                                    self.previousTranslation = translation
                                                } else if self.previousTranslation - translation < -10{
                                                    self.viewModel.upGestureTime(type: .hour)
                                                    self.previousTranslation = translation
                                                }
                                            }
                                            .onEnded { _ in
                                                self.isDragging = false
                                            }
                                    )
                                Text("시간")
                                    .font(Font.system(size: 15).bold())
                                    .multilineTextAlignment(.center)
                                    .foregroundColor(.darkBlue414756)
                            }
                            Text(":").font(Font.system(size: 48).bold()).foregroundColor(.darkBlue414756)
                            VStack{
                                Text("\(String(format: "%02d", self.viewModel.minutes/60))")
                                    .font(Font.system(size: 48).bold())
                                    .multilineTextAlignment(.center)
                                    .foregroundColor(.darkBlue414756)
                                    .padding()
                                    .gesture(
                                        DragGesture()
                                            .onChanged { value in
                                                self.isDragging = true
                                                let translation = value.translation.height
                                                print("translation: \(translation), previousTranslation: \(self.previousTranslation)")
                                                // 드래그 방향에 따라 숫자를 증가 또는 감소
                                                if self.previousTranslation - translation > 10{
                                                    self.viewModel.downGestureTime(type: .minute)
                                                    self.previousTranslation = translation
                                                } else if self.previousTranslation - translation < -10{
                                                    self.viewModel.upGestureTime(type: .minute)
                                                    self.previousTranslation = translation
                                                }
                                                
                                            }
                                            .onEnded { _ in
                                                self.isDragging = false
                                            }
                                    )
                                Text("분")
                                    .font(Font.system(size: 15).bold())
                                    .multilineTextAlignment(.center)
                                    .foregroundColor(.darkBlue414756)
                            }
                            Text(":").font(Font.system(size: 48).bold()).foregroundColor(.darkBlue414756)
                            VStack{
                                Text("\(String(format: "%02d", self.viewModel.seconds))")
                                    .font(Font.system(size: 48).bold())
                                    .multilineTextAlignment(.center)
                                    .foregroundColor(.darkBlue414756)
                                    .padding()
                                    .gesture(
                                        DragGesture()
                                            .onChanged { value in
                                                self.isDragging = true
                                                let translation = value.translation.height
                                                print("translation: \(translation), previousTranslation: \(self.previousTranslation)")
                                                // 드래그 방향에 따라 숫자를 증가 또는 감소
                                                if self.previousTranslation - translation > 10{
                                                    self.viewModel.downGestureTime(type: .second)
                                                    self.previousTranslation = translation
                                                } else if self.previousTranslation - translation < -10{
                                                    self.viewModel.upGestureTime(type: .second)
                                                    self.previousTranslation = translation
                                                }
                                                
                                            }
                                            .onEnded { _ in
                                                self.isDragging = false
                                            }
                                    )
                                Text("초")
                                    .font(Font.system(size: 15).bold())
                                    .multilineTextAlignment(.center)
                                    .foregroundColor(.darkBlue414756)
                            }
                            Spacer()
                        }
                    }
                    Spacer()
                    Button{
                        if self.name != ""{
                            self.userDataManager.userInfo.name = self.name
                        }
                        if self.email != "" {
                            self.userDataManager.userInfo.email = self.email
                        }
                        print(self.userDataManager.userInfo)
                        self.userDataManager.userInfo.goalStudyTime = self.viewModel.goalTime
                        self.userDataManager.writeRankData()
                        self.userDataManager.writeUserInfo()
                        if let selectedImage = self.selectedUIImage {
                            self.userDataManager.writeProfileImage(selectedImage)
                        }
                        if self.isEditProfile {
                            coordinator.isProfileEdit = false
                            coordinator.pop()
                        } else {
                            coordinator.changeRoot(.main)
                        }
                        
                    } label: {
                        Text("Next")
                            .font(Font.system(size: 18).bold())
                            .foregroundColor(.whiteFFFFFF)
                    }.disabled(self.userDataManager.id == "")
                        .frame(width: 100, height: 100)
                        .background(.darkBlue414756)
                        .cornerRadius(100)
                        .shadow(color: .black.opacity(0.25), radius: 2, x: 0, y: 4)
                    
                    if self.isEditProfile == true {
                        Button {
                            print("회원탈퇴")
                            self.isDeleteSuccess = self.userDataManager.deleteUserData()
                            if self.isDeleteSuccess {
                                coordinator.changeRoot(.login)
                            }
                        } label: {
                            Text("회원탈퇴")
                                .font(Font.system(size: 15).bold())
                                .foregroundColor(.redFF0000)
                                .underline(true)
                        }
                        .alert(isPresented: self.$isDeleteSuccess, content: {
                            Alert(title: Text("회원탈퇴에 실패했습니다."), message: Text("회원탈퇴에 실패했습니다. 다시 시도해주세요."), dismissButton: .default(Text("확인")))
                        })
                    }
                    Spacer()
                }
            }
            .scrollDisabled(self.isDragging)
            .background(.whiteFFFFFF, ignoresSafeAreaEdges: .all)
            .navigationBarHidden(!isEditProfile)
            .navigationBarTitle("프로필 수정",displayMode: .inline)
            .navigationBarBackButtonHidden(true)
            .navigationBarItems(leading:
                Button{
                    self.coordinator.pop()
                } label: {
                    Text("Main")
                    .foregroundColor(.black)
                }
            )
            .onAppear(){
                self.viewModel.initGoalTime(goalTime: self.userDataManager.userInfo.goalStudyTime)
            }
    }
}

struct ImagePickerView : UIViewControllerRepresentable {
    @Binding var image: UIImage?
    @Environment(\.presentationMode) var mode
        
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
        
    func makeUIViewController(context: Context) -> some UIViewController {
        let picker = UIImagePickerController()
        picker.delegate = context.coordinator
        picker.sourceType = .photoLibrary
        return picker
    }
        
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
            
    }
}

extension ImagePickerView {
    class Coordinator: NSObject, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
        let parent: ImagePickerView
        
        init(_ parent: ImagePickerView) {
            self.parent = parent
        }
        
        func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
            guard let image = info[.originalImage] as? UIImage else { return }
            parent.image = image
            parent.mode.wrappedValue.dismiss()
        }
    }
}

#if canImport(UIKit)
extension View {
    func hideKeyboard() {
        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
    }
}
#endif

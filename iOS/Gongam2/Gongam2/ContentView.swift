//
//  ContentView.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/12.
//

import SwiftUI

struct ContentView: View {
    var body: some View {
        GeometryReader{ geometry in
            VStack(spacing: 20.0){
                VStack(spacing: 15.0){  // 헤더
                    Text("오늘 공부한 시간")
                        .font(.system(size: 12))
                        .underline()
                        .multilineTextAlignment(.center)
                        .foregroundColor(.whiteFFFFFF)
                    
                    Text("99:99:99")
                        .font(.system(size: 30).weight(.bold))
                        .multilineTextAlignment(.center)
                        .foregroundColor(.whiteFFFFFF)
                    
                    HStack{
                        Text("🔥")
                            .font(.system(size: 12))
                            .multilineTextAlignment(.center)
                        Text("목표까지")
                            .font(.system(size: 12))
                            .multilineTextAlignment(.center)
                        Text("99:99:99")
                            .font(.system(size: 12).weight(.bold))
                            .multilineTextAlignment(.center)
                        Text("🔥")
                            .font(.system(size: 12))
                            .multilineTextAlignment(.center)
                    }.padding(.leading, 30)
                        .padding(.trailing, 30)
                        .padding(.top, 7)
                        .padding(.bottom, 7)
                        .background(.grayA5ABBD)
                        .cornerRadius(20)
                    
                }.frame(width: geometry.size.width, height: 280).background(Color.darkBlue414756)
                
                VStack(spacing: 15.0){  //타이머
                    Text("타이머").fontWeight(.bold).underline()
                    HStack(spacing: 10.0){
                        Text("⏰").font(.system(size: 20)).fontWeight(.medium)
                        Text("타이머").font(.system(size: 15)).fontWeight(.medium)
                        Spacer()
                        HStack{
                            Text("GO").font(.system(size: 15)).fontWeight(.medium)
                                .foregroundColor(.whiteFFFFFF)
                            Image("goTriangle").resizable().frame(width: 15.0, height: 15.0)
                            Button(""){}.padding(.leading, 0).padding(.trailing, 0).padding(.top, 0).padding(.bottom, 0)
                        }.frame(width: 80, height: 48).background(.darkBlue414756).cornerRadius(10, corners: [.topRight, .bottomRight])
                    }.padding(.leading, 20).frame(width: geometry.size.width-80, height: 48).background(.whiteFFFFFF).cornerRadius(10).shadow(color: .black.opacity(0.1), radius: 5, x: 0, y: 4)
                    HStack(spacing: 10.0){
                        Text("⏱️").font(.system(size: 20)).fontWeight(.medium)
                        Text("스톱워치").font(.system(size: 15)).fontWeight(.medium)
                        Spacer()
                        HStack{
                            Text("GO").font(.system(size: 15)).fontWeight(.medium)
                                .foregroundColor(.whiteFFFFFF)
                            Image("goTriangle").resizable().frame(width: 15.0, height: 15.0)
                            Button(""){print("Go!")}.padding(.leading, 0).padding(.trailing, 0).padding(.top, 0).padding(.bottom, 0)
                        }.frame(width: 80, height: 48).background(.darkBlue414756).cornerRadius(10, corners: [.topRight, .bottomRight])
                    }.padding(.leading, 20)
                        .frame(width: geometry.size.width-80, height: 48)
                        .background(.whiteFFFFFF)
                        .cornerRadius(10)
                        .shadow(color: .black.opacity(0.1), radius: 5, x: 0, y: 4)
                }
            }
        }
        
    }
}

func GoButton() {
    HStack{
        Text("GO").font(.system(size: 15)).fontWeight(.medium)
            .foregroundColor(.whiteFFFFFF)
        Image("goTriangle").resizable().frame(width: 15.0, height: 15.0)
        Button(""){}.padding(.leading, 0).padding(.trailing, 0).padding(.top, 0).padding(.bottom, 0)
    }.frame(width: 80, height: 48).background(.darkBlue414756).cornerRadius(10, corners: [.topRight, .bottomRight])
}

#Preview {
    ContentView()
}

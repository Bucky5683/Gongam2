//
//  TimerView.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import SwiftUI

struct TimerView: View {
    var isStarted: Bool = false
    let timer = Timer.publish(every: 1, on: .main, in: .common).autoconnect()
    var body: some View {
        VStack{
            if isStarted == false{
                Text("📚😴📚")
            } else{
                Text("📚🙇📚")
            }
            
        }
    }
}

#Preview {
    TimerView()
}

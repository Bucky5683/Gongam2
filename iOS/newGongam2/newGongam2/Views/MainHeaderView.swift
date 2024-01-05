//
//  MainHeaderView.swift
//  newGongam2
//
//  Created by 김서연 on 1/5/24.
//

import SwiftUI

struct MainHeaderView: View {
    var time: String = "00:00:00"
    var purposeTimeRemains: Bool = true
    
    var body: some View {
        VStack(content: {
            Text("오늘 공부한 시간")
            Text(time)
            HStack(){
                if purposeTimeRemains{
                    Text("목표까지")
                    Text("남은 시간")
                }
            }
        })
    }
}

#Preview {
    MainHeaderView(time: "99:99:99")
}

//
//  MainRankBar.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/13.
//

import SwiftUI

struct MainRankBar: View {
    var body: some View {
        HStack{
            Spacer().frame(width: 78)
            Rectangle()
                .foregroundColor(.clear)
                .background(
                    LinearGradient(
                        stops: [
                            Gradient.Stop(color: Color(.darkBlue414756).opacity(0.7), location: 0.00),
                            Gradient.Stop(color: Color(.darkBlue414756), location: 1.00),
                        ],
                        startPoint: UnitPoint(x: 0.5, y: 0),
                        endPoint: UnitPoint(x: 0.5, y: 1)
                    )
                )
                .cornerRadius(10)
            Spacer().frame(width: 261)
        }.frame(height: 215)
    }
}

#Preview {
    MainRankBar()
}

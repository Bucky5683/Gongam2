//
//  SwiftUIView.swift
//  Gongam2
//
//  Created by 김서연 on 2023/12/13.
//

import SwiftUI

struct DayReportCircle: View {
    var Aday: String
    var Atime: Int
    var AtimeMeasure: String
    var body: some View {
        VStack{
            Text(Aday)
                .font(.system(size: 15))
                .fontWeight(.bold)
                .foregroundColor(.darkBlue414756)
            if Atime < 0 {
                Text("\(Atime)" + AtimeMeasure)
                    .font(.system(size: 10))
                    .fontWeight(.medium)
                    .foregroundColor(.blue5C84FF)
            } else if Atime > 0{
                Text("\(Atime)" + AtimeMeasure)
                    .font(.system(size: 10))
                    .fontWeight(.medium)
                    .foregroundColor(.redFF0000)
            } else{
                Text("\(Atime)" + AtimeMeasure)
                    .font(.system(size: 10))
                    .fontWeight(.medium)
                    .foregroundColor(.grayA5ABBD)
            }
        }.frame(width: 50, height: 50)
            .background(.whiteFFFFFF)
            .cornerRadius(50)
            .shadow(color: .black.opacity(0.25), radius: 2, x: 0, y: 4)
    }
}

#Preview {
    DayReportCircle(Aday: "S", Atime: -99, AtimeMeasure: "h")
}

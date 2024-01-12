//
//  Extenstiones.swift
//  newGongam2
//
//  Created by 김서연 on 1/11/24.
//

import SwiftUI

extension String {
    func timeToInt() -> Int{
        let arr = self.split(separator: ":")
        let hour = (Int(arr[0]) ?? 0) * 60 * 60
        let minute = (Int(arr[1]) ?? 0) * 60
        let seconds = (Int(arr[2]) ?? 0)
        
        let time = hour + minute + seconds
        return time
    }
}

extension Int {
    func timeToText() -> String{ // 시간(Int)을 "00:00:00"형식으로 변환
        let seconds = self % 60
        let minute = (self - seconds) % 60
        let hours = (self - seconds - (minute*60)) % 60
        
        let time = String(format: "%02d", hours) + ":" + String(format: "%02d", minute) + ":" + String(format: "%02d", seconds)
        return time
    }
}



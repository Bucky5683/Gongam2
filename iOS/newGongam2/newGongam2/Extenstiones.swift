//
//  Extenstiones.swift
//  newGongam2
//
//  Created by 김서연 on 1/11/24.
//

import SwiftUI

public enum weeklyDay{
    case Monday
    case Tuesday
    case Wednesday
    case Thursday
    case Friday
    case Saturday
    case Sunday
}


extension String {
    func timeToInt() -> Int{
        let arr = self.split(separator: ":")
        let hour = (Int(arr[0]) ?? 0) * 60 * 60
        let minute = (Int(arr[1]) ?? 0) * 60
        let seconds = (Int(arr[2]) ?? 0)
        
        let time = hour + minute + seconds
        return time
    }
    
    func mmdd() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        
        let currentDate = self
        let date = dateFormatter.date(from: currentDate) ?? Date()
        dateFormatter.dateFormat = "MM-dd"
        let datemmdd = dateFormatter.string(from: date)
        return datemmdd
    }
    
    func getDaysFromString() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        
        let currentDate = self
        let date = dateFormatter.date(from: currentDate) ?? Date()
        dateFormatter.dateFormat = "EEEEE"
        let days = dateFormatter.string(from: date)
        return days
    }
    
    func getDaysFromFULLString() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        
        let currentDate = self
        let date = dateFormatter.date(from: currentDate) ?? Date()
        dateFormatter.dateFormat = "EEEE"
        let days = dateFormatter.string(from: date)
        return days
    }
}

extension Int {
    func timeToText() -> String{ // 시간(Int)을 "00:00:00"형식으로 변환
        let seconds = self % 60
        let minute = ((self - seconds) % 3600) / 60
        let hours = (self - seconds - (minute*60)) / 3600
        
        let time = String(format: "%02d", hours) + ":" + String(format: "%02d", minute) + ":" + String(format: "%02d", seconds)
        return time
    }
    
    func timeToTextForWeekly() -> String{ // 시간(Int)을 "00:00:00"형식으로 변환
        let seconds = self % 60
        let minute = ((self - seconds) % 3600) / 60
        let hours = (self - seconds - (minute*60)) / 3600
        
        let time = String(format: "%03d", hours) + ":" + String(format: "%02d", minute) + ":" + String(format: "%02d", seconds)
        return time
    }
    
    func nonNegativeModulo(_ modulo: Int) -> Int {
        let result = self % modulo
        return result < 0 ? result + modulo : result
    }
}

extension Date {
    func getCurrentDateAsString() -> String {
        let dateFormatter = DateFormatter()
        let koreaTimeZone = TimeZone(identifier: "Asia/Seoul")!
        dateFormatter.dateFormat = "yyyy-MM-dd"
        dateFormatter.timeZone = koreaTimeZone
        
        let currentDate = self
        let dateString = dateFormatter.string(from: currentDate)
        
        return dateString
    }
    
    func getWeeksAgoSunday(week: Int) -> [Date]?{
        let calendar = Calendar.current

        if let weeksAgo = calendar.date(byAdding: .weekOfYear, value: week * -1, to: self) {
            if let startOfWeek = calendar.date(bySetting: .weekday, value: 1, of: weeksAgo),
               let endOfWeek = calendar.date(byAdding: .day, value: 6, to: startOfWeek) {
                let dateFormatter = DateFormatter()
                let koreaTimeZone = TimeZone(identifier: "Asia/Seoul")!
                dateFormatter.dateFormat = "yyyy-MM-dd"
                dateFormatter.timeZone = koreaTimeZone
                print("\(week) weeks ago, Sunday was between \(dateFormatter.string(from: startOfWeek)) and \(dateFormatter.string(from: endOfWeek))")
                return [startOfWeek, endOfWeek]
            } else {
                print("Error: Unable to calculate the start or end of the week")
                return nil
            }
        } else {
            print("Error: Unable to calculate three weeks ago")
            return nil
        }
    }
    
    func findSaturday() -> Date? {
        let calendar = Calendar.current
        if let weeksAgo = calendar.date(byAdding: .weekOfYear, value: 0, to: self) {
            if let startOfWeek = calendar.date(bySetting: .weekday, value: 1, of: weeksAgo),
               let endOfWeek = calendar.date(byAdding: .day, value: -1, to: startOfWeek) {
                let dateFormatter = DateFormatter()
                let koreaTimeZone = TimeZone(identifier: "Asia/Seoul")!
                dateFormatter.dateFormat = "yyyy-MM-dd"
                dateFormatter.timeZone = koreaTimeZone
                print("Saturday: \(dateFormatter.string(from: endOfWeek))")
                return endOfWeek
            } else {
                print("Error: Unable to calculate Saturday")
                return nil
            }
        } else {
            print("Error: Unable to calculate Saturday")
            return nil
        }
    }
    
    func getFirstDayOfCurrentWeek() -> Date? {
        let calendar = Calendar.current
        
        // 현재 날짜가 속한 주의 첫 번째 날을 찾음
        let currentDateComponents = calendar.dateComponents([.yearForWeekOfYear, .weekOfYear, .weekday], from: self)
        let firstDayOfWeek = calendar.date(from: currentDateComponents)
        
        return firstDayOfWeek
    }
}

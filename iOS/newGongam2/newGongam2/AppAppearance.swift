//
//  AppAppearance.swift
//  newGongam2
//
//  Created by 김서연 on 2/27/24.
//

import Foundation
import UIKit

final class AppAppearance {
    static func setupAppearance() {

        // MARK: - NavigationBar
        /// navigationBar 배경 색상
        UINavigationBar.appearance().backgroundColor = .clear
        /// navigationBar의 버튼 색상 (ex - back 버튼)
        UINavigationBar.appearance().tintColor = .black
        /// navigationBar 중앙에 존재하는 title 색상
        UINavigationBar.appearance().titleTextAttributes = [NSAttributedString.Key.foregroundColor: UIColor.black]
    }
}

//
//  UIViewExtensions.swift
//  Gongam2
//
//  Created by 김서연 on 12/14/23.
//

import UIKit

extension UIView{
    func findViewController() -> UIViewController? {
        if let nextResponder = self.next as? UIViewController {
            return nextResponder
        } else if let nextResponder = self.next as? UIView {
            return nextResponder.findViewController()
        } else {
            return nil
        }
    }
}

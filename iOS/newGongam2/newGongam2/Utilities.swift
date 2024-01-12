//
//  Utilities.swift
//  newGongam2
//
//  Created by 김서연 on 1/8/24.
//

import Foundation
import UIKit

final class Utilities {
    static let shared = Utilities()
    private init() { }
    
    @MainActor
    func topViewController(controller: UIViewController? = nil) -> UIViewController? {
        let controller = controller ?? UIApplication.shared.keyWindow?.rootViewController
        if let navigationController = controller as? UINavigationController {
            return topViewController(controller: navigationController.visibleViewController)
        }
        if let tabController = controller as? UITabBarController {
            if let selected = tabController.selectedViewController {
                return topViewController(controller: selected)
            }
        }
        if let presented = controller?.presentedViewController {
            return topViewController(controller: presented)
        }
        return controller
    }
}

class CountPushLabel: UILabel, CAAnimationDelegate {
    var originCycle: Int!
    var cycle: Int! // 앞보다 늦게끝나기위한 프로퍼티
    var originNum: Int = 0
    var curNum: Int = 0 {
        didSet {
            if curNum > 9 {
                cycle -= 1
                curNum = 0
            }
        }
    }
    var duration: TimeInterval!
    
    func config(num: Int, cycle: Int, duration: TimeInterval = 0.1) {
        self.originCycle = cycle
        self.cycle = cycle
        self.originNum = num
        self.curNum = num
        self.duration = duration
        self.text = "\(num)"
        self.font = .boldSystemFont(ofSize: 20)
    }
    
    func animate() {
        curNum += 1
        self.text = "\(curNum)"
        pushAnimate()
    }
    
    private func pushAnimate() {
        let transition = CATransition()
        transition.duration = duration
        transition.timingFunction = .init(name: .easeInEaseOut)
        transition.type = .push
        transition.subtype = .fromTop
        transition.delegate = self
        self.layer.add(transition, forKey: CATransitionType.push.rawValue)
    }
    
    func animationDidStop(_ anim: CAAnimation, finished flag: Bool) {
        if curNum == originNum && cycle < 0 {
            self.layer.removeAllAnimations()
            return
        }
        animate()
    }
    
    func clean() {
        config(num: originNum, cycle: originCycle)
    }
}

class DragIntLabelText: UILabel,CAAnimationDelegate{
    var labelText: String = ""
    var number: Int = 0
    
    func config(number: Int){
        self.number = number
        self.labelText = String(format: "%02d", number)
    }
}

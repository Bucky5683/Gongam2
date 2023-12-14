//
//  BaseView.swift
//  Gongam2
//
//  Created by 김서연 on 12/14/23.
//

import UIKit

class BaseView: UIView {

    @IBInspectable var nibName: String?
    var contentView: UIView?
    override var backgroundColor: UIColor? {
        didSet {
            self.contentView?.backgroundColor = backgroundColor
        }
    }
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */
    func setupView() {
        guard let view = loadViewFromNib() else { return }
        view.frame = bounds
        view.autoresizingMask =
            [.flexibleWidth, .flexibleHeight]
        addSubview(view)
        contentView = view
        contentView?.backgroundColor = self.backgroundColor
    }

    func loadViewFromNib() -> UIView? {
        
        var xibName: String
        if let nibName = nibName {
            
            xibName = nibName
            
        } else {
            
            xibName = String(describing: type(of: self))
        }
        
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: xibName, bundle: bundle)
        return nib.instantiate(withOwner: self, options: nil).first as? UIView
    }
}

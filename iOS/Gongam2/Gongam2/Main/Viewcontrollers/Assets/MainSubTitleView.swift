//
//  MainSubTitleView.swift
//  Gongam2
//
//  Created by 김서연 on 12/14/23.
//

import UIKit
enum subViewType: String{
    case timer = "타이머"
    case rank = "랭킹"
    case myReport = "마이 리포트"
    case none
}

class MainSubTitleView: BaseView {
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var moreButton: UIButton!
    var type: subViewType = .timer
    var title: String = ""{
        didSet(type){
            self.title = type
            self.titleLabel.text = self.title
        }
    }
    var pageName: String = ""
    
    override init(frame: CGRect){
        super.init(frame: frame)
        setupView()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    override func setupView() {
        guard let view = loadViewFromNib() else { return }
        view.frame = bounds
        view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        addSubview(view)
        contentView = view
        self.setTitle()
        self.configureUI()
    }
    @IBAction func tapMoreButton(_ sender: Any) {
        self.findViewController()?.navigationController?.popViewController(animated: false)
    }
    
    func configureUI(){
        
    }
    
    func setTitle() {
        self.titleLabel.text = self.title
    }
}

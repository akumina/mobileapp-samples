//
//  HomeViewController.swift
//  akumina-ios-sample
//
//  Created by Mac on 05/05/23.
//

import Foundation


import UIKit

class HomeViewController : UIViewController {
        
    override func viewDidAppear(_ animated: Bool) {

    }
    
    @IBAction func profileBtnClicked(_ sender: Any) {
            UIUtils.showProfile();
    }
    
    @IBAction func homeBtnClicked(_ sender: Any) {
        UIUtils.showMain();
    }
    
}


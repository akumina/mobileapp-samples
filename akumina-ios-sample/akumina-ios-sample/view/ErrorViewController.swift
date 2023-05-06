//
//  Error.swift
//  akumina-ios-sample
//
//  Created by Mac on 05/05/23.
//

import Foundation
import UIKit


public class ErrorViewController : UIViewController {
    
    @IBOutlet weak var errorTxt: UITextView!
    
    @IBAction func homeBtnClicked(_ sender: Any) {
    }
    func setError(error: Error) {
        let errorMsg = "Error \(error)"
        errorTxt.text = errorMsg;
    }
}

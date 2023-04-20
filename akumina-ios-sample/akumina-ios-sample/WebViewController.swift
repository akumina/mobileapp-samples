//
//  WebViewController.swift
//  akumina-ios-sample
//
//  Created by Mac on 20/04/23.
//

import Foundation
import UIKit
import WebKit

class WebViewController : UIViewController {
    
    @IBOutlet weak var webView: WKWebView!
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidLoad();
       
        webView.load(URLRequest(url: Foundation.URL(string: Constants.WEB_APP_URL +  Constants.TOKEN)!))
       
    }
}

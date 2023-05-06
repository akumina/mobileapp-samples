//
//  UIUtils.swift
//  akumina-ios-sample
//
//  Created by Mac on 05/05/23.
//

import Foundation
import UIKit


class UIUtils {
    
    class func getCurrentViewController() -> UIViewController {
        var topController = UIApplication.shared.keyWindow?.rootViewController
        if (nil != topController) {
            var presentedViewController = topController!.presentedViewController
            //Loop until there are no more view controllers to go to
            while (nil != presentedViewController){
                topController = presentedViewController
                presentedViewController = topController!.presentedViewController
            }
        }
        //Return the final view controller
        return topController!
    }
    class func showMain() {
        let mainView = UIStoryboard(name: "Main", bundle: nil);
        let viewcontroller : ViewController = mainView.instantiateViewController(withIdentifier: "main") as! ViewController
        
        if Thread.isMainThread {
            viewcontroller.modalPresentationStyle = .fullScreen;
            self.getCurrentViewController().present(viewcontroller, animated: true);
        }else {
            DispatchQueue.main.async {
                viewcontroller.modalPresentationStyle = .fullScreen;
                self.getCurrentViewController().present(viewcontroller, animated: true);
            }
        }
    }
    
    class func showHome() {
        let mainView = UIStoryboard(name: "Home", bundle: nil);
        let viewcontroller : HomeViewController = mainView.instantiateViewController(withIdentifier: "home") as! HomeViewController
        
        if Thread.isMainThread {
            viewcontroller.modalPresentationStyle = .fullScreen;
            self.getCurrentViewController().present(viewcontroller, animated: true);
        }else {
            DispatchQueue.main.async {
                viewcontroller.modalPresentationStyle = .fullScreen;
                self.getCurrentViewController().present(viewcontroller, animated: true);
            }
        }
    }
    
    class func showWeb() {
        let mainView = UIStoryboard(name: "WebView", bundle: nil);
        let viewcontroller : WebViewController = mainView.instantiateViewController(withIdentifier: "webView") as! WebViewController
        
        if Thread.isMainThread {
            viewcontroller.modalPresentationStyle = .fullScreen;
            self.getCurrentViewController().present(viewcontroller, animated: true);
        }else {
            DispatchQueue.main.async {
                viewcontroller.modalPresentationStyle = .fullScreen;
                self.getCurrentViewController().present(viewcontroller, animated: true);
            }
        }
    }
    
    class func showError(error: Error) {
        let mainView = UIStoryboard(name: "Error", bundle: nil);
        let viewcontroller : ErrorViewController = mainView.instantiateViewController(withIdentifier: "error") as! ErrorViewController
        viewcontroller.setError(error: error)
        if Thread.isMainThread {
            viewcontroller.modalPresentationStyle = .fullScreen;
            
            self.getCurrentViewController().present(viewcontroller, animated: true);
        }else {
            DispatchQueue.main.async {
                viewcontroller.modalPresentationStyle = .fullScreen;
                self.getCurrentViewController().present(viewcontroller, animated: true);
            }
        }
    }
    
    class func showProfile() {
        let mainView = UIStoryboard(name: "Profile", bundle: nil);
        let viewcontroller : ProfileContoller = mainView.instantiateViewController(withIdentifier: "profile") as! ProfileContoller
        
        if Thread.isMainThread {
            viewcontroller.modalPresentationStyle = .fullScreen;
            self.getCurrentViewController().present(viewcontroller, animated: true);
        }else {
            DispatchQueue.main.async {
                viewcontroller.modalPresentationStyle = .fullScreen;
                self.getCurrentViewController().present(viewcontroller, animated: true);
            }
        }
    }
}

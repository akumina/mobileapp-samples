//
//  ViewController.swift
//  akumina-ios-sample
//
//  Created by Mac on 19/04/23.
//

import UIKit
import AkuminaAuthiOSLib

class ViewController: UIViewController {
    
    public static let MSAL_SCOPES: [String] = ["https://graph.microsoft.com/.default"];
    
    public static let TENENT_URL: String = "https://mainapp.akumina.dev/api/v2.0/token";
    
    public static var TENENT_ID : String = "15d05f6e-046b-4ed5-9ab8-4b6c25f719b5";
    
    public static var SHAREPOINT_SCOPE: String  = "https://akuminadev.sharepoint.com/.default";
    
    public static var AUTHORITY: String = "https://login.microsoftonline.com/15d05f6e-046b-4ed5-9ab8-4b6c25f719b5";
    
    
    public static let APP_MANAGER_URL: String = "https://mainapp.akumina.dev/api/v2.0/token/preauth";
    
    public static  let CLIENT_ID: String = "b86cf6b1-745b-47ce-a3c1-912f7ee3d8ac"
    
    public static let REDIRECT_URL = "msauth.com.mobile.akumina.sample.ios://auth"
    
    @IBOutlet weak var errorTxt: UITextView!
    
    @IBAction func msalButtonClicked(_ sender: Any) {
        self.doSign(mamLogin: false);
    }
    
    @IBAction func mamButtonClicked(_ sender: Any) {
        self.doSign(mamLogin: true);
    }
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    
    func doSign(mamLogin: Bool){
        do {
            var akuminaLib:  AkuminaLib = AkuminaLib.instance;
            
            var clientDetails = try ClientDetails(authority: ViewController.AUTHORITY, clientId:
                                                    ViewController.CLIENT_ID, redirectUri:
                                                    ViewController.REDIRECT_URL, scopes:ViewController.MSAL_SCOPES, sharePointScope: ViewController.SHAREPOINT_SCOPE, appManagerURL: ViewController.APP_MANAGER_URL, tenantId: ViewController.TENENT_ID)
            if(mamLogin) {
                
                try  akuminaLib.authenticateWithMSALAndMAM(parentViewController: self, clientDetails: clientDetails, completionHandler: { result in
                    self.handleResponse(result: result);
                    
                },loggingHandler: {message, error in
                    self.handleLogs(message: message, error: error);
                });
            }else {
                
                try  akuminaLib.authenticateWithMSAL(parentViewController: self, clientDetails: clientDetails, completionHandler: { result in
                    self.handleResponse(result: result)
                },loggingHandler: { message, error in
                    self.handleLogs(message: message, error: error)
                })
            }
            
        }catch{
            print(error);
        }
    }
    
    func handleLogs(message: String, error: Bool) {
        print(message);
    }
    func handleResponse(result: MSALResponse) {
        if (result.error != nil) {
            let errorMsg = "Error while Auth \(String(describing: result.error))"
            
            errorTxt.text = errorMsg;
            print(errorMsg)
            return;
            
        }
        guard result.token != "" else {
            errorTxt.text = "Token Not Found";
            print("Token Not Found")
            return;
            
        }
        print(result.token)
        Constants.TOKEN = result.token;
        showWeb();
    }
    
    
     func showWeb() {
            let mainView = UIStoryboard(name: "WebView", bundle: nil);
            let viewcontroller : WebViewController = mainView.instantiateViewController(withIdentifier: "webView") as! WebViewController
            
            if Thread.isMainThread {
                viewcontroller.modalPresentationStyle = .fullScreen;
                self.present(viewcontroller, animated: true);
            }else {
                DispatchQueue.main.async {
                    viewcontroller.modalPresentationStyle = .fullScreen;
                    self.present(viewcontroller, animated: true);
                }
            }
        }
}


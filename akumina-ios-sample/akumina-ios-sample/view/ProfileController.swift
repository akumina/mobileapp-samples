//
//  ProfileController.swift
//  akumina-ios-sample
//
//  Created by Mac on 05/05/23.
//

import Foundation
import UIKit
import AkuminaAuthiOSLib

class ProfileContoller: UIViewController {
    
    @IBOutlet weak var displayNameTxt: UITextField!
    
    @IBOutlet weak var givenNameTxt: UITextField!
    @IBOutlet weak var errorTxt: UITextField!
    
    @IBOutlet weak var jobTitleTxt: UITextField!
    
    @IBOutlet weak var principalTxt: UITextField!
    @IBOutlet weak var mailTxt: UITextField!
    @IBOutlet weak var surNameTxt: UITextField!
    @IBAction func homeBtnClicked(_ sender: Any) {
        
        UIUtils.showHome();
    }
    
    func parse(jsonData: Data) {
        do {
            let decoder = JSONDecoder()
            let profile: Profile = try decoder.decode(Profile.self, from: jsonData);
            if Thread.isMainThread {
                self.display(profile: profile)
            }else {
                DispatchQueue.main.async {
                    self.display(profile: profile)
                }
            }
        }catch{
            let errMsg = "Error \(error)";
            errorTxt.text = errMsg;
        }
    }
    
    func display(profile: Profile) {
        displayNameTxt.text = profile.displayName
        givenNameTxt.text = profile.givenName
        jobTitleTxt.text = profile.jobTitle
        surNameTxt.text = profile.surname
        mailTxt.text = profile.mail
        principalTxt.text = profile.userPrincipalName
    }
    override func viewDidAppear(_ animated: Bool) {
        do {
            let token =  try AkuminaLib.instance.getToken(type: TokenType.GRAPH);
            let endPoint = "https://mainapp.akumina.dev/api/graph/graphquery";
            var query: Dictionary = Dictionary<String,String>();
            query["queryUrl"]  = Constants.QUERY_URL;
            query["headers"] = "{}";
            query["cachekey"] =  "null";
            print("Calling api with token " + token );
            try AkuminaLib.instance.callAkuminaAPI(endPoint: endPoint, method: "get", accessToken: token, query: query) { [self] result, data, error in
                print( data as Any);
                if (result) {
                    
                    let json = (try? JSONSerialization.jsonObject(with: data!, options: [])) as? [String:AnyObject]
                    var msg = json!["Message"] as! String
                    let success = json!["Success"] as! Bool
                    let statusCode = json!["StatusCode"]
                    print(success)
                    if(success == true) {
                        let rawDataJSON = json!["RawData"] as! String;
                        let jsonData = rawDataJSON.data(using: .utf8)!
                        parse(jsonData: jsonData);
                        
                    }else {
                        errorTxt.text = msg;
                    }
                    
                    
                }else {
                    let errMsg = "Failed to execute  \(String(describing: error))";
                    errorTxt.text = errMsg;
                }
            }
        }catch{
            UIUtils.showError(error: error);
        }
    }
    
    struct Profile: Decodable {
        let displayName: String?;
        let businessPhones: [String]?;
        let givenName: String?;
        let jobTitle: String?;
        let mail: String?;
        let mobilePhone: String?;
        let officeLocation: String?;
        let preferredLanguage: String?;
        let surname: String?;
        let userPrincipalName: String?;
        let id: String?;
    }
}

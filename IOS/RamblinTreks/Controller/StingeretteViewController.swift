//
//  StingeretteViewController.swift
//  RamblinTreks
//
//  Created by FNU Richard on 4/23/18.
//  Copyright © 2018 cookiesNcream. All rights reserved.
//

import UIKit
import WebKit
class ViewController: UIViewController, WKUIDelegate {
    
    var webView: WKWebView!
    
    override func loadView() {
        super.loadView()
        let webConfiguration = WKWebViewConfiguration()
        webView = WKWebView(frame: .zero, configuration: webConfiguration)
        webView.uiDelegate = self
        view = webView
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let myURL = URL(string: "https://www.stingerette.com")
        let myRequest = URLRequest(url: myURL!)
        webView.load(myRequest)
    }
    

}

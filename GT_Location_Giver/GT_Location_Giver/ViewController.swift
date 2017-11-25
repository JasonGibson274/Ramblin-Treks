//
//  ViewController.swift
//  GT_Location_Giver
//
//  Created by Varun Giridhar on 11/23/17.
//  Copyright © 2017 VarunCorp. All rights reserved.
//

import UIKit
import CoreLocation

class ViewController: UIViewController, CLLocationManagerDelegate {
    
    private var locman = CLLocationManager()
    private var startTime: Date?
    
    @IBAction func startSending(_ sender: Any) {
        locman.startUpdatingLocation()
    }
    
    @IBAction func stopSending(_ sender: Any) {
        locman.stopUpdatingLocation()
    }
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let loc = locations.last else {return}
        
        let time = loc.timestamp
        
        guard let startTime = startTime else {
            self.startTime = time
            return
        }
        
        let elapsed = time.timeIntervalSince(startTime)
        
        if elapsed > 30 {
            print("Upload updated location to server")
            
//            updateUser(location: loc)
//
//            startTime = time
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}


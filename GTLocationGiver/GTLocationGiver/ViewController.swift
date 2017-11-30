//
//  ViewController.swift
//  GTLocationGiver
//
//  Created by Varun Giridhar on 11/25/17.
//  Copyright © 2017 VarunCorp. All rights reserved.
//

import UIKit
import CoreLocation

class ViewController: UIViewController, CLLocationManagerDelegate {
    private var locationManager = CLLocationManager()
    private var startTime: Date?
    var timer = Timer()
    var count = 0
    
    @IBAction func sendLocation(_ sender: Any) {
        scheduleTimerWithTimeInterval()
    }
    
    func scheduleTimerWithTimeInterval() {
        timer = Timer.scheduledTimer(timeInterval: 0.05, target: self, selector: #selector(self.doLocationManagerStuff), userInfo: nil, repeats: true)
    }
    
    @objc func doLocationManagerStuff() {
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
    }
    
    @IBAction func stopSendingLocation(_ sender: Any) {
        locationManager.stopUpdatingLocation()
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.first {
            let latitudeText = String(format: "%f", location.coordinate.latitude)
            let longitudeText = String(format: "%f", location.coordinate.longitude)
            
            sendData(latitude: latitudeText, longitude: longitudeText)
        }
//        guard let loc = locations.last else {return}
//
//        let time = loc.timestamp
//
//        guard let startTime = startTime else {
//            self.startTime = time
//            return
//        }
//
//        let elapsed = time.timeIntervalSince(startTime)
//
//        if elapsed > 5 { //time interval > 5 seconds
//            print("upload updated location to server")
//            print(loc)
//            let locString = String(describing: loc)
//
//            sendPostRequest(postString: locString)
//
//            //send "location: loc" to your endpoint
//            //set startTime = time to current timestamp
//        }
    }
    
    func sendData(latitude: String!, longitude: String!) {
        count = count + 1
        print(count)
        print("latitude" + latitude)
        print("longitude" + longitude)
        let urlString = "http://128.61.127.211:8080/data"
        
        let urlWithParams = urlString + "?latitude=\(latitude!)" + "&longitude=\(longitude!)"
        
        let myUrl = URL(string: urlWithParams)
        
        var request = URLRequest(url: myUrl!)
        
        request.httpMethod = "GET"
        
        let task = URLSession.shared.dataTask(with: request) { (data, response, error) in
        }
        
        task.resume()
        
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


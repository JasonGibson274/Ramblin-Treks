//
//  ViewController.swift
//  RamblinTreks
//
//  Created by FNU Richard on 1/23/18.
//  Copyright © 2018 cookiesNcream. All rights reserved.
//

import UIKit
import GoogleMaps

class MapViewController: UIViewController {
    
    //UI elements outlets
    @IBOutlet weak var hamMenu: UIButton!
    @IBOutlet weak var currentLocationTextField: UITextField!
    @IBOutlet weak var destinationTextField: UITextField!
    
    //Google Map View Outlet
    @IBOutlet weak var mapView: GMSMapView!
    
    //Location Manager to get current location
    let locationManager = CLLocationManager()
    
    //Building Model
    struct Building {
        let name : String
        let address : String
        let latitude : String
        let longitude : String
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        
//        loadBuildings()
        //Gatech location
        let gaTech = GMSCameraPosition.camera(withLatitude: 33.7756, longitude: -84.3963, zoom: 14)
        //Set map to focus on gatech area
        mapView.camera = gaTech
        
        //Add my current location button
        mapView.isMyLocationEnabled = true
        mapView.settings.myLocationButton = true
        
        let completion = {(result: NSString) in print(result)}
        getPath(startLatitude: "33.7768361", startLongitude: "-84.3897006", endLatitude: "33.776995", endLongitude: "-84.397009", completionHandler: completion)
        
        print("********************************************")
        print("********************************************")
        print("********************************************")
        print("COMPLETION")
        print(completion)
        print("********************************************")
        print("********************************************")
        print("********************************************")
    }
    
    func loadBuildings() {
        guard let url = URL(string: "https://m.gatech.edu/api/gtplaces/buildings") else {return}
        
        let task = URLSession.shared.dataTask(with: url) { (data, response, error) in
            if error != nil {
                print("Error")
            } else {
                if let mydata = data {
                    do {
                        let myJson = try JSONSerialization.jsonObject(with: mydata) as! [String: Any]
                        print(type(of: myJson))
                    } catch {
                        
                    }
                }
            }
        }
        task.resume()
    }
    
    func getPath(startLatitude: String!, startLongitude: String!, endLatitude: String!, endLongitude: String!, completionHandler: @escaping (NSString) -> ()) {
        let urlString = "http://jasongibson274.hopto.org:9003/pathing"
        
        let parameters = ["startLatitude": startLatitude, "startLongitude": startLongitude, "endLatitude": endLatitude, "endLongitude": endLongitude]
        
        let myUrl = URL(string: urlString)
        
        var request = URLRequest(url: myUrl!)
        
        request.httpMethod = "POST"
        
        let httpBody = try? JSONSerialization.data(withJSONObject: parameters, options: [])
        
        request.httpBody = httpBody
        
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        
        let task = URLSession.shared.dataTask(with: request) { (data, response, eror) in
            if let response = response {
//                print(response)
            }
            
            if let data = data {
                do {
                    let json = try JSONSerialization.jsonObject(with: data, options: [])
                    print(json)
                    let responseString = NSString(data: data, encoding: String.Encoding.utf8.rawValue)
                    completionHandler(responseString!)
                } catch {
                    print(error)
                }
            }
        }
        
        task.resume()
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}


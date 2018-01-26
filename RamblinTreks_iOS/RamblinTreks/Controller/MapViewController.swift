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
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        //Gatech location
        let gaTech = GMSCameraPosition.camera(withLatitude: 33.7756, longitude: -84.3963, zoom: 14)
        //Set map to focus on gatech area
        mapView.camera = gaTech
        
        //Add my current location button
        mapView.isMyLocationEnabled = true
        mapView.settings.myLocationButton = true
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}


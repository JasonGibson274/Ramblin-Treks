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

    @IBOutlet weak var hamMenu: UIButton!
    @IBOutlet weak var currentLocationTextField: UITextField!
    @IBOutlet weak var destinationTextField: UITextField!
    @IBOutlet weak var mapView: GMSMapView!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        mapView.bringSubview(toFront: hamMenu)
        mapView.bringSubview(toFront: currentLocationTextField)
        mapView.bringSubview(toFront: destinationTextField)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}


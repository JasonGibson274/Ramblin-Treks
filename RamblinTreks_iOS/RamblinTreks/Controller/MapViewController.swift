//
//  ViewController.swift
//  RamblinTreks
//
//  Created by FNU Richard on 1/23/18.
//  Copyright © 2018 cookiesNcream. All rights reserved.
//

import UIKit
import GoogleMaps
import Alamofire
import SwiftyJSON

class MapViewController: UIViewController, CLLocationManagerDelegate {
    
    //Outlets for hamburger menu
    @IBOutlet weak var mainViewTrailingConstraint: NSLayoutConstraint!
    @IBOutlet weak var mainViewLeadingConstraint: NSLayoutConstraint!
    @IBOutlet weak var mainView: UIView!
    
    
    //UI elements outlets
    @IBOutlet weak var startingPointTextField: UITextField!
    @IBOutlet weak var destinationTextField: UITextField!
    
    //Google Map View Outlet
    @IBOutlet weak var mapView: GMSMapView!
    
    //Location Manager to get current location
    var locationManager = CLLocationManager()
    var userCurrentLocation = CLLocation()
    var route  = [Coordinate]()
    
    //List of Buildings
    var buildings = [Building]()
   
    //Flag for hamburger Menu
    var hamburgerMenuIsVisible = false
    
    var navigationMode = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.

        //Set up the  location manager
        locationManager = CLLocationManager()
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestAlwaysAuthorization()
        locationManager.distanceFilter = 50
        locationManager.startUpdatingLocation()
        locationManager.delegate = self
        
        //Gatech location
        let gaTechArea = GMSCameraPosition.camera(withLatitude: 33.7756, longitude: -84.3963, zoom: 14)
        //Set map to focus on gatech area
        mapView.camera = gaTechArea
        
        
        //NETWORKING JSON TEST
        fetchPath()
        //followUser()
        
        //BUILDINGS JSON TEST
        fetchBuildings()
        
//        for b in buildings {
//            print(b.name)
//        }
//
        //Add my current location button
        mapView.isMyLocationEnabled = true
        mapView.settings.myLocationButton = true
        
    }
    
    
    //MARK: - Hamburger Menu
    /***************************************************************/
    @IBAction func hamburgerMenuButtonTapped(_ sender: Any) {
        if !hamburgerMenuIsVisible{
            mainViewLeadingConstraint.constant = 200
            mainViewTrailingConstraint.constant = 200
            
            hamburgerMenuIsVisible = true
            mainView.isUserInteractionEnabled = false
            
        } else {
            mainViewLeadingConstraint.constant = 0
            mainViewTrailingConstraint.constant = 0
            
            hamburgerMenuIsVisible = false
            mainView.isUserInteractionEnabled = true
        }
    }
    
    
    
    //MARK: - Networking
    /***************************************************************/
    
    struct CustomPostEncoding: ParameterEncoding {
        func encode(_ urlRequest: URLRequestConvertible, with parameters: Parameters?) throws -> URLRequest {
            var urlRequest = try urlRequest.asURLRequest()
            
            let data = try JSONSerialization.data(withJSONObject: parameters!, options: [])
            
//            if urlRequest.value(forHTTPHeaderField: "Content-Type") == nil {
//                urlRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
//            }
            
            urlRequest.httpBody = data
            
            return urlRequest
        }
    }
    
    //Write the getWeatherData method here:
    func fetchPath() {
        let url : String = "http://jasongibson274.hopto.org:9003/pathing"
        let params : [String : String] = ["startLatitude": "33.7768361", "startLongitude" : "-84.3897006", "endLatitude" : "33.776995", "endLongitude" : "-84.397009"]
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Accept": "application/json"
        ]
        
        Alamofire.request(url, method: .post, parameters: params, encoding: CustomPostEncoding(), headers: headers).responseJSON {
            respose in
            if respose.result.isSuccess {
                
                print("Alamofire succeded to get path data")
                let pathJSON : JSON = JSON(respose.result.value!)
                print("///////////////////")
                print(pathJSON.count)

                
                self.getPath(json: pathJSON)
                
                
            } else {
                print("Error\(String(describing: respose.result.error))")
            }
        }
    }
    
    
    
    //MARK: - JSON Parsing
    /***************************************************************/
    func getPath(json: JSON) {
        //path = Array(count: json.count)
        for(key,subJson):(String, JSON) in json {
            if (key != "orientation" && key != "estimate") {
                route.append(Coordinate(index: Double(key)! , longitude: subJson["longitude"].doubleValue, latitude: subJson["latitude"].doubleValue))
            }
//            print(key)
            //print(i, route[i].longitude, route[i].latitude)
            //i = i + 1
        }
//        print(route.count)
    }
    
    //MARK: - JSON Parsing for Building Names
    func fetchBuildings() {
        let url: String = "https://m.gatech.edu/api/gtplaces/buildings"
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Accept": "application/json"
        ]
        
        Alamofire.request(url, method: .get, headers: headers).responseJSON { (response) in
            if response.result.isSuccess {
                print("Alamofire succeeded to get buildings data")
                
                
                let buildingsJSON = JSON(response.result.value!)
                
                self.getBuildings(json: buildingsJSON)
                
                print("**********************************************")
                
            } else {
                print("Error\(String(describing: response.result.error))")
            }
        }
    }
    
    //MARK: - JSON Parsing for Getting Buildings
    func getBuildings(json: JSON) {
        for (index, subJson):(String,JSON) in json {
//            let name = subJson["name"].stringValue
//            buildings.append(Building(name: subJson["name"].stringValue, address: subJson["address"].stringValue, latitude: subJson["latitude"].stringValue, longitude: subJson["longitude"].stringValue))
            
            print(subJson["name"].stringValue)
        }
        
    }
    
    
    
    
    //MARK: - Route Drawing
    /***************************************************************/
    @IBAction func drawPath(_ sender: Any) {
        let path = GMSMutablePath()
        
        let sortedRoute = route.sorted(by: { $0.index < $1.index })
        
        
        for coordinate in sortedRoute {
            print(coordinate.index ,coordinate.latitude, coordinate.longitude)
            path.add(CLLocationCoordinate2D(latitude: coordinate.latitude, longitude: coordinate.longitude))
        }
        
        let polyline = GMSPolyline(path: path)
        polyline.strokeWidth = 2
        polyline.geodesic = true
        polyline.map = mapView
    }
    
    
    
    
    
    //MARK: - UI Updates
    /***************************************************************/
    
    
    
    
    
    
    
    //MARK: - Location Manager Delegate Methods
    /***************************************************************/
    
    func followUser() {
        let userCamera = GMSCameraPosition.camera(withLatitude: userCurrentLocation.coordinate.latitude, longitude: userCurrentLocation.coordinate.longitude, zoom: 190, bearing: userCurrentLocation.course, viewingAngle: 45)
        //let userUpdateCamera = GMSCameraUpdate.setCamera(userCamera)
        mapView.animate(to: userCamera)

    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let location = locations[locations.count - 1]
        if location.horizontalAccuracy > 0 {
            locationManager.stopUpdatingLocation()
            print("Longitude = \(location.coordinate.longitude), Latitude = \(location.coordinate.latitude)")
        }

        userCurrentLocation = locations.last!
        let userCamera = GMSCameraPosition.camera(withLatitude: userCurrentLocation.coordinate.latitude, longitude: userCurrentLocation.coordinate.longitude, zoom: 50, bearing: userCurrentLocation.course, viewingAngle: 45)
        //let userUpdateCamera = GMSCameraUpdate.setCamera(userCamera)
        mapView.animate(to: userCamera)


    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateHeading newHeading: CLHeading) {

    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print(error)
        //tell user there is an error in retriving location
        //TODO: Location unavalable alert
    }

    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

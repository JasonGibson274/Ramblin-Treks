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

class MapViewController: UIViewController, CLLocationManagerDelegate, UITableViewDataSource, UITableViewDelegate, UISearchResultsUpdating{
    
    //Outlets for hamburger menu
    @IBOutlet weak var mainViewTrailingConstraint: NSLayoutConstraint!
    @IBOutlet weak var mainViewLeadingConstraint: NSLayoutConstraint!
    @IBOutlet weak var mainView: UIView!
    
    @IBOutlet weak var searchViewLeadingConstant: NSLayoutConstraint!
    @IBOutlet weak var searchViewTrailingConstant: NSLayoutConstraint!

    //Outlets for Building Search Menu
    @IBOutlet weak var destinationLabel: UIButton!
    @IBOutlet weak var tableView: UITableView!


    //Google Map View Outlet
    @IBOutlet weak var mapView: GMSMapView!
    
    //Location Manager to get current location
    var locationManager = CLLocationManager()
    var userCurrentLocation = CLLocation()
    var route  = [Cooridnate]()
    var naviOrientation: Double!
    
    //DataModel Array
    var buildings = [Building]()
    
    //Flag for hamburger Menu
    var hamburgerMenuIsVisible = false
    
    
    //Variables for destination building search Menu
    var destinationLongitude: String = ""
    var destinationLatitude: String = ""
    var searchMenuIsVisible = false
    var filteredBuildings = [Building]()
    let searchController = UISearchController(searchResultsController: nil)
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        //set up tabelview
        
        filteredBuildings = buildings
        
        searchController.searchResultsUpdater = self
        searchController.dimsBackgroundDuringPresentation = false
        definesPresentationContext = true
        tableView.tableHeaderView = searchController.searchBar
        
        self.tableView.register(UITableViewCell.self, forCellReuseIdentifier: "cell")
        
        
        //tempData = ["Waffle House", "Wardlaw Center", "W.H.Emerson Building"]
        
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
        
        //mapView.layer.zPosition = 1;
        //NETWORKING JSON TEST
        
        //fetchPath()
        fetchBuildings()
        //followUser()
        
        
        //Add my current location button
        mapView.isMyLocationEnabled = true
        mapView.settings.myLocationButton = true
        
    }
    
    @IBAction func strigeretteButtonPressed(_ sender: Any) {
        UIApplication.shared.open(URL(string: "https://gt-new.ridecell.com/request")! as URL, options: [:], completionHandler: nil)
    }
    
    //MARK: - Building Search Menu
    /***************************************************************/
    @IBAction func destinationLabelPressed(_ sender: Any) {
        if !searchMenuIsVisible{
            mainViewLeadingConstraint.constant = 400
            mainViewTrailingConstraint.constant = 400
            
            searchMenuIsVisible = true
            mainView.isUserInteractionEnabled = false
            
        } else {
            mainViewLeadingConstraint.constant = 0
            mainViewTrailingConstraint.constant = 0
            
            searchMenuIsVisible = false
            mainView.isUserInteractionEnabled = true
        }
    }
    
    func updateSearchResults(for searchController: UISearchController) {
        // If we haven't typed anything into the search bar then do not filter the results
        if searchController.searchBar.text! == "" {
            filteredBuildings = buildings
        } else {
            // Filter the results
            filteredBuildings = buildings.filter{ $0.name.lowercased().contains(searchController.searchBar.text!.lowercased()) }
        }
        
        self.tableView.reloadData()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.filteredBuildings.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell     {
        let cell: UITableViewCell = UITableViewCell(style: UITableViewCellStyle.subtitle, reuseIdentifier: "cell")
        
        cell.textLabel?.text = self.filteredBuildings[indexPath.row].name
        //cell.detailTextLabel?.text = self.filteredBuildings[indexPath.row].size
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("Row \(indexPath.row) selected")
        print(filteredBuildings[indexPath.row].name)
        print(filteredBuildings[indexPath.row].longitude)
        print(filteredBuildings[indexPath.row].latitude)
        destinationLatitude = "\(filteredBuildings[indexPath.row].latitude)"
        destinationLongitude = "\(filteredBuildings[indexPath.row].longitude)"
        print(destinationLongitude)
        print(destinationLatitude)
        destinationLabel.setTitle(filteredBuildings[indexPath.row].name, for: [] )
        mainViewTrailingConstraint.constant = 0
        mainViewLeadingConstraint.constant = 0
        searchMenuIsVisible = false
        tableView.isHidden = true
        mainView.isUserInteractionEnabled = true
        searchController.isActive = false
        fetchPath()

    }
    
    //MARK: - Hamburger Menu
    /***************************************************************/
    @IBAction func hamburgerMenuButtonTapped(_ sender: Any) {
        if !hamburgerMenuIsVisible{
            mainViewLeadingConstraint.constant = 200
            mainViewTrailingConstraint.constant = 200
            searchViewLeadingConstant.constant = 200
            searchViewTrailingConstant.constant = 200
            
            hamburgerMenuIsVisible = true
            mainView.isUserInteractionEnabled = false
            
        } else {
            mainViewLeadingConstraint.constant = 0
            mainViewTrailingConstraint.constant = 0
            searchViewLeadingConstant.constant = 0
            searchViewTrailingConstant.constant = 0
            
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
    
    //HTTP Request with Alamofire
    func fetchPath() {
        let url : String = "http://jasongibson274.hopto.org:9003/pathing"
        let params : [String : String] = ["startLatitude": "33.776995", "startLongitude" : "-84.397009", "endLatitude" :destinationLatitude, "endLongitude" : destinationLongitude]
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
    
    func fetchBuildings() {
        let url : String = "https://m.gatech.edu/api/gtplaces/buildings"
        let headers: HTTPHeaders = [
            "Accept": "text/html"
        ]
        Alamofire.request(url, method: .get, encoding: URLEncoding.default, headers: headers).responseJSON {
            response in
            switch response.result{
            case .success(let value):
                let buildingJSON = JSON(value)
                print("Alamofire succeded to get building data")
                self.parseBuilding(json: buildingJSON)
                
            case .failure(let error):
                print(error)
            }
        }
        
    }
    
    
    //MARK: - JSON Parsing
    /***************************************************************/
    func getPath(json: JSON) {
        //path = Array(count: json.count)
        naviOrientation = 360 - json["orientation"].doubleValue
        
        for(key,subJson):(String, JSON) in json {
            if (key == "status") {return}
            if (key != "orientation" && key != "estimate") {

                route.append(Cooridnate(index: Double(key)! , longitude: subJson["longitude"].doubleValue, latitude: subJson["latitude"].doubleValue, color: subJson["color"].stringValue))
            }
        }
        print(route.count)
        print("orientation is ", naviOrientation)
    }
    
    //Parse buildingJSON
    func parseBuilding(json: JSON) {
        for (_, subJson):(String, JSON) in json {
            buildings.append(Building(name: subJson["name"].stringValue, longitude: subJson["longitude"].stringValue, latitude: subJson["latitude"].stringValue))
            
        }
        buildings.sort {
            $0.name < $1.name
        }
        print("Buildings count: ", buildings.count)
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
        
        let userCamera = GMSCameraPosition.camera(withLatitude: userCurrentLocation.coordinate.latitude, longitude: userCurrentLocation.coordinate.longitude, zoom: 20, bearing: naviOrientation, viewingAngle: 50)
        mapView.animate(to: userCamera)
    }
    
    
    //MARK: - Location Manager Delegate Methods
    /***************************************************************/
    
    func followUser() {
        let userCamera = GMSCameraPosition.camera(withLatitude: userCurrentLocation.coordinate.latitude, longitude: userCurrentLocation.coordinate.longitude, zoom: 190, bearing: naviOrientation, viewingAngle: 45)
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

    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateHeading newHeading: CLHeading) {

    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print(error)

    }

    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
}


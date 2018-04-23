//
//  Route.swift
//  RamblinTreks
//
//  Created by FNU Richard on 2/18/18.
//  Copyright © 2018 cookiesNcream. All rights reserved.
//

import Foundation
import GoogleMaps

struct Path: Codable {
    let geoPoint: GeoPoint
    let orientation: Float
    
    static func getUrlString() -> String {
        return "http://jasongibson274.hopto.org:9003/pathing"
    }
    
    static func drawPath(startLatitude: String!, startLongitude: String!, endLatitude: String!, endLongitude: String!, completionHandler: @escaping ([String: Path]?, Error?) -> Void){
        //        let path = GMSMutablePath()
        //        path.add(CLLocationCoordinate2D(latitude: 33.776804, longitude: -84.404577))
        //        path.add(CLLocationCoordinate2D(latitude: 33.776515, longitude: -84.395318))
        //
        //
        //        let rectangle = GMSPolyline(path: path)
        //        rectangle.map = mapView
        
        
        
        
        // set up URLRequest with URL
        let urlString = getUrlString()
        // Set up Parameters of start and end points
        let parameters = ["startLatitude": startLatitude, "startLongitude": startLongitude, "endLatitude": endLatitude, "endLongitude": endLongitude]
        
        guard let url = URL(string: urlString) else {
            print("Error: cannot create URL")
            let error = BackendError.urlError(reason: "Could not construct URL")
            completionHandler(nil, error)
            return
        }
        //Set up URL Request with Parameter inside body
        var urlRequest = URLRequest(url: url)
        
        urlRequest.httpMethod = "POST"
        
        let httpBody = try? JSONSerialization.data(withJSONObject: parameters, options: [])
        
        urlRequest.httpBody = httpBody
        
        urlRequest.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        // Make request
        let session = URLSession.shared
        let task = session.dataTask(with: urlRequest, completionHandler: {
            (data, response, error) in
            // handle response to request
            // check for error
            guard error == nil else {
                completionHandler(nil, error!)
                return
            }
            // make sure we got data in the response
            guard let responseData = data else {
                print("Error: did not receive data")
                let error = BackendError.objectSerialization(reason: "No data in response")
                completionHandler(nil, error)
                return
            }
            
            // parse the result as JSON
            // then create a Todo from the JSON
            let decoder = JSONDecoder()
            do {
                let path = try decoder.decode([String: Path].self, from: responseData)
                completionHandler(path, nil)
            } catch {
                print("error trying to convert data to JSON")
                print(error)
                completionHandler(nil, error)
            }
        })
        task.resume()
    }
}


struct GeoPoint: Codable {
    
    let color: String
    let latitude: CLLocationDegrees
    let longitude: CLLocationDegrees
}


enum BackendError: Error {
    case urlError(reason: String)
    case objectSerialization(reason: String)
}





//
//  Path.swift
//  RamblinTreks
//
//  Created by FNU Richard on 3/2/18.
//  Copyright © 2018 cookiesNcream. All rights reserved.
//

import Foundation

class Coordinate {
    var index : Double
    var longitude : Double
    var latitude : Double
    
    init(index : Double, longitude : Double, latitude : Double) {
        self.index = index
        self.longitude = longitude
        self.latitude = latitude
    }
}

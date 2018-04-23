//
//  Path.swift
//  RamblinTreks
//
//  Created by FNU Richard on 3/2/18.
//  Copyright © 2018 cookiesNcream. All rights reserved.
//

import Foundation

class Cooridnate {
    var index : Double
    var longitude : Double
    var latitude : Double
    var color: String
    
    init(index : Double, longitude : Double, latitude : Double, color : String) {
        self.index = index
        self.longitude = longitude
        self.latitude = latitude
        self.color = color
    }
}

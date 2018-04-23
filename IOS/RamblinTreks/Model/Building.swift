//
//  Building.swift
//  RamblinTreks
//
//  Created by Varun Giridhar on 3/30/18.
//  Copyright © 2018 cookiesNcream. All rights reserved.
//

import Foundation

class Building {
    var name: String
    var address: String
    var latitude: String
    var longitude: String
    
    init(name: String, address: String, latitude: String, longitude: String) {
        self.name = name
        self.address = address
        self.latitude = latitude
        self.longitude = longitude
    }
}

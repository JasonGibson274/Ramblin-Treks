//
//  DataCell.swift
//  RamblinTreks
//
//  Created by FNU Richard on 4/22/18.
//  Copyright © 2018 cookiesNcream. All rights reserved.
//

import UIKit

class DataCell: UITableViewCell {
    
    @IBOutlet weak var label: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    func congigureCell(text: String) {
        
        label.text = text
    }
}

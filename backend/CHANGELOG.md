## Ramblink Treks 1.0.0 (Alpha Release)

  * Substantial code clean up and streamlining
  * Deceased step size in path to make them more granular
  * Resolved issue where bus estimates would remain if the bus went offline
  * Resolved issue where routes using a bus would not get on the bus at the earliest point
  * Added Readme and changelog[]

### Known bugs

  * Bus API returns Bad Gateway error (502) on occassion
  * Pathing service uses excessive amount of memory
  * Insufficient map coverage leads abuse of bus routes
  * Bus estimates queries are bundled so a single failure will result an entire route failing
  * Locations persist forever therefore construction can lead to invalid paths

## Ramblink Treks 0.0.3

  * Changed API used for getting bus estimates
    - Arrival times are fully working
  * Can search for path using buses and walking routes
  * Added support for returning colors for different bus routes

### Known bugs

  * Paths are not logical or followable, tend to go through buildings
  * Bus estimates are not cleared when the bus is not running
  * Bus routes will not get on bus at earliest time point

## Ramblink Treks 0.0.2

  * Can search for a path through walking locations only
  * Util package added for convesion from lat/lon to meters
    - Distances are now in meters
  * Started pulling in bus information

### Known bugs

  * Bus locations are often completely inaccurate

## Ramblink Treks 0.0.1

  * Implemented basic functionality
    - Can upload locations to be used in pathing
    - Fully supported docker-compose start
    - Created separate services for pathing and data

### Known bugs

  * Distances are in lat/lon degrees

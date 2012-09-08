package org.wololo.wastelands.core.scenario
import org.wololo.wastelands.core.Coordinate
import org.wololo.wastelands.core.map.TileMap

class Scenario(var players: Int) {
  val tileMap = new TileMap()
  var units = Map[Int, Unit]()
  val startLocations = Vector[Coordinate](new Coordinate(5,5), new Coordinate(60,60))
}
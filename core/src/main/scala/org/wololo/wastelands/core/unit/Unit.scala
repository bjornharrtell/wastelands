package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import org.wololo.wastelands.vmlayer.Sound

abstract class Unit(map2: GameMap, player2: Int, position2: Coordinate) extends AbstractUnit(map2, player2, position2) with Movable with Combatable with Selectable {

}

abstract class AbstractUnit(val map: GameMap, val player: Int, val position: Coordinate) {

  var isOnScreen = false
  val ScreenBounds: Rect = (0, 0, 0, 0)

  var direction = Direction(0, 0)

  var visible = true
  
  var alive = true
  
  def tick() {
    
  }
  
}
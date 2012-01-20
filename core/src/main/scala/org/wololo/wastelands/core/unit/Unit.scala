package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import org.wololo.wastelands.vmlayer.Sound

abstract class Unit(val map: GameMap, val player: Int, val position: Coordinate)
  extends Tickable with Movable with Combatable with Selectable {
  
  var isOnScreen = false
  val ScreenBounds: Rect = (0, 0, 0, 0)

  var direction = Direction(0, 0)

  var visible = true

  var alive = true
}
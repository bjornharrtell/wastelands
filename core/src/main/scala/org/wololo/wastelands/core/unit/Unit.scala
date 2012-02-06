package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import org.wololo.wastelands.vmlayer.Sound

/**
 * Base combined implementation for units
 *
 * This class only contains helper information for renderer and collission detection.
 * Mixins provide base implementations for combined unit logic.
 */
abstract class Unit(val game: Game, val player: Int, val position: Coordinate)
  extends Tickable with Movable with Combatable with Selectable {

  var isOnScreen = false
  val ScreenBounds: Rect = (0, 0, 0, 0)
}
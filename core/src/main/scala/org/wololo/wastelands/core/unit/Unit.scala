package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import org.wololo.wastelands.core.unit.order.Move
import org.wololo.wastelands.core.unit.order.Attack
import org.wololo.wastelands.core.unit.order.Guard
import org.wololo.wastelands.core.unit.action.MoveStep
import org.wololo.wastelands.core.unit.action.Turn
import org.wololo.wastelands.core.unit.action.Fire
import java.io.File
import org.wololo.wastelands.core.event.Event
import scala.collection.mutable.ArrayBuffer

/**
 * Base abstract implementation for units
 */
abstract class Unit(val player: Player, val position: Coordinate) {
  val Velocity = 0.04
  var moveDistance = 0.0
  var direction: Direction = Direction.fromTileIndex((math.random * 7 + 1).toInt)

  val Range = 2
  val AttackStrength = 2
  var alive = true
  var hp = 10
}
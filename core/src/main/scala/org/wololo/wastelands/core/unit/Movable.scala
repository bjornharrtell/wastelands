package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._

object Movable {
  val MoveStatusIdling = 0
  val MoveStatusMoving = 1
  val MoveStatusPausing = 2
}

trait Movable {
  self: Unit =>
  
  // TODO: explain why this is needed
  import Movable._

  val Velocity = 0.04
  val MovePauseTicks = 15
  
  var destination = position.clone
  var nextDestination = destination.clone

  var moveDistance = 0.0

  var moveStatus = MoveStatusIdling
  var movePauseTicksCounter = MovePauseTicks

  def tick() {
    moveStatus match {
      case MoveStatusMoving => tickMoving
      case MoveStatusPausing => tickPausing
      case MoveStatusIdling => tickIdling
    }
  }

  private def tickMoving() {
    if (moveDistance == 0) {
      map.tiles(position).unit = None
      map.tiles(position + direction).unit = Option(this)
    }

    moveDistance += Velocity

    if (moveDistance >= 1) {
      moveDistance = 0

      position += direction

      map.removeShadeAround(position)

      moveStatus = MoveStatusPausing
    }
  }

  private def tickPausing() {
    movePauseTicksCounter += -1

    if (movePauseTicksCounter == 0) {
      movePauseTicksCounter = MovePauseTicks
      moveStatus = MoveStatusIdling
      destination.setTo(nextDestination)
      calc
    }
  }

  private def tickIdling() {
    if (nextDestination != destination) {
      destination.setTo(nextDestination)
      calc
    }
  }

  def moveTo(coordinate: Coordinate) {
    nextDestination.setTo(coordinate)
  }

  /**
   * Calculate next move.
   * 
   * TODO: use real pathfinding, for now it will simply try a direction
   */
  private def calc() {
    var delta = destination - position

    // destination reached, bail
    if (delta == (0, 0)) return

    // calculate tile directions per axis
    val dx = math.signum(delta.x)
    val dy = math.signum(delta.y)

    direction = Direction(dx, dy)

    if (map.tiles(position + direction).isOccupied) return

    moveStatus = MoveStatusMoving
  }
}


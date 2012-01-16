package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._

object Movable {
  val MoveStatusIdling = 0
  val MoveStatusMoving = 1
  val MoveStatusPausing = 2
}

trait Movable {
  self: Unit =>
  
  // TODO: why is this needed? I want an explanation :)
  import Movable._

  val Velocity = 0.04
  val MovePauseTicks = 15
  
  var tile: Option[Tile] = Option(map.tiles(position))
  tile.get.unit = Option(this)
  
  var moveDistance = 0.0
  var moveStatus = MoveStatusIdling
  
  private var destination = position.clone
  private var nextDestination = destination.clone
  private var movePauseTicksCounter = MovePauseTicks

  def tick() {
    moveStatus match {
      case MoveStatusMoving => tickMoving
      case MoveStatusPausing => tickPausing
      case MoveStatusIdling => tickIdling
    }
  }

  /**
   * Handle ticks when moving. Will freeze if unit is dead.
   * 
   * moveDistance is 0 first tick of move
   * moveDistance is >= 1 when move is complete
   */
  private def tickMoving() {
    if (moveDistance == 0) {
      // (de)associate with tile when move is initiated 
      map.tiles(position).unit = None
      tile = Option(map.tiles(position + direction))
      tile.get.unit = Option(this)
    }

    if (alive) moveDistance += Velocity

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


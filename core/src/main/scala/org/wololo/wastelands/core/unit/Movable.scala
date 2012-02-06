package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._

object Movable {
  val MoveStatusIdling = 0
  val MoveStatusMoving = 1
  val MoveStatusPausing = 2
}

/**
 * Logic that makes a unit movable
 * 
 * Default state is idle.
 * When idle, a movable unit will progress to moving state when nextDestination isn't equal to destination.
 * When moving a movable unit will initiate, tick and finish a move from one tile to an adjacent tile then progress to pausing state.
 * When pausing a movable unit will do nothing for a defined amount of ticks then progress to idle state.
 */
trait Movable extends Tickable {
  self: Unit =>
    
  import Movable._

  val Velocity = 0.04
  val MovePauseTicks = 15

  var tile: Tile = map.tiles(position)
  tile.unit = Option(this)

  // randomize initial direction
  var direction: Direction = (math.random*7+1).toInt
  
  var moveDistance = 0.0
  var moveStatus = MoveStatusIdling

  // destination is the target position of the unit 
  private var destination = position.clone
  // next destination is the queued next destination to be transitioned when unit is idle
  private var nextDestination = destination.clone
  
  private var movePauseTicksCounter = MovePauseTicks

  override def tick() : Unit = {
    moveStatus match {
      case MoveStatusMoving => tickMoving()
      case MoveStatusPausing => tickPausing()
      case MoveStatusIdling => tickIdling()
    }
    super.tick()
  }

  /**
   * Handle ticks when moving. Will freeze if unit is dead.
   *
   * moveDistance is 0 first tick of move
   * moveDistance is >= 1 when move is complete
   */
  private def tickMoving() {
    if (moveDistance == 0) init()
    if (alive) moveDistance += Velocity
    if (moveDistance >= 1) finish()
  }

  /**
   * Stuff that needs to be done when move is initiated
   */
  private def init() {
    map.removeShadeAround(position+direction)
  }
  
  /**
   * Stuff that needs to be done when move is finished
   */
  private def finish() {
    moveDistance = 0
    
    // deassociate with previous tile
    map.tiles(position).unit = None
    
    position += direction
    
    // associate with new tile
    tile = map.tiles(position)
    tile.unit = Option(this)

    moveStatus = MoveStatusPausing
  }

  private def tickPausing() {
    movePauseTicksCounter += -1

    if (movePauseTicksCounter == 0) {
      movePauseTicksCounter = MovePauseTicks
      moveStatus = MoveStatusIdling
      destination.setTo(nextDestination)
      if (calc()) map.removeShadeAround(position, true)
    }
  }

  private def tickIdling() {
    if (nextDestination != destination) {
      destination.setTo(nextDestination)
      calc()
    }
  }

  def moveTo(coordinate: Coordinate) {
    nextDestination.setTo(coordinate)
  }

  /**
   * Calculate next move.
   *
   * TODO: use real pathfinding, for now it will simply try a few directions
   * 
   * @return true if new move can be calculated false if obstructed or at destination
   */
  private def calc(): Boolean = {
    val delta = destination - position
    val isDestinationReached = delta == (0, 0)
    
    if(!isDestinationReached){
      // calculate tile directions per axis
      val dx = math.signum(delta.x)
      val dy = math.signum(delta.y)

      var testDirection = Direction(dx, dy)

      // if direction is obstructed try left/right
      if (map.tiles(position + testDirection).isOccupied) {
        testDirection = testDirection.leftOf
        if (map.tiles(position + testDirection).isOccupied) {
          testDirection = testDirection.rightOf
          if (map.tiles(position + testDirection).isOccupied) return false
        }
      }

      direction = testDirection

      moveStatus = MoveStatusMoving
    }

    isDestinationReached
  }
}


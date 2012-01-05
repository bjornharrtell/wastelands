package org.wololo.wastelands.core
import org.wololo.wastelands.vmlayer.Canvas

/**
 * TODO: probably needs refactoring for more intelligent rendering with other parts
 */
abstract class Unit(map: GameMap, var x: Int, var y: Int) {

  val MoveStatusIdle = 0
  val MoveStatusMoving = 1
  val MoveStatusPausing = 2

  var velocity = 0.04
  var direction = 0
  var destX = x
  var destY = y

  var moveDistance = 0.0

  var moveStatus = MoveStatusIdle
  val MovePauseTicks = 15
  var movePauseTicksCounter = MovePauseTicks

  map.removeShadeAround(x, y)

  def tick() {
    tickMove
  }

  def startMove() {
    // TODO: should be able to move to map limit but restricted now since it crashes
    if (x < 3 || x >= map.Width - 3 || y < 3 || y >= map.Height - 3)
      return

    // TODO: remove test code
    //direction = (Math.random * 7).toInt
    moveStatus = MoveStatusMoving
  }

  def moveTo(x: Int, y: Int) {
    destX = x
    destY = y
    calcMove
  }

  def calcMove() {
    var dx = destX - x
    var dy = destY - y

    // destination reached, bail
    if (dx == 0 && dy == 0) {
      // TODO: remove test code
      def randomPos() = { ((Math.random * (map.Width - 6)) + 3).toInt }
      moveTo(randomPos(), randomPos())

      return
    }

    dx = Math.signum(dx)
    dy = Math.signum(dy)

    if (dx == 0 && dy == -1) direction = 0
    else if (dx == 1 && dy == -1) direction = 1
    else if (dx == 1 && dy == 0) direction = 2
    else if (dx == 1 && dy == 1) direction = 3
    else if (dx == 0 && dy == 1) direction = 4
    else if (dx == -1 && dy == 1) direction = 5
    else if (dx == -1 && dy == 0) direction = 6
    else if (dx == -1 && dy == -1) direction = 7

    startMove()
  }

  /**
   * Calculate map tile delta from direction if the unit would move that way
   */
  def mapDelta(): (Int, Int) = {
    direction match {
      case 0 => (0, -1)
      case 1 => (1, -1)
      case 2 => (1, 0)
      case 3 => (1, 1)
      case 4 => (0, 1)
      case 5 => (-1, 1)
      case 6 => (-1, 0)
      case 7 => (-1, -1)
    }
  }

  def tickMove() {
    if (moveStatus == MoveStatusMoving) {
      moveDistance += velocity

      if (moveDistance >= 1) {
        moveDistance = 0

        var xf = 0
        var yf = 0

        val (dx, dy) = mapDelta

        x += dx
        y += dy

        map.removeShadeAround(x, y)
        moveStatus = MoveStatusPausing
      }
    } else if (moveStatus == MoveStatusPausing) {
      movePauseTicksCounter += -1

      if (movePauseTicksCounter == 0) {
        movePauseTicksCounter = MovePauseTicks
        moveStatus = MoveStatusIdle
        // TODO: remove test, should not be initiated here
        calcMove
      }
    }

  }
}
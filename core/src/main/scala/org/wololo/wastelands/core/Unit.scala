package org.wololo.wastelands.core
import org.wololo.wastelands.vmlayer.Canvas

/**
 * TODO: probably needs refactoring for more intelligent rendering with other parts
 */
class Unit(map: GameMap, var x: Int, var y: Int) {

  val Velocity = 0.04
  val MoveStatusIdle = 0
  val MoveStatusMoving = 1
  val MoveStatusPausing = 2

  var moveDistance = 0.0
  var direction = 0
  var destX = x
  var destY = y

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

    if (dx == 0 && dy == 0)
      return

    dx = Math.signum(dx)
    dy = Math.signum(dy)

    if (dx == 0 && dy == -1) direction = 0
    if (dx == 1 && dy == -1) direction = 1
    if (dx == 1 && dy == 0) direction = 2
    if (dx == 1 && dy == 1) direction = 3
    if (dx == 0 && dy == 1) direction = 4
    if (dx == -1 && dy == 1) direction = 5
    if (dx == -1 && dy == 0) direction = 6
    if (dx == -1 && dy == -1) direction = 7
    
    startMove()
  }

  def tickMove() {
    if (moveStatus == MoveStatusMoving) {
      moveDistance += Velocity

      if (moveDistance >= 1) {
        moveDistance = 0

        var xf = 0
        var yf = 0

        // TODO: refactor with renderer
        direction match {
          case 0 => xf = 0; yf = -1
          case 1 => xf = 1; yf = -1
          case 2 => xf = 1; yf = 0
          case 3 => xf = 1; yf = 1
          case 4 => xf = 0; yf = 1
          case 5 => xf = -1; yf = 1
          case 6 => xf = -1; yf = 0
          case 7 => xf = -1; yf = -1
        }

        x += xf
        y += yf

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
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
  var direction = Direction(0, 0)
  var destX = x
  var destY = y

  var moveDistance = 0.0

  var moveStatus = MoveStatusIdle
  val MovePauseTicks = 15
  var movePauseTicksCounter = MovePauseTicks

  var selected = false
  def select = { selected = true }
  def unselect = { selected = false }
  
  map.tiles(x, y).unit = this
  map.removeShadeAround(x, y)

  def tick() {
    tickMove()
  }

  def startMove() {
    // TODO: should be able to move to map limit but restricted now since it crashes
    if (x < 3 || x >= map.Width - 3 || y < 3 || y >= map.Height - 3)
      return

    moveStatus = MoveStatusMoving
  }

  def moveTo(x: Int, y: Int) {
    // TODO: should defer move until ready (after pause)
    
    destX = x
    destY = y
    calcMove()
  }

  def calcMove() {
    var dx = destX - x
    var dy = destY - y

    // destination reached, bail
    if (dx == 0 && dy == 0) return
    
    dx = math.signum(dx)
    dy = math.signum(dy)

    if (dx == 0 && dy == -1) direction = 0
    else if (dx == 1 && dy == -1) direction = 1
    else if (dx == 1 && dy == 0) direction = 2
    else if (dx == 1 && dy == 1) direction = 3
    else if (dx == 0 && dy == 1) direction = 4
    else if (dx == -1 && dy == 1) direction = 5
    else if (dx == -1 && dy == 0) direction = 6
    else if (dx == -1 && dy == -1) direction = 7
    
    if (map.tiles(x+dx,y+dy).isOccupied) return

    startMove()
  }

  def tickMove() {
    if (moveStatus == MoveStatusMoving) {
      if (moveDistance==0) {
        val (dx, dy) = mapDelta
        
        map.tiles(x, y).unit = null
        map.tiles(x+dx, y+dy).unit = this
      }
      
      moveDistance += velocity
      
      if (moveDistance >= 1) {
        moveDistance = 0

        val Direction(dx, dy) = direction

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
        calcMove()
      }
    }

  }
  
}
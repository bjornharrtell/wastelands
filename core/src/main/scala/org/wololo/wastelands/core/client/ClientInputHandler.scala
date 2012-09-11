package org.wololo.wastelands.core.client

import org.wololo.wastelands.core.event.Touch
import org.wololo.wastelands.core.event.Event
import org.wololo.wastelands.core.server.UnitActor
import org.wololo.wastelands.core.Coordinate

object KeyCode {
  val KEY_1: Int = 0
  val KEY_2: Int = 1
  val KEY_3: Int = 2
  val KEY_4: Int = 3
  val KEY_5: Int = 4
  val KEY_9: Int = 8
  val KEY_0: Int = 9
}

trait ClientInputHandler {
  self: Client =>

  val tolerance = 5
  var previous: Coordinate = (0, 0)
  var px = 0
  var py = 0
  var downAtX = 0
  var downAtY = 0
  var hasScrolled = false

  def touch(e: Touch) {
    e.action match {
      case Touch.DOWN => touchDown(e.x, e.y)
      case Touch.MOVE => touchMove(e.x, e.y)
      case Touch.UP => touchUp(e.x, e.y)
    }
  }

  private def touchUp(coordinate: Coordinate) {
    if (hasScrolled) return

    var clickedUnit = false

    // process out visible and clicked units
    // TODO: need to handle case where units have overlapping bounds i.e multiple hits here

    for (unit <- units) unit match {
      case (unit, state) =>
        if (state.alive && state.screenBounds.contains(coordinate)) {
          unitAction(unit)
          clickedUnit = true
        }
    }

    // no unit was clicked
    if (!clickedUnit) {
      val mx = screen.calculateTileIndex(screen.screenOffset.x + coordinate.x)
      val my = screen.calculateTileIndex(screen.screenOffset.y + coordinate.y)
      mapTileAction((mx, my))
    }
  }

  def touchMove(x: Int, y: Int) {
    if (math.sqrt(math.pow(x - downAtX, 2) + math.pow(y - downAtY, 2)).toInt > tolerance) {
      screen.scroll(px-x, py-y)
      hasScrolled = true
    }

    px = x
    py = y
  }

  private def touchDown(x: Int, y: Int) {
    hasScrolled = false

    downAtX = x
    downAtX = y
    px = x
    py = y
  }

  def keyDown(keyCode: Int) {

  }
}
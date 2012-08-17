package org.wololo.wastelands.core

import org.wololo.wastelands.core.event.Touch
import org.wololo.wastelands.core.event.Event
import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.UnitClientState

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
  self : Client =>
  
  val tolerance = 5
  var previous: Coordinate = (0, 0)
  var downAt: Coordinate = (0, 0)
  var hasScrolled = false
  
  def touch(e: Touch) {
    e.action match {
      case Touch.DOWN => touchDown(e.coordinate)
      case Touch.MOVE => touchMove(e.coordinate)
      case Touch.UP => touchUp(e.coordinate)
    }
  }

  private def touchUp(coordinate: Coordinate) {
    if (hasScrolled) return
    
    var clickedUnit = false
    
    // process out visible and clicked units
    // TODO: need to handle case where units have overlapping bounds i.e multiple hits here
    
    for (unit <- units if unit._2.alive && unit._2.screenBounds.contains(coordinate)) {
      unitAction(unit._1)
      clickedUnit = true
    }

    // no unit was clicked
    if (!clickedUnit) {
      val mx = screen.calculateTileIndex(screen.screenOffset.x + coordinate.x)
      val my = screen.calculateTileIndex(screen.screenOffset.y + coordinate.y)
      mapTileAction((mx, my))
    }
  }
  
  def touchMove(coordinate: Coordinate) {
    if (coordinate.distance(downAt)>tolerance) {
      screen.scroll(previous-coordinate)
      hasScrolled = true
    }
    
    previous = coordinate
  }

  private def touchDown(coordinate: Coordinate) {
    hasScrolled = false
    
    downAt = coordinate
    previous = coordinate
  }
  
  def keyDown(keyCode: Int) {
    
  }
}
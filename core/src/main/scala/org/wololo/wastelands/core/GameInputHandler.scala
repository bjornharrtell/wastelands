package org.wololo.wastelands.core

import org.wololo.wastelands.core.event.TouchEvent
import org.wololo.wastelands.core.event.Event

trait GameInputHandler extends Subscriber {
  self : Game =>
  
  val tolerance = 5
  var previous: Coordinate = (0, 0)
  var downAt: Coordinate = (0, 0)
  
  def notify(pub: Publisher, event: Event) {
    event match {
      case x: TouchEvent => touch(x)
      case _ =>
    }
  }
  
  def touch(e: TouchEvent) {
    e.action match {
      case TouchEvent.DOWN => touchDown(e.coordinate)
      case TouchEvent.MOVE => touchMove(e.coordinate)
      case TouchEvent.UP => touchUp(e.coordinate)
    }
  }

  def touchUp(coordinate: Coordinate) {
  }
  
  def touchMove(coordinate: Coordinate) {
    if (coordinate.distance(downAt)>tolerance) screen.scroll(previous-coordinate)
    
    previous = coordinate
  }

  def touchDown(coordinate: Coordinate) {
    var clickedUnit = false

    // process out visible and clicked units
    // TODO: need to handle case where units have overlapping bounds i.e multiple hits here
    for (unit <- units if unit.alive && unit.ScreenBounds.contains(coordinate)) {
      unitAction(unit)
      clickedUnit = true
    }

    // no unit was clicked
    if (!clickedUnit) {
      val mx = screen.calculateTileIndex(screen.screenOffset.x + coordinate.x)
      val my = screen.calculateTileIndex(screen.screenOffset.y + coordinate.y)
      mapTileAction((mx, my))
    }
    
    downAt = coordinate
    previous = coordinate
  }
}
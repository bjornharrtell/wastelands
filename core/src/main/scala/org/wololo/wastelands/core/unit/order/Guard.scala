package org.wololo.wastelands.core.unit.order

import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Order
import org.wololo.wastelands.core.TileOccupationEvent
import org.wololo.wastelands.core.Event

class Guard(unit: Unit) extends Order(unit: Unit) {
  unit.map.subscribe(unit, filter)
  
  /**
   * Filter out TileOccupationEvents that isn't for the guarding unit itself, only enemies and within range of the guarding unit
   */
  def filter(e: Event) : Boolean = {
    e match {
      case e: TileOccupationEvent => e.unit != unit && unit.player != e.unit.player && e.unit.position.distance(unit.position) <= unit.Range
      case _ => false
    }
  }
  
  override def dispose() {
    unit.map.removeSubscription(unit)
  }
}
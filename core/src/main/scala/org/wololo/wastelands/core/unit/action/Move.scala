package org.wololo.wastelands.core.unit.action
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.Game
import org.wololo.wastelands.core.TickEvent
import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.ActionCompleteEvent
import scala.collection.mutable.Publisher
import org.wololo.wastelands.core.Event

class Move(unit: Unit) extends Action(unit) {

  val map = unit.game.map
  
  init()
  
  def notify(pub: Publisher[Event], event: Event) {
    unit.moveDistance += unit.Velocity
    
    if (unit.moveDistance >= 1) finish()
  }
  
  /**
   * Stuff that needs to be done when move is initiated
   */
  private def init() {
    map.removeShadeAround(unit.position+unit.direction)
  }
  
  /**
   * Stuff that needs to be done when move is finished
   */
  private def finish() {
    unit.moveDistance = 0
    
    // deassociate with previous tile
    map.tiles(unit.position).unit = None
    
    unit.position += unit.direction
    
    // associate with new tile
    map.tiles(unit.position).unit = Option(unit)
    
    unit.game.removeSubscription(this)
    publish(new ActionCompleteEvent)
  }
}
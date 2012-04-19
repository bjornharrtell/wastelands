package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.Subscriber
import org.wololo.wastelands.core.Publisher
import org.wololo.wastelands.core.event.Event

case class ActionCompleteEvent extends Event

abstract class Action(unit: Unit) extends Publisher with Subscriber {
  type Pub = Action

  unit.game.subscribe(this)
  subscribe(unit)
  
  var shouldAbort = false 

  def notify(pub: Publisher, event: Event) {
    onTick()
  }
  
  def abort() { shouldAbort = true }
  
  def complete() {
    unit.game.removeSubscription(this)
    publish(new ActionCompleteEvent)
    removeSubscriptions()
  }
  
  def onTick()
}
package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.Game
import org.wololo.wastelands.core.TickEvent
import org.wololo.wastelands.core.Event
import org.wololo.wastelands.core.Subscriber
import org.wololo.wastelands.core.Publisher

class ActionCompleteEvent extends Event {

}

abstract class Action(unit: Unit) extends Publisher with Subscriber {
  type Pub = Action

  unit.game.subscribe(this)
  subscribe(unit)

  def notify(pub: Publisher, event: Event) {
    onTick
  }
  
  def complete() {
    unit.game.removeSubscription(this)
    publish(new ActionCompleteEvent)
    removeSubscriptions()
  }
  
  def onTick()
}
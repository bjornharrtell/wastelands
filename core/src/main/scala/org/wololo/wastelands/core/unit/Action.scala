package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.Subscriber
import org.wololo.wastelands.core.Publisher
import org.wololo.wastelands.core.event.Event

case class ActionCompleteEvent(action: Action) extends Event

abstract class Action(val order: Order, val unit: Unit) extends Publisher with Subscriber {
  type Pub = Action

  // subscribe this action to game to get tick events
  unit.game.subscribe(this)

  // subscribe unit to action
  subscribe(unit)
  
  val CooldownTicks = 30

  def notify(pub: Publisher, event: Event) {
    onTick()
  }
  
  def complete() {
    unit.game.removeSubscription(this)
    publish(new ActionCompleteEvent(this))
    removeSubscriptions()
  }
  
  def onTick()
}
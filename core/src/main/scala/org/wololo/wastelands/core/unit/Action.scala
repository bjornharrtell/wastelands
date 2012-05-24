package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.Subscriber
import org.wololo.wastelands.core.Publisher
import org.wololo.wastelands.core.event.Event

case class ActionCompleteEvent() extends Event

abstract class Action(val unit: Unit) extends Publisher with Subscriber {
  type Pub = Action

  // subscribe this action to game to get tick events
  unit.game.subscribe(this)

  // subscribe unit to action
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
    
    // TODO: a unit probably has to keep track of all cooldowns so it should probably be instantiated in unit and stored there
    new Cooldown(this)
  }
  
  def onTick()
}
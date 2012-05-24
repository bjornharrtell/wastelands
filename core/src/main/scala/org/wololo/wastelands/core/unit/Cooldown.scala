package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core.Subscriber
import org.wololo.wastelands.core.event.Event
import org.wololo.wastelands.core.Publisher

case class CooldownCompleteEvent() extends Event

class Cooldown(action: Action) extends Publisher with Subscriber {
  type Pub = Cooldown

  // subscribe this cooldown to game to get tick events
  action.unit.game.subscribe(this)

  // subscribe unit to cooldown
  subscribe(action.unit)

  // TODO: this should be a default to be overriden by actions
  var counter = 20
  
  def notify(pub: Publisher, event: Event) {
    onTick()
  }
  
  def complete() {
    action.unit.game.removeSubscription(this)
    publish(new CooldownCompleteEvent)
    removeSubscriptions()
  }
  
  def onTick() {
    counter -= 1
    if (counter == 0) complete()
  }
}
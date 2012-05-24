package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core.Subscriber
import org.wololo.wastelands.core.event.Event
import org.wololo.wastelands.core.Publisher

case class CooldownCompleteEvent(cooldown: Cooldown) extends Event

class Cooldown(val action: Action) extends Publisher with Subscriber {
  type Pub = Cooldown

  // subscribe this cooldown to game to get tick events
  action.unit.game.subscribe(this)

  // subscribe unit to cooldown
  subscribe(action.unit)

  var counter = action.CooldownTicks
  
  def notify(pub: Publisher, event: Event) {
    onTick()
  }
  
  def complete() {
    action.unit.game.removeSubscription(this)
    publish(new CooldownCompleteEvent(this))
    removeSubscriptions()
  }
  
  def onTick() {
    counter -= 1
    if (counter == 0) complete()
  }
}
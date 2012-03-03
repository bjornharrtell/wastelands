package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.Game
import scala.collection.mutable.Publisher
import org.wololo.wastelands.core.TickEvent
import scala.collection.mutable.Subscriber
import org.wololo.wastelands.core.Event

class ActionCompleteEvent extends Event {

}

abstract class Action(unit: Unit) extends Publisher[Event] with Subscriber[Event, Publisher[Event]] {
  type Pub = Action

  unit.game.subscribe(this)

  def notify(pub: Publisher[Event], event: Event)
}
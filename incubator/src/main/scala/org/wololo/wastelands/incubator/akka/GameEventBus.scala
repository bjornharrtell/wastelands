package org.wololo.wastelands.incubator.akka

import akka.event.{SubchannelClassification, EventBus}
import akka.util.Subclassification


class GameEventBus extends EventBus with SubchannelClassification{
  type Event = GameEvent
  type Subscriber = GameEntity
  type Classifier = GameEvent

  //protected implicit def subclassification =

  protected def classify(event: GameEventBus#Event) = null

  protected def publish(event: GameEventBus#Event, subscriber: GameEventBus#Subscriber) {}

  protected implicit def subclassification = null
}
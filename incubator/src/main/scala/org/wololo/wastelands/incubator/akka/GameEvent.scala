package org.wololo.wastelands.incubator.akka

import akka.util.Subclassification

object GameEventClassifier extends Subclassification[GameEvent] {
  def isEqual(x: GameEvent, y: GameEvent) = x.equals(y)

  def isSubclass(x: GameEvent, y: GameEvent) = x.isInstanceOf[GameEvent]
}

sealed trait GameEvent

case class TravelEvent(gameUnit: GameUnit, to: (Int, Int)) extends GameEvent
case class ArrivedEvent(gameUnit: GameUnit, to: (Int, Int)) extends GameEvent

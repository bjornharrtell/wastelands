package org.wololo.wastelands.core.unit
import akka.actor.ActorRef
import org.wololo.wastelands.core.Coordinate

sealed trait Order

@SerialVersionUID(3016L) case class Move(destination: Coordinate) extends Order
@SerialVersionUID(3017L) case class Attack(target: ActorRef) extends Order
@SerialVersionUID(3018L) case class Guard() extends Order
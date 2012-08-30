package org.wololo.wastelands.core
import akka.actor.ActorRef
import org.wololo.wastelands.core.unit.Unit

class PlayerGame extends Game {
  var units = Map[ActorRef, Unit]()
}


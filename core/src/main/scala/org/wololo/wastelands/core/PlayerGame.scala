package org.wololo.wastelands.core
import akka.actor.ActorRef
import org.wololo.wastelands.core.unit.Unit

/**
 * Game state for a player
 */
trait PlayerGame extends Game {

  /**
   * Map that pairs UnitActor with clientside state
   */
  var units = Map[ActorRef, Unit]()
}


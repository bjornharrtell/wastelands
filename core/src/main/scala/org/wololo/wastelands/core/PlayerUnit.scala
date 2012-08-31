package org.wololo.wastelands.core

import unit._
import akka.actor.ActorRef

/**
 * Generic concrete unit state
 * 
 * TODO: Generic unit state should probably be abstract since it needs implementation for both PCs and NPCs? If so this class is not needed 
 */
class PlayerUnit(val player: ActorRef, val game: Game, val unitType: Int, var position: Coordinate, var direction: Direction) extends Unit
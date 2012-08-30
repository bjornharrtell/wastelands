package org.wololo.wastelands.core

import unit._
import akka.actor.ActorRef

class PlayerUnit(val player: ActorRef, val game: Game, val unitType: Int, var position: Coordinate, var direction: Direction) extends Unit
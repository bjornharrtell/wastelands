package org.wololo.wastelands.core

import org.wololo.wastelands.core.unit._
import akka.actor.ActorRef

/**
 * Basic Game state
 * 
 * Intended to provide an interface to common Game state for both server and clientside
 */
trait Game {
  var players = Vector[ActorRef]()
  val map: TileMap = new TileMap()
  var ticks = 0
}


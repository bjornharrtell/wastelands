package org.wololo.wastelands.core

import org.wololo.wastelands.core.unit._
import akka.actor.ActorRef

trait Game {
  //val events = ArrayBuffer[Event]()
  var players = Vector[ActorRef]()
  val map: TileMap = new TileMap()
  var ticks = 0
}


package org.wololo.wastelands.core

import org.wololo.wastelands.core.unit._
import akka.actor.ActorRef

trait GameState {
  //val events = ArrayBuffer[Event]()
  var players= Vector[ActorRef]()
  val map: TileMap = new TileMap()
  var ticks = 0
}


trait GamePlayerState extends GameState {
  var units = Map[ActorRef, UnitClientState]()
  var projectiles = Vector[Projectile]()
}

class GameCpuPlayerState extends GamePlayerState {
}
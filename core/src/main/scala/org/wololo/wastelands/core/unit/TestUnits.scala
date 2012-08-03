package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import java.io.File
import akka.actor.ActorRef

object UnitTypes {
  val TestUnit1 = 0
  val TestUnit2 = 1
  val Harvester = 2
}

class TestUnit1(player: ActorRef, gameState: GameState, position: Coordinate, direction: Direction) extends Unit(player, gameState, position, direction) {
  override val Velocity = 0.02
  //override val fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  //override val explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))
}

class TestUnit2(player: ActorRef, gameState: GameState,position: Coordinate, direction: Direction) extends Unit(player, gameState,position, direction) {
  override val Velocity = 0.04
  //override val fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  //override val explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))
}

class Harvester(player: ActorRef, gameState: GameState,position: Coordinate, direction: Direction) extends Unit(player, gameState,position, direction) {
  override val Velocity = 0.01
}
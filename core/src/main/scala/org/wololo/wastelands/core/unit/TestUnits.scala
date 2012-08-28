package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import java.io.File
import akka.actor.ActorRef

// TODO: have types be traits to be mixed into both unit actors and unit state...?


class TestUnit1(player: ActorRef, game: GameState, position: Coordinate, direction: Direction) extends Unit(player, game, position, direction) {
  val unitType = Unit.TestUnit1
  override val Velocity = 0.02
  //override val fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  //override val explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))
}

class TestUnit2(player: ActorRef, game: GameState, position: Coordinate, direction: Direction) extends Unit(player, game, position, direction) {
  val unitType = Unit.TestUnit2
  override val Velocity = 0.04
  //override val fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  //override val explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))
}

class Harvester(player: ActorRef, game: GameState, position: Coordinate, direction: Direction) extends Unit(player, game, position, direction) {
  val unitType = Unit.Harvester
  override val Velocity = 0.01
}
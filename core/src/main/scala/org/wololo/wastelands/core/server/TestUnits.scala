package org.wololo.wastelands.core.server

import org.wololo.wastelands.core._
import java.io.File
import akka.actor.ActorRef
import org.wololo.wastelands.core.unit._

class TestUnit1(player: ActorRef, game: Game, position: Coordinate, direction: Direction) extends UnitActor(player, game, Unit.TestUnit1, position, direction) {
  override val Velocity = 0.02
  //override val fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  //override val explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))
}

class TestUnit2(player: ActorRef, game: Game, position: Coordinate, direction: Direction) extends UnitActor(player, game, Unit.TestUnit2, position, direction) {
  override val Velocity = 0.04
  //override val fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  //override val explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))
}

class Harvester(player: ActorRef, game: Game, position: Coordinate, direction: Direction) extends UnitActor(player, game, Unit.Harvester, position, direction) {
  override val Velocity = 0.01
}
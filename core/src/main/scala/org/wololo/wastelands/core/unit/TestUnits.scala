package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import java.io.File

class TestUnit1(game: Game, player: Int, coordinate: Coordinate) extends Unit(game, player, coordinate) {
  override val Velocity = 0.02
  //override val fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  //override val explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))
}

class TestUnit2(game: Game, player: Int, coordinate: Coordinate) extends Unit(game, player, coordinate) {
  override val Velocity = 0.04
  //override val fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  //override val explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))
}
package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import java.io.File

class TestUnit1(player: Player, coordinate: Coordinate) extends Unit( player, coordinate) {
  override val Velocity = 0.02
  //override val fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  //override val explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))
}

class TestUnit2(player: Player, coordinate: Coordinate) extends Unit( player, coordinate) {
  override val Velocity = 0.04
  //override val fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  //override val explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))
}

class Harvester(player: Player, coordinate: Coordinate) extends Unit( player, coordinate) {
  override val Velocity = 0.01
}
package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import java.io.File

class TestUnit1(val player: Int, val position: Coordinate) extends Unit(player, position) {
  override val Velocity = 0.02
  //override val fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  //override val explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))
}

class TestUnit2(val player: Int, val position: Coordinate) extends Unit(player, position) {
  override val Velocity = 0.04
  //override val fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  //override val explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))
}

class Harvester(val player: Int, val position: Coordinate) extends Unit(player, position) {
  override val Velocity = 0.01
}
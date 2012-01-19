package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import org.wololo.wastelands.vmlayer.SoundFactory
import java.io.File

class TestUnit1(soundFactory: SoundFactory, map: GameMap, player: Int, coordinate: Coordinate) extends Unit(map, player, coordinate) {
  override val Velocity = 0.03
  var fireSound = soundFactory.create(new File("sounds/laser.ogg"))
  var explodeSound = soundFactory.create(new File("sounds/explosion.ogg"))
}

class TestUnit2(soundFactory: SoundFactory, map: GameMap, player: Int, coordinate: Coordinate) extends Unit(map, player, coordinate) {
  override val Velocity = 0.06
  var fireSound = soundFactory.create(new File("sounds/laser.ogg"))
  var explodeSound = soundFactory.create(new File("sounds/explosion.ogg"))
}
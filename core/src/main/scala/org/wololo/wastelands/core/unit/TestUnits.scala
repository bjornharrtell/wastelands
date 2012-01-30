package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import org.wololo.wastelands.vmlayer.SoundFactory
import java.io.File

class TestUnit1(game: Game, player: Int, coordinate: Coordinate) extends Unit(game, player, coordinate) {
  override val Velocity = 0.03
  var fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  var explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))
}

class TestUnit2(game: Game, player: Int, coordinate: Coordinate) extends Unit(game, player, coordinate) {
  override val Velocity = 0.06
  var fireSound = game.vmContext.soundFactory.create(new File("sounds/laser.ogg"))
  var explodeSound = game.vmContext.soundFactory.create(new File("sounds/explosion.ogg"))
}
package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._

class TestUnit1(map: GameMap, player: Int, coordinate: Coordinate) extends Unit(map, player, coordinate) {
  override val Velocity = 0.03
}

class TestUnit2(map: GameMap, player: Int, coordinate: Coordinate) extends Unit(map, player, coordinate) {
  override val Velocity = 0.06
}
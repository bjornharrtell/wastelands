package org.wololo.wastelands.core.unit

trait Tickable {
  self: Unit =>

  val map = game.map

  def tick() : Unit = {
    this
  }
}
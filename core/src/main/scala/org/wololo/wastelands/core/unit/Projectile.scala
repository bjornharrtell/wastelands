package org.wololo.wastelands.core.unit

class Projectile(fromUnit: Unit, toUnit: Unit) {

  val fromPos = fromUnit.position.clone()
  val toPos = toUnit.position.clone()

  val Ticks = 10
  var ticks = Ticks

  var distance = 1.0

  def tick() {
    ticks -= 1

    distance -= 1.0 / Ticks
  }
}
package org.wololo.wastelands.core.unit

class Projectile(fromUnit: Unit, toUnit: Unit) {

  val fromPos = fromUnit.position.clone()
  val toPos = toUnit.position.clone()

  val Ticks = 10
  var ticks:Float = Ticks

  var distance = 0.0

  def tick() {
    ticks -= 1

    distance = 1.0-(ticks/Ticks)
  }
}
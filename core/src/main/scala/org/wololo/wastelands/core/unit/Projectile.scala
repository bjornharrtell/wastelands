package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.Game

class Projectile(game: Game, fromUnit: Unit, toUnit: Unit) {

  val fromPos = fromUnit.position.clone()
  val toPos = toUnit.position.clone()

  val Ticks = 10
  var ticks: Float = Ticks

  var distance = 0.0
  
  var alive = true

  def tick() : Projectile = {
    ticks -= 1

    distance = 1.0 - (ticks / Ticks)

    if (distance == 1.0) alive = false
    
    this
  }
}
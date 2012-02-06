package org.wololo.wastelands.core.unit
import org.wololo.wastelands.core.Game

class Projectile(game: Game, fromUnit: Unit, toUnit: Unit) {

  val fromPos = fromUnit.position.clone()
  val toPos = toUnit.position.clone()

  var distance = 0.0
  var tagetDistance = toPos.distance(fromPos)
  
  val Ticks = 10 * tagetDistance
  var ticks: Float = Ticks
  
  var alive = true

  def tick() : Projectile = {
    ticks -= 1

    distance = (1 - (ticks / Ticks)) * tagetDistance

    if (distance == tagetDistance) alive = false
    
    this
  }
}
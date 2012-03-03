package org.wololo.wastelands.core.unit.action

import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.unit.Projectile

class Fire(unit: Unit, target: Unit) extends Action(unit) {
  
  var firstTick = true
  val PauseTicks = 120
  private var pauseTicksCounter = PauseTicks
  
  def onTick() {
    if (firstTick)
      fire()
    else
      pauseTick()
  }
  
  private def fire() {
    firstTick = false
    
    unit.game.projectiles += new Projectile(unit.game, unit, target)
    
    target.damage(2)
  }

  private def pauseTick() {
    pauseTicksCounter -= 1
    if (pauseTicksCounter == 0) complete()
  }
}
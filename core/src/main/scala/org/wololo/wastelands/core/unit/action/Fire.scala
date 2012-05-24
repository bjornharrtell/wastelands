package org.wololo.wastelands.core.unit.action

import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.unit.Projectile

class Fire(unit: Unit, target: Unit) extends Action(unit) {
  
  def onTick() {
	fire()
  }
  
  private def fire() {
    unit.fireSound.play()
    
    unit.game.projectiles += new Projectile(unit.game, unit, target)
    
    target.damage(unit.AttackStrength)
    
    complete()
  }
}
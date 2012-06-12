package org.wololo.wastelands.core.unit.action

import org.wololo.wastelands.core.unit.Order

import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.unit.Projectile

class Fire(order: Order, unit: Unit, target: Unit) extends Action(order, unit) {
  
  override val CooldownTicks = 60
  
  val Type = 2
  
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
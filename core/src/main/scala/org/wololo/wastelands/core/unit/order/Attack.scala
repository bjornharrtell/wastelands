package org.wololo.wastelands.core.unit.order

import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Order
import org.wololo.wastelands.core.unit.Action
import org.wololo.wastelands.core.unit.action.Turn
import org.wololo.wastelands.core.unit.action.Fire
import org.wololo.wastelands.core.unit.action.MoveStep

class Attack(unit: Unit, target: Unit) extends Order(unit: Unit) {
  val Type = 2
  
  override def generateAction(): Option[Action] = {
    if (!target.alive) return None
    
    val direction = unit.game.map.calcDirection(unit.position, target.position)

    if (direction.isDefined) {
      if (unit.direction != direction.get)
        return Option(new Turn(this, unit, direction.get))
      else if (unit.position.distance(target.position) <= unit.Range)
        return Option(new Fire(this, unit, target))
      else
        return Option(new MoveStep(this, unit))
    } else {
      return None
    }
  }
}
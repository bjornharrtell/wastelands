package org.wololo.wastelands.core.unit

import scala.reflect.Type
import org.wololo.wastelands.core.unit.action.Fire

abstract class Order(unit: Unit) {
  type generatesAction
  //val generatesAction: Class[Action] = classOf[Action]
  
  def generateAction(): Option[Action] = { return None }
  def dispose() {}
}
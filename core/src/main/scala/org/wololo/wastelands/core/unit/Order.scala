package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core.unit.action.Fire

abstract class Order(unit: Unit) {

  val Type: Int

  def generateAction(): Option[Action] = { return None }
  def dispose() {}
}

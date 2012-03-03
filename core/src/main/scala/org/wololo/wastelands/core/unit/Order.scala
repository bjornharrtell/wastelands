package org.wololo.wastelands.core.unit

abstract class Order(unit: Unit) {
	def generateAction() : Option[Action] = { return None }
}
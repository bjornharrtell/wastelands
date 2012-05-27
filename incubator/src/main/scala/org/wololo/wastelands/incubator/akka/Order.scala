package org.wololo.wastelands.incubator.akka

sealed trait Order

case class Move(gameUnit: GameUnit, to: (Int, Int)) extends Order
case class Attack(attackingUnit: GameUnit, target: GameEntity) extends Order
case class Guard(gameUnit: GameUnit) extends Order

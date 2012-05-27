package org.wololo.wastelands.incubator.akka

sealed trait GameEntity{
  def owner: Player
  def position: (Int, Int)
}

sealed trait GameUnit extends GameEntity

sealed trait GameBuilding extends GameEntity {
  def dimensions: (Int, Int)
}


case class Jeep(owner:Player, position: (Int,  Int)) extends GameUnit

case class Base(owner:Player, position: (Int,  Int), dimensions: (Int, Int) = (2,2)) extends GameBuilding


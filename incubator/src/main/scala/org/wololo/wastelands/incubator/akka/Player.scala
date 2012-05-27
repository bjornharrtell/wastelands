package org.wololo.wastelands.incubator.akka

object PlayerBuilder {
  def humanPlayer(id: Int, name: String) = {
    val humanPlayer = HumanPlayer(id, name)
    humanPlayer.units = humanPlayer.units :+ Jeep(humanPlayer, (5,5)) //temp placement
    humanPlayer.buildings = humanPlayer.buildings:+ Base(humanPlayer, (0,0)) //temp placement
  }

  def aiPlayer(id: Int, name: String) = {
    val aiPlayer = AiPlayer(id, name)
    aiPlayer.units = aiPlayer.units :+ Jeep(aiPlayer, (7,7)) //temp placement
    aiPlayer.buildings = aiPlayer.buildings:+ Base(aiPlayer, (9,9)) //temp placement
  }
}

sealed trait Player {
  var units: Vector[GameUnit] = Vector[GameUnit]()
  var buildings: Vector[GameBuilding] = Vector[GameBuilding]()
}

case class HumanPlayer(id: Int,  name: String) extends Player
case class AiPlayer(id: Int,  name: String) extends Player

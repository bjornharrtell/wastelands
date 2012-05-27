package org.wololo.wastelands.incubator.akka

class AkkaApp extends App {

  override def main(args: Array[String]) {
    val players = List[Player](
      HumanPlayer(1, "Sarah Conner"),
      AiPlayer(1, "Skynet")
    )

    val game = new Game(players)

    game.play
  }

}

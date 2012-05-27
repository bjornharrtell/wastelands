package org.wololo.wastelands.incubator.akka


class GameMap(horizontalMapLength: Int, verticalMapLength: Int) {
  val ground = Array.fill[Ground](horizontalMapLength, verticalMapLength)(Sand)
  val gameEntities = Array.fill[Option[GameEntity]](horizontalMapLength, verticalMapLength)(None)

}

class Game(val players: List[Player], horizontalMapLength: Int = 10, verticalMapLength: Int = 10) {
  assert(players.length > 1) //must be minimum 2 players

  val map: GameMap = new GameMap(horizontalMapLength, verticalMapLength)



  def play {
    while(true){


    }
  }

}

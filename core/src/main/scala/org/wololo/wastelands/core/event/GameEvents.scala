package org.wololo.wastelands.core.event

import org.wololo.wastelands.core.GameMap

case object Exit extends Event
case object CreateGame extends Event
case class GameJoined(gameMap: GameMap) extends Event
package org.wololo.wastelands.core.event

import org.wololo.wastelands.core.TileMap

case object Create extends Event
case object End extends Event
case object Join extends Event
case class Joined(map: TileMap) extends Event
package org.wololo.wastelands.core.event

import org.wololo.wastelands.core.Coordinate
import org.wololo.wastelands.core.TileMap
import org.wololo.wastelands.core.unit.Unit

case object Create extends Event
case object End extends Event
case object Join extends Event
case class Joined(map: TileMap) extends Event
case object Tick extends Event

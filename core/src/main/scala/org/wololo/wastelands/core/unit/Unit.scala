package org.wololo.wastelands.core.unit

import org.wololo.wastelands.core._
import org.wololo.wastelands.core.unit.order.Move
import org.wololo.wastelands.core.unit.order.Attack
import org.wololo.wastelands.core.unit.order.Guard
import org.wololo.wastelands.core.unit.action.MoveStep
import org.wololo.wastelands.core.unit.action.Turn
import org.wololo.wastelands.core.unit.action.Fire
import java.io.File
import org.wololo.wastelands.core.event
import org.wololo.wastelands.core.event.Event
import scala.collection.mutable.ArrayBuffer

import akka.actor._
import com.typesafe.config.ConfigFactory

abstract class Unit(player: Int, position: Coordinate) extends Actor {
	val unitState: UnitState = new UnitState(player, position)
}
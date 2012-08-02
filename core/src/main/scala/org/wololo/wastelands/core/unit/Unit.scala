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

abstract class Unit(player: ActorRef, position: Coordinate, direction: Direction) extends Actor with UnitState {
  val Velocity = 0.04
  val Range = 2
  val AttackStrength = 2
  
  //var order: Order = new Guard(this)
  
  def receive = {
    case e: event.Action =>
    case e: event.ActionComplete =>
    case e: event.Cooldown =>
    case e: event.CooldownComplete =>
  }
}
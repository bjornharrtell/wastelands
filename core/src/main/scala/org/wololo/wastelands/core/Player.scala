package org.wololo.wastelands.core

import akka.actor._
import com.typesafe.config.ConfigFactory
import org.wololo.wastelands.core.unit.Order
import org.wololo.wastelands.core.event.Event

class Player extends Actor {
  val order: Order

  def handleEvent(e: Event) {
    println(e)
    e match {
      case e: event.Action => exit
    }
  }

  def receive = {
    case e: Event => handleEvent(e)
  }
}
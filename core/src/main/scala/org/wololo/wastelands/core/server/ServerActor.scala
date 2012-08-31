package org.wololo.wastelands.core.server

import akka.actor._
import com.typesafe.config.ConfigFactory
import org.wololo.wastelands.core.event
import org.wololo.wastelands.core.CpuPlayer
import org.wololo.wastelands.core.event

class ServerActor extends Actor  {
  def receive = {
    case e: event.CreateGame =>
      val game = context.actorOf(Props[GameActor], e.name)
      val cpuPlayer = context.actorOf(Props(new CpuPlayer(game)), "CpuPlayer")
      sender ! event.GameCreated(game)
    case e: event.Connect =>
      sender ! event.Connected()
    case e: event.Tick =>
      context.children.foreach(_.forward(e))
  }
}
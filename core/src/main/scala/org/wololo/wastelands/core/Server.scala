package org.wololo.wastelands.core

import akka.actor._
import com.typesafe.config.ConfigFactory

class Server extends Actor  {
  def receive = {
    case e: event.CreateGame =>
      val game = context.actorOf(Props[Game], e.name)
      val cpuPlayer = context.actorOf(Props(new CpuPlayer(game, new GameCpuPlayerState())), "CpuPlayer")
      sender ! event.GameCreated(game)
    case e: event.Connect =>
      sender ! event.Connected()
    case e: event.Tick =>
      context.children.foreach(_.forward(e))
  }
}
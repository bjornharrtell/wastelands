package org.wololo.wastelands.core

import akka.actor._
import scala.collection.mutable.ArrayBuffer
import com.typesafe.config.ConfigFactory

class Server extends Actor  {
  def receive = akka.event.LoggingReceive {
    case e: event.CreateGame =>
      val game = context.actorOf(Props[Game], e.name)
      //val cpuPlayer = context.actorOf(Props(new CpuPlayer(game)), "CpuPlayer")
      sender ! event.GameCreated(game)
    case e: event.Connect =>
      sender ! event.Connected()
    case e: event.Tick =>
      context.children.foreach(_.forward(e))
  }
}
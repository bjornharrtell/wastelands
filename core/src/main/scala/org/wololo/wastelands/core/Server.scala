package org.wololo.wastelands.core

import akka.actor._
import scala.collection.mutable.ArrayBuffer
import com.typesafe.config.ConfigFactory

class Server extends Actor {
  val clients = ArrayBuffer[ActorRef]()
  val games = ArrayBuffer[ActorRef]()

  def receive = {
    case e: event.Create => create(sender, e.name)
    case e: event.Connect => sender ! event.Connected
    case e: event.Disconnect => clients -= sender
  }

  def create(client: ActorRef, name: String) = {
    // TODO: handle errors, like duplicate name
    val game = context.actorOf(Props[Game], name = name)
    client ! event.Created(game)
    games += game
  }
}
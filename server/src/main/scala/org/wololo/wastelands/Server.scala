package org.wololo.wastelands

import java.util.UUID

import scala.actors.Actor
import scala.actors.OutputChannel
import scala.actors.remote.RemoteActor
import scala.collection.mutable.HashMap

object Server extends Actor {
  //scala.actors.Debug.level = 3

  var running = true

  val id = UUID.randomUUID().toString()
  var clients: HashMap[String, OutputChannel[Any]] = null

  def main(args: Array[String]): Unit = {
    clients = HashMap[String, OutputChannel[Any]]().empty

    println("Server " + id + " starting...")
    start

    while (running) {
      this ! Heartbeat()
      Thread.sleep(1000)
    }
  }

  def act(): Unit = {
    println("Server listening to port 9000...")
    RemoteActor.alive(9000)
    RemoteActor.register('server, this)

    println("Server ready and awaiting connections....")
    loop {
      react {
        case e: Heartbeat => clients.values.foreach(_ ! e)
        case e: ClientEvent => handleClientEvent(sender, e)
      }
    }
  }

  def handleClientEvent(sender: OutputChannel[Any], e: ClientEvent) {
    println("Client message: " + e)
    e match {
      case e: InfoRequest => sender ! Info(id, "Server info...")
      case e: Connect => connect(sender, e)
      case e: Disconnect => disconnect(sender, e)
      case e: Shutdown => shutdown(sender, e)
    }
  }

  def connect(sender: OutputChannel[Any], e: Connect) {
    clients += (e.id -> sender)
    sender ! Connected(id)
    println("Clients connected: " + clients.size)
  }

  def disconnect(sender: OutputChannel[Any], e: Disconnect) = {
    clients -= e.id
    println("Clients connected: " + clients.size)
  }

  def shutdown(sender: OutputChannel[Any], e: Shutdown) = {
    println("Server is shutting down...")
    running = false
    exit
  }

}


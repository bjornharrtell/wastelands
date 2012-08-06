package org.wololo.wastelands

import scala.actors.Actor
import scala.actors.TIMEOUT
import scala.actors.remote.RemoteActor
import scala.actors.remote.Node
import java.security.MessageDigest
import java.util.UUID

object Client extends Actor {
  //scala.actors.Debug.level = 3

  val id = UUID.randomUUID().toString()

  var running = true

  def main(args: Array[String]): Unit = {
    println("Client " + id + " starting...")
    start

    println("Will now process user input...")

    while (running) {
      val input = Console.readLine()
      input match {
        case "info" => this ! Trigger(InfoRequest(id))
        case "quit" => this ! Trigger(Disconnect(id)); exit; running = false
        case "shutdown" => this ! Trigger(Shutdown(id)); exit; running = false
        case _ => println("Invalid input")
      }
    }
  }

  def act() {
    println("Client connecting to server...")
    val server = RemoteActor.select(Node("localhost", 9000), 'server)
    server ! Connect(id)

    loopWhile(running) {
      reactWithin(5000L) {
        case e: Heartbeat =>
        case e: Trigger => server ! e.event;
        case e: ServerEvent => println("Server message: " + e)
        case TIMEOUT => {
          println("Server connection timed out...")
          running = false
          exit
          // TODO: will be stuck on readLine here :(
        }
      }
    }
  }
}

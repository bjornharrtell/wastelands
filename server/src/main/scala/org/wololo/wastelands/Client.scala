package org.wololo.wastelands

import scala.actors._
import scala.actors.Actor._
import scala.actors.remote._
import scala.actors.remote.RemoteActor._

object Client extends Actor {
  var server: AbstractActor = null
  
  def main(args: Array[String]): Unit = {
    println("Client starting...")
    println("Client connecting to server...")
    server = select(Node("localhost", 9000), 'server)
    
    start

    while (true) {
      val input = Console.readLine("Client input: ")
      input match {
        case "quit" => server ! Disconnect; exit
        case "shutdown" => server ! Shutdown; exit
        case _ => println("Invalid input")
      }
    }
  }

  def handleEvent(e: Event) {
    println(e)
    e match {
      case Shutdown => exit
    }
  }

  def act() {
    server ! Connect

    loop {
      react {
        case e: Event => handleEvent(e)
      }
    }
  }
}

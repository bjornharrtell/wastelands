package org.wololo.wastelands

import akka.actor._
import com.typesafe.config.ConfigFactory

object Client extends App {
  
  override def main(args: Array[String]): Unit = {
    val system = ActorSystem("client", ConfigFactory.load.getConfig("client"))
    system.registerOnTermination(System.exit(1))

    val clientActor = system.actorOf(Props[ClientActor])

    var running = true

    while (running) {
      val input = Console.readLine("Client input: ")
      input match {
        case "quit" => clientActor ! Disconnect(); running = false;
        case "shutdown" => clientActor ! Shutdown(); running = false;
        case _ => println("Invalid input")
      }
    }
  }

  class ClientActor extends Actor {
    val id = hashCode;

    val server = context.actorFor("akka://server@127.0.0.1:9000/user/Server")

    server ! Connect()
    
    def disconnect() {
      server ! Disconnect()
      context.system.shutdown()
    }

    def shutdown() {
      server ! Shutdown()
      context.system.shutdown()
    }

    def handleEvent(e: Event) {
      println(e)
      e match {
        case e: Connected =>
        case e: Disconnect => disconnect()
        case e: Shutdown => shutdown()
        case _ =>
      }
    }

    def receive = {
      case e: Event => handleEvent(e)
    }
  }

}
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
        case "quit" => clientActor ! Disconnect(0); running = false;
        case "shutdown" => clientActor ! Shutdown(0); running = false;
        case _ => println("Invalid input")
      }
    }
  }

  class ClientActor extends Actor {
    val id = hashCode;

    val server = context.actorFor("akka://server@127.0.0.1:9000/user/Server")

    server ! Connect(id)
    
    def disconnect() {
      server ! Disconnect(id)
      context.system.shutdown()
    }

    def shutdown() {
      server ! Shutdown(0)
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
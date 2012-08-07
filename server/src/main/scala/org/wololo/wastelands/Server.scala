package org.wololo.wastelands

import scala.collection.mutable.HashMap
import akka.actor._
import com.typesafe.config.ConfigFactory
import scala.collection.mutable.ArrayBuffer

object Server extends App {
  override def main(args: Array[String]): Unit = {
    val system = ActorSystem("server", ConfigFactory.load.getConfig("server"))
    system.registerOnTermination(System.exit(1))
    
    val serverActor = system.actorOf(Props[ServerActor], name="Server")
  }

  class ServerActor extends Actor {
    var clients = ArrayBuffer[ActorRef]()

    def connect(sender: ActorRef) {
      sender ! Connected()
      clients += sender
      println("Clients connected: " + clients.size)
    }

    def disconnect(sender: ActorRef) {
      clients -= sender
      println("Clients connected: " + clients.size)
    }

    def shutdown() {
      clients.foreach(_ ! Shutdown())
      context.stop(self)
      context.system.shutdown()
    }

    def handleEvent(e: Event) {
      println(e)
      e match {
        case e: Connect => connect(sender)
        case e: Connected =>
        case e: Disconnect => disconnect(sender)
        case e: Shutdown => shutdown()
      }
    }

    def receive = {
      case e: Event => handleEvent(e)
    }
  }
}




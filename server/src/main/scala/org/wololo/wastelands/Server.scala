package org.wololo.wastelands

import scala.collection.mutable.HashMap

import akka.actor._
import com.typesafe.config.ConfigFactory

object Server extends App {
  override def main(args: Array[String]): Unit = {
    val system = ActorSystem("server", ConfigFactory.load.getConfig("server"))
    system.registerOnTermination(System.exit(1))
    
    val serverActor = system.actorOf(Props[ServerActor], name="Server")
  }

  class ServerActor extends Actor {
    var clients: HashMap[Int, ActorRef] = HashMap.empty[Int, ActorRef]

    def connect(id: Int, sender: ActorRef) {
      sender ! Connected
      clients += (id -> sender)
      println("Clients connected: " + clients.size)
    }

    def disconnect(id: Int, sender: ActorRef) {
      clients -= id
      println("Clients connected: " + clients.size)
    }

    def shutdown() {
      clients.values.foreach(_ ! Shutdown)
      context.stop(self)
      context.system.shutdown()
    }

    def handleEvent(e: Event) {
      println(e)
      e match {
        case e: Connect => connect(e.id, sender)
        case e: Connected =>
        case e: Disconnect => disconnect(e.id, sender)
        case e: Shutdown => shutdown()
      }
    }

    def receive = {
      case e: Event => handleEvent(e)
    }
  }
}




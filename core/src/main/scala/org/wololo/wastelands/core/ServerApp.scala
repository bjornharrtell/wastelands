package org.wololo.wastelands.core
import akka.actor._
import com.typesafe.config.ConfigFactory

object ServerApp extends App {
  override def main(args: Array[String]): Unit = {
    val system = ActorSystem("server", ConfigFactory.load.getConfig("server"))
    //system.registerOnTermination(System.exit(1))

    val server = system.actorOf(Props[Server], "Server")
  }
}
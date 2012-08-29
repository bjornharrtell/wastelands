package org.wololo.wastelands.core.server
import akka.actor._
import com.typesafe.config.ConfigFactory
import org.wololo.wastelands.core.event

object ServerApp extends App {
  override def main(args: Array[String]): Unit = {
    val system = ActorSystem("server", ConfigFactory.load.getConfig("server"))
    //system.registerOnTermination(System.exit(1))

    val server = system.actorOf(Props[Server], "Server")
    
    var lastTime = System.nanoTime
    var unprocessed = 0.0
    val nsPerTick = 1000000000.0 / 60.0
    var frames = 0
    var lastTimer1 = System.currentTimeMillis

    // TODO: make simple loop... does not have to account for render delays
    while (true) {
      val now = System.nanoTime
      unprocessed += (now - lastTime) / nsPerTick
      lastTime = now
      var shouldRender = false
      while (unprocessed >= 1.0) {
        server ! event.Tick()
        unprocessed -= 1
      }

      Thread.sleep(2)
    }

    system.shutdown()
  }
}
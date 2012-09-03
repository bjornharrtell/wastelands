package org.wololo.wastelands.core.client

import akka.actor._
import org.wololo.wastelands.vmlayer.VMContext
import com.typesafe.config.ConfigFactory
import org.wololo.wastelands.core.event
import org.wololo.wastelands.core.server.ServerActor

/**
 * Base reusable implementation for platform specific client application
 */
trait ClientApp {
  this: VMContext =>

  var running = false

  val config = ConfigFactory.parseString(
    """
    akka {
      actor {
        provider = "akka.remote.RemoteActorRefProvider"
      }
    }
    """)

  val system = ActorSystem("client", ConfigFactory.load(config))
  
  // NOTE: local or remote server...
  val server = system.actorOf(Props[ServerActor])
  //val server = system.actorFor("akka://server@192.168.0.100:9000/user/Server")
  
  val client = system.actorOf(Props(new Client(this, server)), "Player")

  def run() = {
    running = true

    var lastTime = System.nanoTime
    var unprocessed = 0.0
    val nsPerTick = 1000000000.0 / 60.0
    var frames = 0
    var lastTimer1 = System.currentTimeMillis

    while (running) {
      val now = System.nanoTime
      unprocessed += (now - lastTime) / nsPerTick
      lastTime = now
      var shouldRender = false
      while (unprocessed >= 1.0) {
        client ! event.Tick()
        // NOTE: need to tick server if it's local
        server ! event.Tick()
        unprocessed -= 1
        shouldRender = true
      }

      // NOTE: sleeping thread seem to disturb akka...
      //Thread.sleep(2)

      if (shouldRender) {
        frames += 1

        client ! event.Render()
      }

      if (System.currentTimeMillis - lastTimer1 > 1000) {
        lastTimer1 += 1000
        //println(frames + " fps")
        frames = 0
      }
    }

    system.shutdown()
  }
}
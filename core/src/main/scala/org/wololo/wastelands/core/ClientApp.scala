package org.wololo.wastelands.core

import akka.actor._
import org.wololo.wastelands.vmlayer.VMContext

trait ClientApp {
  this: VMContext =>
    
  var running = false
  
  val system = ActorSystem("client")
  val client = system.actorOf(Props(new Client(this)), "Player")

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
        unprocessed -= 1
        shouldRender = true
      }

      //Thread.sleep(2)

      if (shouldRender) {
        frames += 1

        client ! event.Render()
      }

      if (System.currentTimeMillis - lastTimer1 > 1000) {
        lastTimer1 += 1000
        //println(ticks + " ticks, " + frames + " fps")
        frames = 0
      }
    }

    system.shutdown()
  }
}
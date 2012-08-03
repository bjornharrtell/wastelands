package org.wololo.wastelands.core

import org.wololo.wastelands.core.event._
import org.wololo.wastelands.vmlayer.VMContext
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.TestUnit1
import org.wololo.wastelands.core.unit.Projectile
import org.wololo.wastelands.core.gfx.Screen
import akka.actor._
import com.typesafe.config.ConfigFactory

class Client(val vmContext: VMContext) extends ClientInputHandler with GameClientState {
  var running = true
  
  var selectedUnit: Option[Unit] = None
  
  val screen = new Screen(this)
  
  var lastTime = System.nanoTime
  var unprocessed = 0.0
  val nsPerTick = 1000000000.0 / 60.0
  var frames = 0
  var lastTimer1 = System.currentTimeMillis

  val system = ActorSystem("client", ConfigFactory.load.getConfig("client"))
  system.registerOnTermination(System.exit(1))

  val player = system.actorOf(Props[Player])

  while (running) {
    val now = System.nanoTime
    unprocessed += (now - lastTime) / nsPerTick
    lastTime = now
    var shouldRender = false
    while (unprocessed >= 1.0) {
      ticks += 1
      tick()
      unprocessed -= 1
      shouldRender = true
    }

    //Thread.sleep(2)

    if (shouldRender) {
      frames += 1

      screen.render()
      vmContext.render(screen.bitmap)
    }

    if (System.currentTimeMillis - lastTimer1 > 1000) {
      lastTimer1 += 1000
      //System.out.println(ticks + " ticks, " + frames + " fps")
      frames = 0
      ticks = 0
    }
  }

  class Player extends Actor {
    def receive = {
      case e: event.Move => println(e)
    }
  }

  def tick() {

    player ! Tick

    //units = units.withFilter(_.alive).map(_.tick)
    projectiles = projectiles.withFilter(_.alive).map(_.tick)

    ticks += 1
  }

}
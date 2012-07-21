package org.wololo.wastelands.core

import org.wololo.wastelands.core.event.Event
import org.wololo.wastelands.core.event.TouchEvent
import org.wololo.wastelands.vmlayer.VMContext
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.TestUnit1
import org.wololo.wastelands.core.unit.Projectile
import org.wololo.wastelands.core.gfx.Screen


class Client(val vmContext: VMContext) extends ClientInputHandler with GameState {
  type Pub = Client
  
  var selectedUnit: Option[Unit] = None

  var player = 0
  
  var running = false
  val screen = new Screen(this)
  
  /*def init() {
    units += (new TestUnit1(this, 1, (3, 10)),
      new TestUnit1(this, 1, (1, 2)),
      new TestUnit1(this, 1, (8, 8)),
      new TestUnit1(this, 1, (9, 11)),
      new TestUnit2(this, player, (5, 4)),
      new TestUnit2(this, player, (6, 6)),
      new Harvester(this, player, (5, 6)))

    for (unit <- units if unit.player == player) {
      map.removeShadeAround(unit.position)
    }

    for (unit <- units) {
      unit.subscribe(map)
    }
  }*/

  def run() {
    running = true

    var lastTime = System.nanoTime
    var unprocessed = 0.0
    val nsPerTick = 1000000000.0 / 60.0
    var frames = 0
    var ticks = 0
    var lastTimer1 = System.currentTimeMillis

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
  }

  def tick() {

    //units = units.withFilter(_.alive).map(_.tick)
    projectiles = projectiles.withFilter(_.alive).map(_.tick)

    ticks += 1
  }

  /**
   * Perform action on a chosen map tile
   */
  def mapTileAction(coordinate: Coordinate) {
    if (selectedUnit.isDefined) {
      selectedUnit.get.moveTo(coordinate)
    }
  }

  /**
   * Perform action on a selectable unit
   */
  def unitAction(unit: Unit) {
    if (selectedUnit.isDefined) {
      if (unit == selectedUnit) {
        return
      } else if (unit.player != player) {
        selectedUnit.get.attack(unit)
      } else {
        selectedUnit.get.unselect()
        unit.select()
        selectedUnit = Option(unit)
      }
    } else if (unit.player == player) {
      unit.select()
      selectedUnit = Option(unit)
    }
  }
}
package org.wololo.wastelands.core
import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core.unit._
import org.wololo.wastelands.core.gfx._
import scala.collection.mutable.ArrayBuffer

class Game[T: ClassManifest](val graphicsContext: GraphicsContext[T]) {

  var running = false
  var tickCount = 0

  val map = new GameMap
  val screen = new Screen(this)

  var selectedUnit: Option[Unit] = None

  var player = 0

  var explodeCounter = 0

  val units = ArrayBuffer[Unit](
    new TestUnit1(map, 1, (3, 10)),
    new TestUnit2(map, player, (5, 4)),
    new TestUnit2(map, player, (6, 6)))

  units.filter(unit => unit.player == player).foreach(unit => map.removeShadeAround(unit.position))

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
      var shouldRender = true
      while (unprocessed >= 1.0) {
        ticks += 1
        tick
        unprocessed -= 1
        shouldRender = true
      }

      //Thread.sleep(2)

      if (shouldRender) {
        frames += 1
        screen.render
        graphicsContext.render(screen.bitmap)
      }

      if (System.currentTimeMillis - lastTimer1 > 1000) {
        lastTimer1 += 1000
        System.out.println(ticks + " ticks, " + frames + " fps")
        frames = 0
        ticks = 0
      }
    }
  }

  def tick() {
    explodeCounter += 1

    if (explodeCounter == 60*5) {
      units(2).exploding = true
    }

    units.foreach(unit => unit.tick)
    tickCount += 1
  }

  def scroll(dx: Int, dy: Int) {
    screen.scroll(dx, dy)
  }

  def click(x: Int, y: Int) {
    var clickedUnit = false

    // filter out visible and clicked units
    // TODO: need to handle case where units have overlapping bounds i.e multiple hits here
    units.filter(unit => unit.visible && unit.ScreenBounds.contains(x, y)).foreach(unit => {
      doClickedUnitAction(unit, x, y)
      clickedUnit = true
    })

    // no unit was clicked
    if (!clickedUnit) doClickedMapTileAction(x, y)
  }

  /**
   * Do appropriate actions when a map tile has been clicked
   */
  private def doClickedMapTileAction(x: Int, y: Int) {
    if (selectedUnit.isDefined) {
      val mx = screen.calculateTileIndex(screen.screenOffset.x + x)
      val my = screen.calculateTileIndex(screen.screenOffset.y + y)
      selectedUnit.get.moveTo(mx, my)
    }
  }

  /**
   * Do appropriate actions when a selectable unit has been clicked
   */
  private def doClickedUnitAction(unit: Unit, x: Int, y: Int) {
    if (selectedUnit.isDefined) {
      if (unit == selectedUnit) {
        return
      } else if (unit.player != player) {
        //selectedUnit.attack(unit)
      } else {
        selectedUnit.get.unselect
        unit.select
        selectedUnit = Option(unit)
      }
    } else if (unit.player == player) {
      unit.select
      selectedUnit = Option(unit)
    }
  }
}

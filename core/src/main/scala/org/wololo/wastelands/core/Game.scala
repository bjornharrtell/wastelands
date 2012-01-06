package org.wololo.wastelands.core
import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core.gfx._
import scala.collection.mutable.ArrayBuffer

class Game[T: ClassManifest](graphicsContext: GraphicsContext[T]) {
  val tileSetFactory = new TileSetFactory[T](graphicsContext)

  val Width = 32 * 16
  val Height = 32 * 16

  var running = false
  var tickCount = 0

  val map = new GameMap()
  val screen = new Screen(this, graphicsContext)
  
  def randomPos: Int = ((Math.random*(map.Width-6))+3).toInt
 
  //create 10 units for each type
  val units1 = for(i <- (0 until 10).toArray) yield new TestUnit1(map,randomPos,randomPos)
  val units2 = for(i <- (0 until 10).toArray) yield new TestUnit2(map,randomPos,randomPos)

  //give all units a destination
  units1.foreach(_.moveTo(randomPos,randomPos))
  units2.foreach(_.moveTo(randomPos,randomPos))

  val units = units1++units2
  
  //val units = ArrayBuffer(, new Unit(map,27,7), new Unit(map,7,7), new Unit(map,3,4), new Unit(map,14,5), new Unit(map,8,8))
  
  // TODO: remove test code
  units.foreach(_.startMove())

  def run() {
    running = true

    var lastTime = System.nanoTime
    var unprocessed = 0.0
    val nsPerTick = 1000000000.0 / 60.0
    var frames = 0
    var ticks = 0
    var lastTimer1 = System.currentTimeMillis

    while (running) {
      val now = System.nanoTime()
      unprocessed += (now - lastTime) / nsPerTick
      lastTime = now
      var shouldRender = true
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

    units.foreach((unit) => unit.tick())

    tickCount += 1
  }

  def move(dx: Int, dy: Int) {
    screen.scroll(dx, dy)
  }
}
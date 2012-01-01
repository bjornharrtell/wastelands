package org.wololo.wastelands.core
import org.wololo.wastelands.vmlayer.Canvas
import org.wololo.wastelands.vmlayer.FrameRenderer
import org.wololo.wastelands.vmlayer.BitmapFactory
import org.wololo.wastelands.vmlayer.CanvasFactory

class Game(frameRenderer: FrameRenderer, val bitmapFactory: BitmapFactory, val canvasFactory: CanvasFactory) {
  val tileSetFactory = new TileSetFactory(bitmapFactory, canvasFactory)

  val Width = 32 * 16
  val Height = 32 * 16

  var running = false
  var tickCount = 0

  val map = new Map()
  val screen = new Screen(this)

  def run() {
    running = true

    var lastTime = System.nanoTime()
    var unprocessed = 0.0
    val nsPerTick = 1000000000.0 / 60.0
    var frames = 0
    var ticks = 0
    var lastTimer1 = System.currentTimeMillis()

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
        frameRenderer.render(screen.bitmap)
      }

      if (System.currentTimeMillis() - lastTimer1 > 1000) {
        lastTimer1 += 1000
        System.out.println(ticks + " ticks, " + frames + " fps")
        frames = 0
        ticks = 0
      }
    }
  }

  def tick() {

    screen.unit.tick

    tickCount += 1
  }

  def move(dx: Int, dy: Int) {
    screen.move(dx, dy)
  }
}
package org.wololo.dune3.core
import org.wololo.dune3.vmlayer.Canvas
import org.wololo.dune3.vmlayer.TileSetFactory
import org.wololo.dune3.vmlayer.Context

class Game (tileSetFactory: TileSetFactory, context: Context) {
  val Width = 32 * 16
  val Height = 32 * 16

  var running = false
  var tickCount = 0

  val map = new Map()
  val screen = new Screen(tileSetFactory, map)
  
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

      Thread.sleep(2)

      if (shouldRender) {
        frames += 1
        render()
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
    tickCount += 1
  }

  def render() {
    val canvas = context.getCanvas();
    
    if (canvas != null) {
      screen.render(canvas, Width, Height)
      canvas.dispose()
      canvas.show()
    }
  }
  
  def move(dx: Int, dy: Int) {
    screen.move(dx, dy)
  }
}
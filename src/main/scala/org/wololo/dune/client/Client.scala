package org.wololo.dune.client
import java.awt.Canvas
import java.awt.event.MouseMotionListener
import java.awt.Dimension
import java.awt.event.MouseEvent
import javax.swing.JFrame
import java.awt.BorderLayout
import org.wololo.dune.game.Map;

object Client extends Canvas with MouseMotionListener with Runnable {
  val Width = 32 * 16
  val Height = 32 * 16

  var running = false
  var tickCount = 0

  val map = new Map()
  val screen = new Screen(map)

  var prevX = 0
  var prevY = 0

  def start() {
    running = true
    addMouseMotionListener(this)
    new Thread(this).start()
  }

  def run() {
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
    val bufferStrategy = getBufferStrategy
    if (bufferStrategy == null) {
      createBufferStrategy(3)
    } else {
      val graphics = bufferStrategy.getDrawGraphics

      screen.render(graphics, Width, Height)

      graphics.dispose()
      bufferStrategy.show()
    }
  }

  def main(args: Array[String]) {
    setPreferredSize(new Dimension(Width, Height))
    setMaximumSize(new Dimension(Width, Height))
    setMinimumSize(new Dimension(Width, Height))

    val frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setLayout(new BorderLayout())
    frame.add(this)
    frame.pack()
    frame.setLocationRelativeTo(null)
    frame.setResizable(false)
    frame.setVisible(true)

    start()
  }

  override def mouseDragged(e: MouseEvent) {
    val x = e.getX
    val y = e.getY

    val dx = prevX - x
    val dy = prevY - y

    screen.move(dx, dy)

    prevX = x
    prevY = y
  }

  override def mouseMoved(e: MouseEvent) {
    val x = e.getX
    val y = e.getY

    prevX = x
    prevY = y
  }
}
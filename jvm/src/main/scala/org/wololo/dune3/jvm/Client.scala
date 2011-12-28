package org.wololo.dune3.jvm
import java.awt.Canvas
import java.awt.event.MouseMotionListener
import java.awt.Dimension
import java.awt.event.MouseEvent
import javax.swing.JFrame
import java.awt.BorderLayout
import org.wololo.dune3.core.Map
import org.wololo.dune3.core.Game
import org.wololo.dune3.vmlayer.Context

object Client extends Canvas with MouseMotionListener with Runnable with Context {

  val Width = 32 * 16
  val Height = 32 * 16

  val game = new Game(new AWTTileSetFactory(32), this)

  var prevX = 0
  var prevY = 0

  override def run() {
    game.run()
  }

  def main(args: Array[String]) {
    setPreferredSize(new Dimension(Width, Height))
    setMaximumSize(new Dimension(Width, Height))
    setMinimumSize(new Dimension(Width, Height))

    val frame = new JFrame()
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setLayout(new BorderLayout())
    frame.add(this)
    frame.pack()
    frame.setLocationRelativeTo(null)
    frame.setResizable(false)
    frame.setVisible(true)

    addMouseMotionListener(this)
    new Thread(this).start()
  }

  def getCanvas(): org.wololo.dune3.vmlayer.Canvas = {
    val bufferStrategy = getBufferStrategy
    if (bufferStrategy == null) {
      createBufferStrategy(3)
      return null
    } else {
      return new AWTCanvas(bufferStrategy)
    }
  }

  def disposeCanvas() {

  }

  override def mouseDragged(e: MouseEvent) {
    val x = e.getX
    val y = e.getY

    val dx = prevX - x
    val dy = prevY - y

    game.move(dx, dy)

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

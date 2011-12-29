package org.wololo.wastelands.jvm
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import java.awt.BorderLayout
import java.awt.Canvas
import java.awt.Dimension

import org.wololo.wastelands.core.Game
import org.wololo.wastelands.vmlayer.Context

import javax.swing.JFrame

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

  def getCanvas(): org.wololo.wastelands.vmlayer.Canvas = {
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

  def mouseDragged(e: MouseEvent) {
    val x = e.getX
    val y = e.getY

    val dx = prevX - x
    val dy = prevY - y

    game.move(dx, dy)

    prevX = x
    prevY = y
  }

  def mouseMoved(e: MouseEvent) {
    val x = e.getX
    val y = e.getY

    prevX = x
    prevY = y
  }
}

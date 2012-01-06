package org.wololo.wastelands.jvm
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.image.BufferedImage
import java.awt.BorderLayout
import java.awt.Canvas
import java.awt.Dimension

import org.wololo.wastelands.core.Game

import javax.swing.JFrame

object Client extends Canvas with MouseListener with MouseMotionListener with Runnable with AWTGraphicsContext {
  var game: Game[BufferedImage] = null

  var prevX = 0
  var prevY = 0

  override def run() {
    game.run()
  }

  def main(args: Array[String]) {
    setPreferredSize(new Dimension(screenWidth, screenHeight))
    setMaximumSize(new Dimension(screenWidth, screenHeight))
    setMinimumSize(new Dimension(screenWidth, screenHeight))

    val frame = new JFrame()
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setLayout(new BorderLayout())
    frame.add(this)
    frame.pack()
    frame.setLocationRelativeTo(null)
    frame.setResizable(false)
    frame.setVisible(true)
    frame.setIgnoreRepaint(true)

    addMouseListener(this)
    addMouseMotionListener(this)
    
    game = new Game(this)
    
    new Thread(this).start()
  }
  
  def render(bitmap: BufferedImage) {
    val bufferStrategy = getBufferStrategy
    if (bufferStrategy == null) {
      createBufferStrategy(2)
    } else {
      val graphics = bufferStrategy.getDrawGraphics()
      // NOTE: I think this is the fastest drawImage for this purpose
      graphics.drawImage(bitmap, 0, 0, screenWidth, screenHeight, null)
      graphics.dispose()
      bufferStrategy.show()
    }
  }

  def mouseDragged(e: MouseEvent) {
    val x = e.getX
    val y = e.getY

    val dx = prevX - x
    val dy = prevY - y

    game.scroll(dx, dy)

    prevX = x
    prevY = y
  }

  def mouseMoved(e: MouseEvent) {
    val x = e.getX
    val y = e.getY

    prevX = x
    prevY = y
  }
  
  def mouseClicked(e: MouseEvent) {
    val x = e.getX
    val y = e.getY
    
    game.click(x, y)
  }
  
  def mouseEntered(e: MouseEvent) = {}
  def mouseExited(e: MouseEvent) = {}
  def mousePressed(e: MouseEvent) = {}
  def mouseReleased(e: MouseEvent) = {}
}

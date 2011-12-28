package org.wololo.dune3.jvm
import org.wololo.dune3.vmlayer.Canvas
import java.awt.Graphics
import java.awt.image.BufferStrategy
import java.awt.image.BufferedImage

class AWTCanvas(bufferStrategy: BufferStrategy) extends Canvas {
  val graphics = bufferStrategy.getDrawGraphics()

  def drawImage(image: Object, x: Int, y: Int) {
    graphics.drawImage(image.asInstanceOf[BufferedImage], x, y, null)
  }

  def drawImage(image: Object, dx1: Int, dy1: Int, dx2: Int, dy2: Int, sx1: Int, sy1: Int, sx2: Int, sy2: Int) {
    graphics.drawImage(image.asInstanceOf[BufferedImage], dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null)
  }

  def clearRect(x1: Int, y1: Int, x2: Int, y2: Int) {
    graphics.fillRect(x1, y1, x2 - x1, y2 - y1)
  }

  def show() {
    bufferStrategy.show()
  }

  def dispose() {
    graphics.dispose()
  }
}
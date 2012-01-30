package org.wololo.wastelands.jvm
import org.wololo.wastelands.vmlayer.Canvas
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.awt.Color

class AWTCanvas(graphics: Graphics) extends Canvas {
  def drawImage(id: Int, x: Int, y: Int) {
    graphics.drawImage(AWTBitmapFactory.bitmaps(id), x, y, null)
  }

  def drawImage(id: Int, dx1: Int, dy1: Int, dx2: Int, dy2: Int, sx1: Int, sy1: Int, sx2: Int, sy2: Int) {
    graphics.drawImage(AWTBitmapFactory.bitmaps(id), dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null)
  }

  def drawRect(x1: Int, y1: Int, x2: Int, y2: Int) {
    graphics.setColor(Color.YELLOW)
    graphics.drawRect(x1, y1, x2 - x1, y2 - y1)
  }
  
  def clearRect(x1: Int, y1: Int, x2: Int, y2: Int) {
    graphics.setColor(Color.BLACK)
    graphics.fillRect(x1, y1, x2 - x1, y2 - y1)
  }
}
package org.wololo.dune3.jvm
import org.wololo.dune3.vmlayer.Canvas
import java.awt.Graphics
import org.wololo.dune3.vmlayer.Image
import java.awt.image.BufferStrategy

class AWTCanvas(bufferStrategy: BufferStrategy) extends Canvas {
  val graphics: Graphics = bufferStrategy.getDrawGraphics();

  def drawImage(image: Image, dx1: Int, dy1: Int, dx2: Int, dy2: Int, sx1: Int, sy1: Int, sx2: Int, sy2: Int) {
    val awtImage: AWTImage = image.asInstanceOf[AWTImage]
    graphics.drawImage(awtImage.bufferedImage, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null)
  }

  def show() {
    bufferStrategy.show();
  }
  
  def dispose() {
    graphics.dispose()
  }
}
package org.wololo.wastelands.jvm
import org.wololo.wastelands.vmlayer.Canvas
import java.awt.image.BufferedImage
import org.wololo.wastelands.vmlayer.CanvasFactory

class AWTCanvasFactory extends CanvasFactory {
  def create(bitmap: Object): Canvas = {
    new AWTCanvas(bitmap.asInstanceOf[BufferedImage].getGraphics())
  }
}
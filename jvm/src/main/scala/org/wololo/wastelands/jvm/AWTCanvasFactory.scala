package org.wololo.wastelands.jvm
import org.wololo.wastelands.vmlayer.Canvas
import java.awt.image.BufferedImage
import org.wololo.wastelands.vmlayer.CanvasFactory

object AWTCanvasFactory extends CanvasFactory {
  def create(id: Int): Canvas = {
    new AWTCanvas(AWTBitmapFactory.bitmaps(id).getGraphics)
  }
}
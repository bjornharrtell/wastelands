package org.wololo.wastelands.jvm
import org.wololo.wastelands.vmlayer.Canvas
import java.awt.image.BufferedImage
import org.wololo.wastelands.vmlayer.CanvasFactory

object AWTCanvasFactory extends CanvasFactory[BufferedImage] {
  def create(bitmap: BufferedImage): Canvas[BufferedImage] = {
    new AWTCanvas(bitmap.getGraphics)
  }
}
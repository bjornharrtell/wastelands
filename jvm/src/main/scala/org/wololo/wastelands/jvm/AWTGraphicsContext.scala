package org.wololo.wastelands.jvm

import java.awt.image.BufferedImage
import org.wololo.wastelands.vmlayer.{GraphicsContext, CanvasFactory, BitmapFactory}


trait AWTGraphicsContext extends GraphicsContext[BufferedImage] {
  def render(bitmap: BufferedImage)
  val bitmapFactory: BitmapFactory[BufferedImage] = AWTBitmapFactory
  val canvasFactory: CanvasFactory[BufferedImage] = AWTCanvasFactory
}
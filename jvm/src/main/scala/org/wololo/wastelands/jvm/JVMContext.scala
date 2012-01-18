package org.wololo.wastelands.jvm

import java.awt.image.BufferedImage
import org.wololo.wastelands.vmlayer._

trait JVMContext extends VMContext[BufferedImage] {
  def render(bitmap: BufferedImage)
  val bitmapFactory: BitmapFactory[BufferedImage] = AWTBitmapFactory
  val canvasFactory: CanvasFactory[BufferedImage] = AWTCanvasFactory
  val soundFactory: SoundFactory = JVMSoundFactory
}
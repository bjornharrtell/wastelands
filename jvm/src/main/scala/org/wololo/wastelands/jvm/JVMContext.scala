package org.wololo.wastelands.jvm

import java.awt.image.BufferedImage
import org.wololo.wastelands.vmlayer._

trait JVMContext extends VMContext {
  def render(id: Int)
  val bitmapFactory: BitmapFactory = AWTBitmapFactory
  val canvasFactory: CanvasFactory = AWTCanvasFactory
  val soundFactory: SoundFactory = JVMSoundFactory
}
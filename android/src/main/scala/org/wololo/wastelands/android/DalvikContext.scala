package org.wololo.wastelands.android

import org.wololo.wastelands.vmlayer.BitmapFactory
import org.wololo.wastelands.vmlayer.SoundFactory
import org.wololo.wastelands.vmlayer.VMContext
import org.wololo.wastelands.vmlayer.CanvasFactory

trait DalvikContext extends VMContext {
  def bitmapFactory: BitmapFactory
  def soundFactory: SoundFactory
  val canvasFactory: CanvasFactory = AndroidCanvasFactory

  def render(id: Int)
}
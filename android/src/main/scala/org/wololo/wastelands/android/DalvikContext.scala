package org.wololo.wastelands.android

import org.wololo.wastelands.vmlayer.BitmapFactory
import org.wololo.wastelands.vmlayer.SoundFactory
import org.wololo.wastelands.vmlayer.VMContext
import org.wololo.wastelands.vmlayer.CanvasFactory

import android.graphics.Bitmap

trait DalvikContext extends VMContext {
  def render(id: Int)
  val bitmapFactory: BitmapFactory
  val canvasFactory: CanvasFactory = AndroidCanvasFactory
  val soundFactory: SoundFactory
}
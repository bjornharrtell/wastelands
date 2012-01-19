package org.wololo.wastelands.android

import org.wololo.wastelands.vmlayer.BitmapFactory
import org.wololo.wastelands.vmlayer.SoundFactory
import org.wololo.wastelands.vmlayer.VMContext
import org.wololo.wastelands.vmlayer.CanvasFactory

import android.graphics.Bitmap

trait DalvikContext extends VMContext[Bitmap] {
  def render(bitmap: Bitmap)
  val bitmapFactory: BitmapFactory[Bitmap]
  val canvasFactory: CanvasFactory[Bitmap] = AndroidCanvasFactory
  val soundFactory: SoundFactory
}
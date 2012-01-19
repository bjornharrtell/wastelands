package org.wololo.wastelands.android

import android.graphics.Bitmap
import org.wololo.wastelands.vmlayer._
import android.content.Context

trait DalvikContext extends VMContext[Bitmap] {
  def render(bitmap: Bitmap)
  val bitmapFactory: BitmapFactory[Bitmap] = AndroidBitmapFactory
  val canvasFactory: CanvasFactory[Bitmap] = AndroidCanvasFactory
  val soundFactory: SoundFactory = AndroidSoundFactory
}
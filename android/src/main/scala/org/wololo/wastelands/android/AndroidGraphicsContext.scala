package org.wololo.wastelands.android

import android.graphics.Bitmap
import org.wololo.wastelands.vmlayer.{CanvasFactory, BitmapFactory, GraphicsContext}


trait AndroidGraphicsContext extends GraphicsContext[Bitmap] {
  def render(bitmap: Bitmap)

  val bitmapFactory: BitmapFactory[Bitmap] = AndroidBitmapFactory
  val canvasFactory: CanvasFactory[Bitmap] = AndroidCanvasFactory
}
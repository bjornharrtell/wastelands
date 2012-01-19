package org.wololo.wastelands.android
import org.wololo.wastelands.vmlayer.Canvas
import org.wololo.wastelands.vmlayer.CanvasFactory

import android.graphics.Bitmap

object AndroidCanvasFactory extends CanvasFactory[Bitmap] {
  def create(bitmap: Bitmap): Canvas[Bitmap] = {
    new AndroidCanvas(new android.graphics.Canvas(bitmap.asInstanceOf[Bitmap]))
  }
}
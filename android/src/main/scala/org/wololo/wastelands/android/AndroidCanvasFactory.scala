package org.wololo.wastelands.android
import org.wololo.wastelands.vmlayer.Canvas
import org.wololo.wastelands.vmlayer.CanvasFactory

import android.graphics.Bitmap

object AndroidCanvasFactory extends CanvasFactory {
  def create(id: Int): Canvas = {
    new AndroidCanvas(new android.graphics.Canvas(AndroidBitmapFactory.bitmaps(id)))
  }
}
package org.wololo.wastelands.android
import org.wololo.wastelands.vmlayer.Canvas
import android.graphics.Bitmap
import org.wololo.wastelands.vmlayer.CanvasFactory

object AndroidCanvasFactory extends CanvasFactory[Bitmap] {
  def create(bitmap: Bitmap): Canvas[Bitmap] = {
    new AndroidCanvas(new android.graphics.Canvas(bitmap.asInstanceOf[Bitmap]))
  }
}
package org.wololo.wastelands.android
import org.wololo.wastelands.vmlayer.Canvas
import android.graphics.Bitmap
import org.wololo.wastelands.vmlayer.CanvasFactory

class AndroidCanvasFactory extends CanvasFactory {
  def create(bitmap: Object): Canvas = {
    new AndroidCanvas(new android.graphics.Canvas(bitmap.asInstanceOf[Bitmap]))
  }
}
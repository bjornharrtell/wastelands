package org.wololo.wastelands.android
import org.wololo.wastelands.vmlayer.BitmapFactory
import android.graphics.Bitmap
import java.io.InputStream
import org.wololo.wastelands.vmlayer.BitmapTypes
import android.graphics.Canvas
import android.graphics.Color

object AndroidBitmapFactory extends BitmapFactory[Bitmap] {
  def create(width: Int, height: Int, bitmapType: Int): Bitmap = {
    Bitmap.createBitmap(width, height, parseBitmapType(bitmapType))
  }
  def create(inputStream: InputStream): Bitmap = {
    android.graphics.BitmapFactory.decodeStream(inputStream)
  }
  def createShadow(bitmap: Bitmap): Bitmap = {
    val shadow = Bitmap.createBitmap(bitmap.getWidth, bitmap.getHeight, Bitmap.Config.ARGB_8888)
    new Canvas(shadow).drawColor(Color.argb(0, 0, 0, 0))

    for (
      y <- 0 until bitmap.getHeight;
      x <- 0 until bitmap.getHeight
    ) {
      shadow.setPixel(x, y, (bitmap.getPixel(x, y) & 0x6f000000) | (shadow.getPixel(x, y) & 0x00ffffff))
    }

    shadow
  }

  private def parseBitmapType(bitmapType: Int): Bitmap.Config = {
    bitmapType match {
      case BitmapTypes.Opague => Bitmap.Config.RGB_565
      case BitmapTypes.Bitmask => Bitmap.Config.ALPHA_8
      case BitmapTypes.Translucent => Bitmap.Config.ARGB_8888
    }
  }
}
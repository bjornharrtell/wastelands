package org.wololo.wastelands.android
import org.wololo.wastelands.vmlayer.BitmapFactory
import android.graphics.Bitmap
import java.io.InputStream
import org.wololo.wastelands.vmlayer.BitmapTypes

object AndroidBitmapFactory extends BitmapFactory[Bitmap] {
  def create(width: Int, height: Int, bitmapType: Int): Bitmap = {
    Bitmap.createBitmap(width, height, parseBitmapType(bitmapType))
  }
  def create(inputStream: InputStream): Bitmap = {
    android.graphics.BitmapFactory.decodeStream(inputStream)
  }
  def createShadow(bitmap: Bitmap) : Bitmap = {
    // TODO: do proper, this is fake impl. will produce transparent image
    Bitmap.createBitmap(bitmap.getWidth, bitmap.getHeight, Bitmap.Config.ARGB_8888)
  }
  
  private def parseBitmapType(bitmapType: Int) : Bitmap.Config = {
    bitmapType match {
      case BitmapTypes.Opague => Bitmap.Config.RGB_565
      case BitmapTypes.Bitmask => Bitmap.Config.ALPHA_8
      case BitmapTypes.Translucent => Bitmap.Config.ARGB_8888
    }
  }
}
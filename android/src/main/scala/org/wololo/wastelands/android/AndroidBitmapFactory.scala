package org.wololo.wastelands.android
import org.wololo.wastelands.vmlayer.BitmapFactory
import android.graphics.Bitmap
import java.io.InputStream
import org.wololo.wastelands.vmlayer.BitmapTypes

class AndroidBitmapFactory extends BitmapFactory {
  def create(width: Int, height: Int, bitmapType: Int): Object = {
    Bitmap.createBitmap(width, height, parseBitmapType(bitmapType))
  }
  def create(inputStream: InputStream): Object = {
    android.graphics.BitmapFactory.decodeStream(inputStream)
  }
  
  def parseBitmapType(bitmapType: Int) : Bitmap.Config = {
    bitmapType match {
      case BitmapTypes.Opague => Bitmap.Config.RGB_565
      case BitmapTypes.Bitmask => Bitmap.Config.ALPHA_8
      case BitmapTypes.Translucent => Bitmap.Config.ARGB_8888
    }
  }
}
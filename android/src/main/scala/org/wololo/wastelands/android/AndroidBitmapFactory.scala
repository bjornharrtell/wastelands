package org.wololo.wastelands.android
import java.io.File

import org.wololo.wastelands.vmlayer.BitmapFactory
import org.wololo.wastelands.vmlayer.BitmapTypes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color

class AndroidBitmapFactory(context: Context) extends BitmapFactory[Bitmap] {
  var assetManager = context.getAssets
  
  def create(width: Int, height: Int, bitmapType: Int): Bitmap = {
    Bitmap.createBitmap(width, height, parseBitmapType(bitmapType))
  }
  def create(file: File): Bitmap = {
    val inputStream = assetManager.open(file.getPath())
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
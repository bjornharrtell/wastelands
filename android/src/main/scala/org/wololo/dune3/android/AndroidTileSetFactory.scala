package org.wololo.dune3.android
import java.io.InputStream
import org.wololo.dune3.vmlayer.TileSetFactory
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect

class AndroidTileSetFactory(size: Int) extends TileSetFactory {

  def createTileFromFile(inputStream: InputStream): Object = {
    val image = BitmapFactory.decodeStream(inputStream)

    val tile = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
    val canvas = new Canvas(tile)
    canvas.drawBitmap(image, new Rect(0, 0, 16, 16), new Rect(0, 0, size, size), null)

    tile
  }

  def createTileSetFromFile(inputStream: InputStream, transparent: Boolean): Array[Object] = {
    val image = BitmapFactory.decodeStream(inputStream)

    val config = if (transparent) Bitmap.Config.ALPHA_8 else Bitmap.Config.RGB_565

    val tileSet = new Array[Object](5 * 18)

    var count = 0

    for (
      y <- 0 until 5;
      x <- 0 until 18
    ) {
      val tile = Bitmap.createBitmap(size, size, config)

      val sx1 = x * 16 + 1 + x
      val sy1 = y * 16 + 1 + y
      val sx2 = sx1 + 16
      val sy2 = sy1 + 16

      val canvas = new Canvas(tile)
      canvas.drawBitmap(image, new Rect(sx1, sy1, sx2, sy2), new Rect(0, 0, size, size), null)

      tileSet(count) = tile
      count += 1
    }

    tileSet
  }
}

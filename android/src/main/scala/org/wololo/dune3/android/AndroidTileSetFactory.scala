package org.wololo.dune3.android
import java.io.InputStream
import org.wololo.dune3.vmlayer.TileSetFactory
import org.wololo.dune3.vmlayer.Image
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect

class AndroidTileSetFactory(size: Int) extends TileSetFactory {

  def createTileFromFile(inputStream: InputStream): Image = {
    val image = BitmapFactory.decodeStream(inputStream)

    val tile = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = new Canvas(tile)
    canvas.drawBitmap(image, new Rect(0,0,16, 16), new Rect(0,0,size, size), null)

    new AndroidImage(tile)
  }

  def createTileSetFromFile(inputStream: InputStream, imageType: Int): Array[Image] = {
    val image = BitmapFactory.decodeStream(inputStream)

    val tileSet = new Array[Image](5 * 18)

    var count = 0

    for (
      y <- 0 until 5;
      x <- 0 until 18
    ) {
      val tile = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

      val sx1 = x * 16 + 1 + x
      val sy1 = y * 16 + 1 + y
      val sx2 = sx1 + 16
      val sy2 = sy1 + 16

      val canvas = new Canvas(tile)
      canvas.drawBitmap(image, new Rect(sx1, sy1, sx2, sy2), new Rect(0,0,size, size), null)

      tileSet(count) = new AndroidImage(tile)
      count += 1
    }

    tileSet
  }
}

package org.wololo.wastelands.core
import java.io.InputStream
import org.wololo.wastelands.vmlayer._
import scala.collection.mutable.ArrayBuffer

class TileSetFactory[T](graphicsContext: GraphicsContext[T], size: Int) {

  val bitmapFactory: BitmapFactory[T] = graphicsContext.bitmapFactory
  val canvasFactory: CanvasFactory[T] = graphicsContext.canvasFactory

  def createTileSetFromFile(inputStream: InputStream, bitmapType: Int, width: Int, height: Int): ArrayBuffer[T] = {
    val image = bitmapFactory.create(inputStream)

    val tileSet = new ArrayBuffer[T](height * width)
    var count = 0

    for (
      y <- 0 until height;
      x <- 0 until width
    ) {
      val tile: T = bitmapFactory.create(size, size, bitmapType)

      val sx1 = x * 16 + 1 + x
      val sy1 = y * 16 + 1 + y
      val sx2 = sx1 + 16
      val sy2 = sy1 + 16

      canvasFactory.create(tile).drawImage(image, 0, 0, size, size, sx1, sy1, sx2, sy2)

      tileSet.insert(count, tile)
      count += 1
    }

    tileSet
  }
}

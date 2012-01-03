package org.wololo.wastelands.core
import java.io.InputStream
import org.wololo.wastelands.vmlayer._

class TileSetFactory[T](graphicsContext: GraphicsContext[T]) {

  val bitmapFactory: BitmapFactory[T] = graphicsContext.bitmapFactory
  val canvasFactory: CanvasFactory[T] = graphicsContext.canvasFactory

  val size = 32
  
  def createTileFromFile(inputStream: InputStream): T = {
    val bitmap = bitmapFactory.create(inputStream)

    val tile = bitmapFactory.create(size, size, BitmapTypes.Opague)

    canvasFactory.create(tile).drawImage(bitmap, 0, 0, size, size, 0, 0, 16, 16)

    tile
  }
  
  def createMapTileSetFromFile[T : ClassManifest](inputStream: InputStream, bitmapType: Int): Array[T] = {
    createTileSetFromFile[T](inputStream, bitmapType, 18, 5)
  }
  
  def createUnitTileSetFromFile[T : ClassManifest](inputStream: InputStream, bitmapType: Int): Array[T] = {
    createTileSetFromFile[T](inputStream, bitmapType, 8, 1)
  }

  def createTileSetFromFile[T : ClassManifest](inputStream: InputStream, bitmapType: Int, width: Int, height: Int): Array[T] = {
    val image = bitmapFactory.create(inputStream)

    val tileSet: Array[T] = new Array[T](height * width)

    var count = 0

    for (
      y <- 0 until height;
      x <- 0 until width
    ) {
      val tile = bitmapFactory.create(size, size, bitmapType)

      val sx1 = x * 16 + 1 + x
      val sy1 = y * 16 + 1 + y
      val sx2 = sx1 + 16
      val sy2 = sy1 + 16

      canvasFactory.create(tile).drawImage(image, 0, 0, size, size, sx1, sy1, sx2, sy2)

      tileSet(count) = tile.asInstanceOf[T]
      count += 1
    }

    tileSet
  }
}

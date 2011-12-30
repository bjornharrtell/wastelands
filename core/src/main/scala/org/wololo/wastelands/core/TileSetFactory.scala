package org.wololo.wastelands.core
import java.io.InputStream
import java.lang.Object

import org.wololo.wastelands.vmlayer.BitmapFactory
import org.wololo.wastelands.vmlayer.BitmapTypes
import org.wololo.wastelands.vmlayer.CanvasFactory

class TileSetFactory(bitmapFactory: BitmapFactory, canvasFactory: CanvasFactory) {

  val size = 32
  
  def createTileFromFile(inputStream: InputStream): Object = {
    val image = bitmapFactory.create(inputStream)

    val tile = bitmapFactory.create(size, size, BitmapTypes.Opague)

    canvasFactory.create(tile).drawImage(image, 0, 0, size, size, 0, 0, 16, 16)

    tile
  }
  
  def createMapTileSetFromFile(inputStream: InputStream, bitmapType: Int): Array[Object] = {
    createTileSetFromFile(inputStream, bitmapType, 18, 5)
  }
  
  def createUnitTileSetFromFile(inputStream: InputStream, bitmapType: Int): Array[Object] = {
    createTileSetFromFile(inputStream, bitmapType, 7, 1)
  }

  def createTileSetFromFile(inputStream: InputStream, bitmapType: Int, width: Int, height: Int): Array[Object] = {
    val image = bitmapFactory.create(inputStream)

    val tileSet = new Array[Object](height * width)

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

      tileSet(count) = tile
      count += 1
    }

    tileSet
  }
}

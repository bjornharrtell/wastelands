package org.wololo.wastelands.core
import java.io.File

import scala.collection.mutable.ArrayBuffer

import org.wololo.wastelands.vmlayer.VMContext
import org.wololo.wastelands.vmlayer.BitmapFactory
import org.wololo.wastelands.vmlayer.CanvasFactory

class TileSetFactory[T](graphicsContext: VMContext[T]) {

  val bitmapFactory: BitmapFactory[T] = graphicsContext.bitmapFactory
  val canvasFactory: CanvasFactory[T] = graphicsContext.canvasFactory

  def createTileSetFromFile(file: File, bitmapType: Int, width: Int, height: Int, srcsize: Int = 16, dstsize: Int): ArrayBuffer[T] = {
    val image = bitmapFactory.create(file)

    val tileSet = new ArrayBuffer[T](height * width)
    var count = 0

    for (
      y <- 0 until height;
      x <- 0 until width
    ) {
      val tile: T = bitmapFactory.create(dstsize, dstsize, bitmapType)

      val sx1 = x * srcsize + 1 + x
      val sy1 = y * srcsize + 1 + y
      val sx2 = sx1 + srcsize
      val sy2 = sy1 + srcsize

      canvasFactory.create(tile).drawImage(image, 0, 0, dstsize, dstsize, sx1, sy1, sx2, sy2)

      tileSet.insert(count, tile)
      count += 1
    }

    tileSet
  }
}

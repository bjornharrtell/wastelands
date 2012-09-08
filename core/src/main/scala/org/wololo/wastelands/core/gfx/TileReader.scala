package org.wololo.wastelands.core.gfx

import java.io.InputStream
import collection.mutable.ArrayBuffer
import java.io.File

trait TileReader {
  self: { val screen: Screen } =>

  def fileToTiles(file: File, bitmapType: Int, width: Int, height: Int, srcsize: Int = 16, dstsize: Int): ArrayBuffer[Int] = {
    screen.tileSetFactory.createTileSetFromFile(file, bitmapType, width, height, srcsize, dstsize)
  }
}
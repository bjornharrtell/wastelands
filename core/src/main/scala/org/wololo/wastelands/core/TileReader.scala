package org.wololo.wastelands.core

import gfx.Screen
import java.io.InputStream
import collection.mutable.ArrayBuffer
import java.io.File

trait TileReader[T] {
  self: { val screen: Screen[T] } =>

  def fileToTiles(file: File, bitmapType: Int, width: Int, height: Int, srcsize: Int = 16, dstsize: Int): ArrayBuffer[T] = {
    screen.tileSetFactory.createTileSetFromFile(file, bitmapType, width, height, srcsize, dstsize)
  }
}
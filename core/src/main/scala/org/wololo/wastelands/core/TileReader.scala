package org.wololo.wastelands.core

import gfx.Screen
import java.io.InputStream
import collection.mutable.ArrayBuffer

trait TileReader[T] {
  self: { val screen: Screen[T] } =>

  private[this] def getResource(resourceName: String): InputStream = {
    getClass.getClassLoader.getResourceAsStream(resourceName)
  }

  def fileToTiles(resourceName: String, bitmapType: Int): ArrayBuffer[T] = {
    screen.tileSetFactory.createMapTileSetFromFile(getResource(resourceName), bitmapType)
  }

  def fileToTile(resourceName: String): T = {
    screen.tileSetFactory.createTileFromFile(getResource(resourceName))
  }
}
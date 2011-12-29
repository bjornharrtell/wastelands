package org.wololo.wastelands.vmlayer
import java.io.InputStream

trait TileSetFactory {
  def createTileFromFile(inputStream: InputStream): Object
  def createTileSetFromFile(inputStream: InputStream, transparent: Boolean): Array[Object]
}
package org.wololo.dune3.vmlayer
import java.io.InputStream

trait TileSetFactory {
  def createTileFromFile(inputStream: InputStream): Image
  def createTileSetFromFile(inputStream: InputStream, imageType: Int): Array[Image]
}
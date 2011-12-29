package org.wololo.wastelands.vmlayer
import java.io.InputStream

object BitmapTypes {
  val Opague = 0
  val Bitmask = 1
  val Translucent = 2
}

trait BitmapFactory {
  def create(width: Int, height: Int, bitmapType: Int): Object
  def create(inputStream: InputStream): Object
}
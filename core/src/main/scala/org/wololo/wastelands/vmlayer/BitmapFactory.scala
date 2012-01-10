package org.wololo.wastelands.vmlayer
import java.io.InputStream

object BitmapTypes {
  val Opague = 0
  val Bitmask = 1
  val Translucent = 2
}

trait BitmapFactory[T] {
  def create(width: Int, height: Int, bitmapType: Int): T
  def create(inputStream: InputStream): T
  def createShadow(bitmap: T): T
}
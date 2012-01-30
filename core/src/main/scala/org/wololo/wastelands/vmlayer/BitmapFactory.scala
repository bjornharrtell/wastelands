package org.wololo.wastelands.vmlayer
import java.io.File

object BitmapTypes {
  val Opague = 0
  val Bitmask = 1
  val Translucent = 2
}

trait BitmapFactory {
  def create(width: Int, height: Int, bitmapType: Int): Int
  def create(file: File): Int
  def createShadow(id: Int): Int
}
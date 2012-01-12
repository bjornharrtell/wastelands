package org.wololo.wastelands.core

object Rect {
  implicit def tuple2Rect(tuple: (Int, Int, Int, Int)) : Rect = new Rect(tuple._1, tuple._2, tuple._3, tuple._4)
}

class Rect(var x1: Int, var y1: Int, var x2: Int, var y2: Int) {
  def contains(coordinate: Coordinate) : Boolean = {
    if (coordinate.x >= x1 && coordinate.x <= x2 && coordinate.y >= y1 && coordinate.y <= y2) true else false
  }
}
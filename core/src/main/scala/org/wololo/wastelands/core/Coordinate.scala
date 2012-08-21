package org.wololo.wastelands.core
import org.wololo.wastelands.core.unit.Direction

object Coordinate {
  implicit def tuple2Coordinate(tuple: (Int, Int)): Coordinate = Coordinate(tuple._1, tuple._2)
  implicit def coordinate2Tuple(coordinate: Coordinate): (Int, Int) = (coordinate.x, coordinate.y)
}

case class Coordinate(x: Int, y: Int) {
  def +(coordinate: Coordinate): Coordinate = (x + coordinate.x, y + coordinate.y)
  def +(direction: Direction): Coordinate = (x + direction.x, y + direction.y)
  def -(coordinate: Coordinate): Coordinate = (x - coordinate.x, y - coordinate.y)
  def *(coordinate: Coordinate): Coordinate = (x * coordinate.x, y * coordinate.y)
  def *(scalar: Int): Coordinate = (x * scalar, y * scalar)
  def *(scalar: Float): Coordinate = ((x * scalar).toInt, (y * scalar).toInt)

  def distance(coordinate: Coordinate): Int = {
    math.sqrt(math.pow(coordinate.x - x, 2) + math.pow(coordinate.y - y, 2)).toInt
  }
}
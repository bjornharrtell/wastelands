package org.wololo.wastelands.core

object Coordinate {
  implicit def tuple2Coordinate(tuple: (Int, Int)): Coordinate = new Coordinate(tuple._1, tuple._2)
}

class Coordinate(val x: Int, val y: Int) {
  def ==(tuple: (Int, Int)) = x == tuple._1 && y == tuple._2
  def !=(tuple: (Int, Int)) = !(this == tuple)
  def ==(coordinate: Coordinate) = x == coordinate.x && y == coordinate.y
  def !=(coordinate: Coordinate) = !(this == coordinate)
  def +(coordinate: Coordinate): Coordinate = (x + coordinate.x, y + coordinate.y)
  def -(coordinate: Coordinate): Coordinate = (x - coordinate.x, y - coordinate.y)

  def distance(coordinate: Coordinate): Int = {
    math.sqrt(math.pow(coordinate.x - x, 2) + math.pow(coordinate.y - y, 2)).toInt
  }

  def copy: Coordinate = (x, y)
  
  override def toString: String = {
    "["+x+","+y+"]"
  }
  
  def equals(other: Coordinate): Boolean = {
    return this == other
  }
}
package org.wololo.wastelands.core

object Coordinate {
  implicit def tuple2Coordinate(tuple: (Int, Int)): Coordinate = new Coordinate(tuple._1, tuple._2)
}

class Coordinate(var x: Int, var y: Int) {
  def ==(tuple: (Int, Int)) = x == tuple._1 && y == tuple._2
  def !=(tuple: (Int, Int)) = !(this == tuple)
  def ==(coordinate: Coordinate) = x == coordinate.x && y == coordinate.y
  def !=(coordinate: Coordinate) = !(this == coordinate)
  def +=(coordinate: Coordinate) = offset(coordinate.x, coordinate.y)
  def +(coordinate: Coordinate): Coordinate = (x + coordinate.x, y + coordinate.y)
  def -(coordinate: Coordinate): Coordinate = (x - coordinate.x, y - coordinate.y)

  def setTo(coordinate: Coordinate) = {
    x = coordinate.x
    y = coordinate.y
  }
  
  def setTo(x: Int, y: Int) = {
    this.x = x
    this.y = y
  }
  
  override def clone: Coordinate = (x, y)

  private def offset(dx: Int, dy: Int) {
    x += dx
    y += dy
  }
}
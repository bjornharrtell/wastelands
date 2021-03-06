package org.wololo.wastelands.vmlayer

trait Canvas {
  def drawImage(id: Int, x: Int, y: Int)
  def drawImage(id: Int, dx1: Int, dy1: Int, dx2: Int, dy2: Int, sx1: Int, sy1: Int, sx2: Int, sy2: Int)
  def drawRect(x1: Int, y1: Int, x2: Int, y2: Int)
  def clearRect(x1: Int, y1: Int, x2: Int, y2: Int)
}
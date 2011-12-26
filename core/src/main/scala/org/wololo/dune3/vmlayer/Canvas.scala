package org.wololo.dune3.vmlayer

trait Canvas {
  def drawImage(image: Image, dx1: Int, dy1: Int, dx2: Int, dy2: Int, sx1: Int, sy1: Int, sx2: Int, sy2: Int)
  def show()
  def dispose()
}
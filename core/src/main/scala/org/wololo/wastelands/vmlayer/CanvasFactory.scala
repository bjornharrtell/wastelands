package org.wololo.wastelands.vmlayer

trait CanvasFactory[T] {
  def create(bitmap: T): Canvas[T]
}
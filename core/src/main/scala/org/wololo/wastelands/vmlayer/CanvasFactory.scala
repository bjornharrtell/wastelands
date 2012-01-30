package org.wololo.wastelands.vmlayer

trait CanvasFactory {
  def create(id: Int): Canvas
}
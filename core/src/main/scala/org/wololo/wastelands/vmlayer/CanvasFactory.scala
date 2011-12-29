package org.wololo.wastelands.vmlayer

trait CanvasFactory {
  def create(bitmap: Object): Canvas
}
package org.wololo.wastelands.vmlayer

trait GraphicsContext {
  def render(bitmap: Object)
  def bitmapFactory(): BitmapFactory
  def canvasFactory(): CanvasFactory
}
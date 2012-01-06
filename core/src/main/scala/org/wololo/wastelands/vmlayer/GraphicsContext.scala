package org.wololo.wastelands.vmlayer

trait GraphicsContext[T] {
  def render(bitmap: T)
  def bitmapFactory: BitmapFactory[T]
  def canvasFactory: CanvasFactory[T]
  var screenWidth = 640
  var screenHeight = 480
}
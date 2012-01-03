package org.wololo.wastelands.vmlayer

trait GraphicsContext[T] {
  def render(bitmap: T)
  val bitmapFactory: BitmapFactory[T]
  val canvasFactory: CanvasFactory[T]
}
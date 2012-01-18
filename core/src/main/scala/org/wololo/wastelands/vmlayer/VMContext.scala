package org.wololo.wastelands.vmlayer

trait VMContext[T] {
  def render(bitmap: T)
  def bitmapFactory: BitmapFactory[T]
  def canvasFactory: CanvasFactory[T]
  def soundFactory: SoundFactory
  var screenWidth = 640
  var screenHeight = 400
}
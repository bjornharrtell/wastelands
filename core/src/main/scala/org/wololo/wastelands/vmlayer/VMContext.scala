package org.wololo.wastelands.vmlayer

trait VMContext {
  def render(id: Int)
  def bitmapFactory: BitmapFactory
  def canvasFactory: CanvasFactory
  def soundFactory: SoundFactory
  def resourceFactory: ResourceFactory
  var screenWidth = 640
  var screenHeight = 400
}
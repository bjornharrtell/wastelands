package org.wololo.wastelands.vmlayer

trait Context {
  def getCanvas(): Canvas
  def disposeCanvas()
}
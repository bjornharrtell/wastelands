package org.wololo.wastelands.vmlayer
import java.io.File

trait SoundFactory {
  def create(file: File): Sound
}
package org.wololo.wastelands.vmlayer
import java.io.InputStream
import java.io.File
import java.net.URL

trait SoundFactory {
  def create(filename: String): Sound
}
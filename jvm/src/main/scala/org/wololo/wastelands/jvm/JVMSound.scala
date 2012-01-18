package org.wololo.wastelands.jvm
import org.wololo.wastelands.vmlayer.Sound

import paulscode.sound.SoundSystem

class JVMSound(soundSystem: SoundSystem, string: String) extends Sound {
  def play() {
    soundSystem.activate(string)
    soundSystem.play(string)
  }
}
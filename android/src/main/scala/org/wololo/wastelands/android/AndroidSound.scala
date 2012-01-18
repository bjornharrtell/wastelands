package org.wololo.wastelands.android
import org.wololo.wastelands.vmlayer.Sound
import android.media.MediaPlayer
import android.media.SoundPool

class AndroidSound(soundPool: SoundPool, id: Int) extends Sound {
  def play() {
    soundPool.play(id, 1, 1, 0, 0, 1)
  }
}
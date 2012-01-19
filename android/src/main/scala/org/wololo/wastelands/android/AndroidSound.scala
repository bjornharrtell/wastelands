package org.wololo.wastelands.android
import org.wololo.wastelands.vmlayer.Sound

import android.media.AudioManager
import android.media.SoundPool

class AndroidSound(audioManager: AudioManager, soundPool: SoundPool, id: Int) extends Sound {
  def play() {
    val streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    val streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val volume = streamVolume / streamMaxVolume

    soundPool.play(id, volume, volume, 1, 0, 1)
  }
}
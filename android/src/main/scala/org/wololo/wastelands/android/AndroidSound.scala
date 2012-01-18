package org.wololo.wastelands.android
import org.wololo.wastelands.vmlayer.Sound
import android.media.MediaPlayer
import android.media.SoundPool
import android.media.AudioManager

class AndroidSound(audioManager: AudioManager, soundPool: SoundPool, id: Int) extends Sound {
  def play() {
    val streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    val streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val volume = streamVolume / streamMaxVolume

    soundPool.play(id, volume, volume, 1, 0, 1)
  }
}
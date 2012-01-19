package org.wololo.wastelands.android
import java.io.File

import org.wololo.wastelands.vmlayer.Sound
import org.wololo.wastelands.vmlayer.SoundFactory

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool

class AndroidSoundFactory(context: Context) extends SoundFactory {
  val audioManager = context.getSystemService(Context.AUDIO_SERVICE).asInstanceOf[AudioManager]
  val soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0)
  var assetManager = context.getAssets

  def create(file: File): Sound = {
    val fd = assetManager.openFd(file.getPath())

    val id = soundPool.load(fd, 1)
    fd.close

    new AndroidSound(audioManager, soundPool, id)
  }
}
package org.wololo.wastelands.android
import org.wololo.wastelands.vmlayer.SoundFactory
import java.io.InputStream
import org.wololo.wastelands.vmlayer.Sound
import java.net.URL
import android.media.MediaPlayer
import android.content.Context
import android.content.res.AssetManager
import android.media.SoundPool
import android.media.AudioManager
import java.io.File

object AndroidSoundFactory extends SoundFactory {
  val audioManager = Activity.getSystemService(Context.AUDIO_SERVICE).asInstanceOf[AudioManager]
  val soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0)
  var assetManager = Activity.getAssets
  
  def create(file: File): Sound = {
    val fd = assetManager.openFd(file.getPath())

    val id = soundPool.load(fd, 1)
    fd.close
    
    new AndroidSound(audioManager, soundPool, id)
  }
}
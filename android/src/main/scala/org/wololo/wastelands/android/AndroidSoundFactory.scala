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

class AndroidSoundFactory(context: Context) extends SoundFactory {
  val audioManager = context.getSystemService(Context.AUDIO_SERVICE).asInstanceOf[AudioManager]
  val soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0)
  
  def create(filename: String): Sound = {
    
    val path = "sounds/" + filename
    val fd = context.getAssets.openFd(path)

    val id = soundPool.load(fd, 1)
    fd.close
    
    new AndroidSound(audioManager, soundPool, id)
  }
}
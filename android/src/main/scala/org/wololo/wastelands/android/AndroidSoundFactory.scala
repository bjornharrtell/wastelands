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

class AndroidSoundFactory(assetManager: AssetManager) extends SoundFactory {
  val soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0)
  
  def create(filename: String): Sound = {
    
    val path = "sounds/" + filename
    val fd = assetManager.openFd(path)
    
    val id = soundPool.load(fd, 1)
    
    new AndroidSound(soundPool, id)
  }
}
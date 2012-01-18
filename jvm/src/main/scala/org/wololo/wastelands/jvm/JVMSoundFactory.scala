package org.wololo.wastelands.jvm

import org.wololo.wastelands.vmlayer.Sound
import org.wololo.wastelands.vmlayer.SoundFactory

import paulscode.sound.codecs.CodecJOrbis
import paulscode.sound.libraries.LibraryLWJGLOpenAL
import paulscode.sound.SoundSystem
import paulscode.sound.SoundSystemConfig

object JVMSoundFactory extends SoundFactory {
  SoundSystemConfig.addLibrary(classOf[LibraryLWJGLOpenAL])
  SoundSystemConfig.setCodec("ogg", classOf[CodecJOrbis])
  val soundSystem = new SoundSystem

  var counter = 0

  def create(filename: String): Sound = {
    val sourcename = filename + counter
    val path = "sounds/" + filename
    var url = getClass.getClassLoader.getResource(path)

    soundSystem.newSource(false, sourcename, url, filename, false, 0, 0, 0, SoundSystemConfig.ATTENUATION_ROLLOFF, SoundSystemConfig.getDefaultRolloff)

    counter += 1

    new JVMSound(soundSystem, sourcename)
  }
}
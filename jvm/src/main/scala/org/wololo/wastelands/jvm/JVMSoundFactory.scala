package org.wololo.wastelands.jvm

import org.wololo.wastelands.vmlayer.Sound
import org.wololo.wastelands.vmlayer.SoundFactory
import paulscode.sound.codecs.CodecJOrbis
import paulscode.sound.libraries.LibraryLWJGLOpenAL
import paulscode.sound.SoundSystem
import paulscode.sound.SoundSystemConfig
import java.io.File

object JVMSoundFactory extends SoundFactory {
  SoundSystemConfig.addLibrary(classOf[LibraryLWJGLOpenAL])
  SoundSystemConfig.setCodec("ogg", classOf[CodecJOrbis])
  val soundSystem = new SoundSystem

  var counter = 0

  def create(file: File): Sound = {
    val sourcename = file.getName + counter

    soundSystem.newSource(
      false,
      sourcename,
      file.toURL,
      file.getName,
      false,
      0,
      0,
      0,
      SoundSystemConfig.ATTENUATION_ROLLOFF,
      SoundSystemConfig.getDefaultRolloff)

    counter += 1

    new JVMSound(soundSystem, sourcename)
  }
}
package org.wololo.wastelands.jvm

import java.io.InputStream
import java.io.FileInputStream
import java.io.File
import java.io.OutputStream
import java.io.FileOutputStream
import org.wololo.wastelands.vmlayer.ResourceFactory

object JVMResourceFactory extends ResourceFactory {
  def getInputStream(file: File): InputStream = {
    new FileInputStream(file)
  }
  def getOutputStream(file: File): OutputStream = {
    new FileOutputStream(file)
  }
}
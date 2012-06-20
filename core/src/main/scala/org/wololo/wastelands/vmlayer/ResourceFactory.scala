package org.wololo.wastelands.vmlayer

import java.io.File
import java.io.OutputStream
import java.io.InputStream

trait ResourceFactory {
  def getInputStream(file: File): InputStream
  def getOutputStream(file: File): OutputStream
}
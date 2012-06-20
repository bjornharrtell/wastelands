package org.wololo.wastelands.android

import org.wololo.wastelands.vmlayer.ResourceFactory
import java.io.InputStream
import android.content.Context
import java.io.File
import java.io.OutputStream

class AndroidResourceFactory(context: Context) extends ResourceFactory {
  val assetManager = context.getAssets
  
  def getInputStream(file: File): InputStream = {
    assetManager.open(file.getPath)
  }
  def getOutputStream(file: File): OutputStream = {
    null
  }
}
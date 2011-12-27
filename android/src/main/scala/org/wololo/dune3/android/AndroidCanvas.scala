package org.wololo.dune3.android
import org.wololo.dune3.android.AndroidImage
import org.wololo.dune3.vmlayer.Canvas
import org.wololo.dune3.vmlayer.Image

import android.graphics.Rect

class AndroidCanvas(canvas: android.graphics.Canvas) extends Canvas {
  
  def drawImage(image: Image, x: Int, y: Int) {
    val androidImage: AndroidImage = image.asInstanceOf[AndroidImage]
    canvas.drawBitmap(androidImage.bitmap, null, new Rect(x, y, x+32, y+32), null)
  }
  
  def drawImage(image: Image, dx1: Int, dy1: Int, dx2: Int, dy2: Int, sx1: Int, sy1: Int, sx2: Int, sy2: Int) {
    val androidImage: AndroidImage = image.asInstanceOf[AndroidImage]
    canvas.drawBitmap(androidImage.bitmap, null /*new Rect(sx1, sy1, sx2, sy2)*/, new Rect(dx1, dy1, dx2, dy2), null)
  }

  def show() {
    
  }
  
  def dispose() {
    
  }
}
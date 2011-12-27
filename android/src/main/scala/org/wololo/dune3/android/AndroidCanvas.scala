package org.wololo.dune3.android
import org.wololo.dune3.vmlayer.Canvas
import android.graphics.Rect
import android.graphics.Bitmap

class AndroidCanvas(canvas: android.graphics.Canvas) extends Canvas {
  
  def drawImage(image: Object, x: Int, y: Int) {
    canvas.drawBitmap(image.asInstanceOf[Bitmap], x, y, null)
  }
  
  def drawImage(image: Object, dx1: Int, dy1: Int, dx2: Int, dy2: Int, sx1: Int, sy1: Int, sx2: Int, sy2: Int) {
    canvas.drawBitmap(image.asInstanceOf[Bitmap], null /*new Rect(sx1, sy1, sx2, sy2)*/, new Rect(dx1, dy1, dx2, dy2), null)
  }

  def show() {
    
  }
  
  def dispose() {
    
  }
}
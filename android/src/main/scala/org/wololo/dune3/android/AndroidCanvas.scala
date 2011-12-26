package org.wololo.dune3.android
import org.wololo.dune3.vmlayer.Canvas
import java.awt.Graphics
import org.wololo.dune3.vmlayer.Image
import java.awt.image.BufferStrategy
import android.graphics.Rect
import android.view.SurfaceHolder

class AndroidCanvas(surfaceHolder: SurfaceHolder  ) extends Canvas {

  val androidCanvas: android.graphics.Canvas = surfaceHolder.lockCanvas(null);
  
  def drawImage(image: Image, dx1: Int, dy1: Int, dx2: Int, dy2: Int, sx1: Int, sy1: Int, sx2: Int, sy2: Int) {
    val androidImage: AndroidImage = image.asInstanceOf[AndroidImage]
    androidCanvas.drawBitmap(androidImage.bitmap, new Rect(sx1, sy1, sx2, sy2), new Rect(dx1, dy1, dx2, dy2), null)
  }

  def show() {
    surfaceHolder.unlockCanvasAndPost(androidCanvas);
  }
  
  def dispose() {
    
  }
}
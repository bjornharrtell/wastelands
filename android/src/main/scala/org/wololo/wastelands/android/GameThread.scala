package org.wololo.wastelands.android
import org.wololo.wastelands.core.Game
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.view.SurfaceHolder
import org.wololo.wastelands.vmlayer.FrameRenderer
import org.wololo.wastelands.vmlayer.GraphicsContext

class GameThread extends Thread with SurfaceHolder.Callback with GraphicsContext {

  val bitmapFactory = new AndroidBitmapFactory()
  val canvasFactory = new AndroidCanvasFactory()
  
  var running = false

  val game: Game = new Game(this)

  var surfaceHolder: SurfaceHolder = null

  override def run() {
    game.run()
  }
  
  def render(bitmap: Object) {
    val canvas = surfaceHolder.lockCanvas(null)
    canvas.drawBitmap(bitmap.asInstanceOf[Bitmap], 0, 0, null)
    surfaceHolder.unlockCanvasAndPost(canvas)
  }

  def surfaceCreated(holder: SurfaceHolder) {
    surfaceHolder = holder
    running = true
    start()
  }

  def surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

  }

  def surfaceDestroyed(holder: SurfaceHolder) {
    var retry = true
    running = false
    while (retry) {
      try {
        join()
        retry = false
      } catch {
        case e: InterruptedException =>
      }
    }
  }
}
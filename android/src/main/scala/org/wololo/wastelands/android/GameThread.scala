package org.wololo.wastelands.android
import org.wololo.wastelands.core.Game

import android.content.Context
import android.graphics.Bitmap
import android.view.SurfaceHolder

class GameThread(context: Context) extends Thread with SurfaceHolder.Callback with DalvikContext {

  var running = false

  val bitmapFactory = new AndroidBitmapFactory(context)
  val soundFactory = new AndroidSoundFactory(context)

  var game: Game = null

  var surfaceHolder: SurfaceHolder = null

  override def run() {
    game.run()
  }

  def render(id: Int) {
    val canvas = surfaceHolder.lockCanvas(null)
    canvas.drawBitmap(AndroidBitmapFactory.bitmaps(id), 0, 0, null)
    surfaceHolder.unlockCanvasAndPost(canvas)
  }

  def surfaceCreated(holder: SurfaceHolder) {
    surfaceHolder = holder
    screenWidth = holder.getSurfaceFrame.width()
    screenHeight = holder.getSurfaceFrame.height()
    game = new Game(this)
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
package org.wololo.wastelands.android

import org.wololo.wastelands.core.event.Touch
import org.wololo.wastelands.core.client.ClientApp
import org.wololo.wastelands.core.Coordinate

import android.content.Context
import android.view.MotionEvent
import android.view.SurfaceHolder

class GameThread(context: Context) extends Thread with SurfaceHolder.Callback with DalvikContext with ClientApp  {
  val bitmapFactory = new AndroidBitmapFactory(context)
  val soundFactory = new AndroidSoundFactory(context)
  val resourceFactory = new AndroidResourceFactory(context)

  var surfaceHolder: SurfaceHolder = null

  override def run() {
    super.run()
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
  
  def handleAction(action: Int, x: Int, y: Int) {
    action match {
      case MotionEvent.ACTION_DOWN => client ! Touch(x, y, Touch.DOWN)
      case MotionEvent.ACTION_UP => client ! Touch(x, y, Touch.UP)
      case MotionEvent.ACTION_MOVE => client ! Touch(x, y, Touch.MOVE)
    }
  }
}
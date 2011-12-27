package org.wololo.dune3.android
import android.view.SurfaceHolder
import android.graphics.Canvas
import org.wololo.dune3.core.Game
import org.wololo.dune3.vmlayer.Context
import android.graphics.Bitmap
import android.graphics.Rect

class GameThread extends Thread with SurfaceHolder.Callback with Context {

  var running = false

  val game: Game = new Game(new AndroidTileSetFactory(32), this)

  var surfaceHolder: SurfaceHolder = null
  
  var canvas: Canvas = null
  
  val board = Bitmap.createBitmap(32*16, 32*16, Bitmap.Config.RGB_565)
  val boardCanvas = new Canvas(board);

  override def run() {
    game.run()
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

  override def getCanvas(): org.wololo.dune3.vmlayer.Canvas = {
    new AndroidCanvas(boardCanvas)
  }
  
  override def disposeCanvas() {
    canvas = surfaceHolder.lockCanvas(null)
    canvas.drawBitmap(board, null, new Rect(0,0,32*16,32*16), null)
    surfaceHolder.unlockCanvasAndPost(canvas)
  }
}
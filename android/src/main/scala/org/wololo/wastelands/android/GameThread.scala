package org.wololo.wastelands.android
import org.wololo.dune3.core.Game
import org.wololo.dune3.vmlayer.Context

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.view.SurfaceHolder

class GameThread extends Thread with SurfaceHolder.Callback with Context {

  var running = false

  val game: Game = new Game(new AndroidTileSetFactory(32), this)

  var surfaceHolder: SurfaceHolder = null

  var canvas: Canvas = null

  val board = Bitmap.createBitmap(game.Width, game.Height, Bitmap.Config.RGB_565)
  val boardCanvas = new AndroidCanvas(new Canvas(board))

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

  def getCanvas(): org.wololo.dune3.vmlayer.Canvas = {
    boardCanvas
  }

  def disposeCanvas() {
    canvas = surfaceHolder.lockCanvas(null)
    canvas.drawBitmap(board, null, new Rect(0, 0, game.Width, game.Height), null)
    surfaceHolder.unlockCanvasAndPost(canvas)
  }
}
package org.wololo.dune3.android
import android.view.SurfaceHolder
import android.graphics.Canvas
import org.wololo.dune3.core.Game
import org.wololo.dune3.vmlayer.Context

class GameThread extends Thread with SurfaceHolder.Callback with Context {

  var running = false

  val game: Game = new Game(new AndroidTileSetFactory(32), this)

  var surfaceHolder: SurfaceHolder = null

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
    new AndroidCanvas(surfaceHolder)
  }
}
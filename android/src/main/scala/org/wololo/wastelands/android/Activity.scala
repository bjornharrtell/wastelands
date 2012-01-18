package org.wololo.wastelands.android

import android.os.Bundle
import android.view.View.OnTouchListener
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View

class Activity extends android.app.Activity with OnTouchListener {
  var gameThread: GameThread = null

  var prevX = 0
  var prevY = 0

  var tdx = 0
  var tdy = 0

  val ClickTolerance = 3

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    val surfaceView = findViewById(R.id.surfaceView1).asInstanceOf[SurfaceView]

    gameThread = new GameThread(this)

    surfaceView.getHolder.addCallback(gameThread)

    surfaceView.setOnTouchListener(this)
  }

  def onTouch(view: View, motionEvent: MotionEvent): Boolean = {

    val action = motionEvent.getAction

    if (action == MotionEvent.ACTION_CANCEL) {
      return true;
    }

    val historySize = motionEvent.getHistorySize

    // TODO: handle multitouch
    // final int pointerCount = motionEvent.getPointerCount();

    val p = 0

    for (h <- 0 until historySize) {
      val x = motionEvent.getHistoricalX(p, h).toInt
      val y = motionEvent.getHistoricalY(p, h).toInt

      if (action == MotionEvent.ACTION_DOWN) {
        prevX = x
        prevY = y
        tdx = 0
        tdy = 0
      } else if (action == MotionEvent.ACTION_UP) {
        if (tdx < ClickTolerance && tdy < ClickTolerance) {
          gameThread.game.click(x, y)
        }
      }

      val dx = prevX - x
      val dy = prevY - y

      tdx += dx
      tdy += dy

      gameThread.game.scroll(dx, dy)

      prevX = x
      prevY = y
    }

    val x = motionEvent.getX(p).toInt
    val y = motionEvent.getY(p).toInt

    if (action == MotionEvent.ACTION_DOWN) {
      prevX = x
      prevY = y
      tdx = 0
      tdy = 0
    } else if (action == MotionEvent.ACTION_UP) {
      if (tdx < ClickTolerance && tdy < ClickTolerance) {
        gameThread.game.click(x, y)
      }
    }

    val dx = prevX - x
    val dy = prevY - y

    gameThread.game.scroll(dx, dy)

    prevX = x
    prevY = y

    // sleep 16 milliseconds to avoid too much input CPU processing
    // goal is to get slightly more input events than target FPS which is 60
    Thread.sleep(16L)

    true
  }
}

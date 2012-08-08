package org.wololo.wastelands.android

import android.media.AudioManager
import android.os.Bundle
import android.view.View.OnTouchListener
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import android.view.Window

class Activity extends android.app.Activity with OnTouchListener {
  var gameThread: GameThread = null

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    
    setVolumeControlStream(AudioManager.STREAM_MUSIC)
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    
    setContentView(R.layout.main)

    val surfaceView = findViewById(R.id.surfaceView1).asInstanceOf[SurfaceView]

    gameThread = new GameThread(this)

    surfaceView.getHolder.addCallback(gameThread)

    surfaceView.setOnTouchListener(this)
  }

  def onTouch(view: View, motionEvent: MotionEvent): Boolean = {

    val action = motionEvent.getAction

    if (action == MotionEvent.ACTION_CANCEL) return true

    val historySize = motionEvent.getHistorySize

    // TODO: handle multitouch
    // final int pointerCount = motionEvent.getPointerCount();

    val p = 0

    for (h <- 0 until historySize) {
      val x = motionEvent.getHistoricalX(p, h).toInt
      val y = motionEvent.getHistoricalY(p, h).toInt
      gameThread.handleAction(action, (x, y))
    }

    val x = motionEvent.getX(p).toInt
    val y = motionEvent.getY(p).toInt
    gameThread.handleAction(action, (x, y))

    // sleep 16 milliseconds to avoid too much input CPU processing
    // goal is to get slightly more input events than target FPS which is 60
    Thread.sleep(16L)

    true
  }
  
}

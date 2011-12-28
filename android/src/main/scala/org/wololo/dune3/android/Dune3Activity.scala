package org.wololo.dune3.android

import android.app.Activity
import android.os.Bundle
import android.view.SurfaceView
import android.view.View.OnTouchListener
import android.view.View
import android.view.MotionEvent

class Dune3Activity extends Activity with OnTouchListener {
  val gameThread = new GameThread()

  var prevX = 0
  var prevY = 0

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    val surfaceView: SurfaceView = findViewById(R.id.surfaceView1).asInstanceOf[SurfaceView]

    surfaceView.getHolder().addCallback(gameThread);

    surfaceView.setOnTouchListener(this);
  }

  override def onTouch(view: View, motionEvent: MotionEvent): Boolean = {

    val action = motionEvent.getAction();

    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
      return true;
    }

    //val historySize = motionEvent.getHistorySize();

    // TODO: handle multitouch
    // final int pointerCount = motionEvent.getPointerCount();

    val p = 0;

    /*for (h <- 0 until historySize) {
      val x = motionEvent.getHistoricalX(p, h).toInt
      val y = motionEvent.getHistoricalY(p, h).toInt

      // TODO: use historical data
      if (action == MotionEvent.ACTION_DOWN) {
    	  prevX = x
    	  prevY = y
      }
      
      val dx = prevX - x
      val dy = prevY - y
      
      gameThread.game.move(dx, dy)
      
      prevX = x
      prevY = y
    }*/

    val x = motionEvent.getX(p).toInt
    val y = motionEvent.getY(p).toInt
    
    if (action == MotionEvent.ACTION_DOWN) {
      prevX = x
      prevY = y
    }

    val dx = prevX - x
    val dy = prevY - y

    gameThread.game.move(dx, dy)

    prevX = x
    prevY = y

    try {
      Thread.sleep(16L);
    } catch {
      case e: InterruptedException =>
      // ignore interruptions
    }

    return true;
  }
}

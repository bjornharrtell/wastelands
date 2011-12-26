package org.wololo.dune3.android

import android.app.Activity
import android.os.Bundle
import android.view.SurfaceView

class Dune3Activity extends Activity {
  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
    
    val surfaceView : SurfaceView= findViewById(R.id.surfaceView1).asInstanceOf[SurfaceView];
    
    val gameThread = new GameThread();
	surfaceView.getHolder().addCallback(gameThread);
  }
}

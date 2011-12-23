package org.wololo.dune3.android

import android.app.Activity
import android.os.Bundle

class Dune3Activity extends Activity {
  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
  }
}

package org.wololo.dune3.android
import org.wololo.dune3.vmlayer.Image
import android.graphics.Bitmap

class AndroidImage(bitmap: Bitmap) extends Image {
 def bitmap() : Bitmap = { bitmap }
}
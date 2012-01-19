package org.wololo.wastelands.android
import org.wololo.wastelands.vmlayer.Canvas

import android.graphics.Paint.Style
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class AndroidCanvas(canvas: android.graphics.Canvas) extends Canvas[Bitmap] {

  val yellowPaint = new Paint()
  yellowPaint.setColor(Color.YELLOW)
  yellowPaint.setStyle(Style.STROKE)
  yellowPaint.setStrokeWidth(1)
  val blackPaint = new Paint()
  blackPaint.setColor(Color.BLACK)

  def drawImage(image: Bitmap, x: Int, y: Int) {
    canvas.drawBitmap(image.asInstanceOf[Bitmap], x, y, null)
  }

  def drawImage(image: Bitmap, dx1: Int, dy1: Int, dx2: Int, dy2: Int, sx1: Int, sy1: Int, sx2: Int, sy2: Int) {
    canvas.drawBitmap(image.asInstanceOf[Bitmap], new Rect(sx1, sy1, sx2, sy2), new Rect(dx1, dy1, dx2, dy2), null)
  }

  def drawRect(x1: Int, y1: Int, x2: Int, y2: Int) {
    canvas.drawRect(x1, y1, x2, y2, yellowPaint)
  }

  def clearRect(x1: Int, y1: Int, x2: Int, y2: Int) {
    canvas.drawRect(x1, y1, x2, y2, blackPaint)
  }

  def show() {

  }

  def dispose() {

  }
}
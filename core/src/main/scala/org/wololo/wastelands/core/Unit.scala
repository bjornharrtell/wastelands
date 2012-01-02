package org.wololo.wastelands.core
import org.wololo.wastelands.vmlayer.Canvas

/**
 * TODO: probably needs refactoring for more intelligent rendering with other parts
 */
class Unit(map: Map, var x: Int, var y: Int, tileSet: Array[Object]) {

  map.removeShadeAround(x, y)

  var mox = 0
  var moy = 0

  var direction = 0

  var count = 0
  
  var tc = 0

  def render(canvas: Canvas, mx: Int, my: Int, ox: Int, oy: Int) {
    var mdx = x - mx
    var mdy = y - my

    if (mdx < 0 || mdx > 16 || mdy < 0 || mdy > 16)
      return

    val sx = mdx * 32 + ox + mox
    val sy = mdy * 32 + oy + moy

    canvas.drawRect(sx, sy, sx + 32, sy + 32)
    canvas.drawImage(tileSet(tc), sx, sy)
  }

  def tick() {
    // TODO: dirty hack test to simulate move right
    // TODO: needs logic for 8 directions, velocity
    if (x >= map.Width - 16)
      return

    if (mox >= 31) {

    } else {
      mox += 1
    }
    
    if (count == 32) {
      val xf = x+1
      map.removeShadeAround(xf, y)
    }

    if (count > 40) {
      mox = 0
      x += 1
      tc += 1
      if (tc>7) tc = 0
      
      count = 0
    }

    count += 1
  }
}
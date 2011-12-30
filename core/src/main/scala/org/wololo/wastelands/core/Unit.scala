package org.wololo.wastelands.core
import org.wololo.wastelands.vmlayer.Canvas

/**
 * TODO: probably needs refactoring for more intelligent rendering with other parts
 */
class Unit(map: Map, startX: Int, startY: Int, tileSet: Array[Object]) {

  var x = startX
  var y = startY

  map.removeShade(x, y)
  map.removeShade(x, y - 1)
  map.removeShade(x, y + 1)
  map.removeShade(x + 1, y)
  //map.removeShade(x + 1, y - 1)
  //map.removeShade(x + 1, y + 1)
  map.removeShade(x - 1, y)
  //map.removeShade(x - 1, y - 1)
  //map.removeShade(x - 1, y + 1)

  var mox = 0
  var moy = 0

  var direction = 0

  var count = 0

  def render(canvas: Canvas, mx: Int, my: Int, ox: Int, oy: Int) {
    var mdx = x - mx
    var mdy = y - my

    if (mdx < 0 || mdx > 16 || mdy < 0 || mdy > 16)
      return

    val sx = mdx * 32 + ox + mox
    val sy = mdy * 32 + oy + moy

    //canvas.drawRect(sx, sy, sx + 32, sy + 32)
    canvas.drawImage(tileSet(2), sx, sy)
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
      map.removeShade(xf, y)
      map.removeShade(xf, y - 1)
      map.removeShade(xf, y + 1)
      map.removeShade(xf + 1, y)
      //map.removeShade(x + 1, y - 1)
      //map.removeShade(x + 1, y + 1)
      map.removeShade(xf - 1, y)
      //map.removeShade(x - 1, y - 1)
      //map.removeShade(x - 1, y + 1)
    }

    if (count > 40) {
      mox = 0
      x += 1
      
      count = 0
    }

    count += 1
  }
}
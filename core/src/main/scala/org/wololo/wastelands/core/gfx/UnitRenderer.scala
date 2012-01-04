package org.wololo.wastelands.core.gfx
import org.wololo.wastelands.vmlayer._

class UnitRenderer[T: ClassManifest](screen: Screen[T]) {
  val tileSet: Array[T] = screen.tileSetFactory.createMapTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/unit.png"), BitmapTypes.Translucent).toArray

  def render(unit: org.wololo.wastelands.core.Unit) {
    val mdx = unit.x - screen.mx
    val mdy = unit.y - screen.my

    if (mdx < 0 || mdx > 16 || mdy < 0 || mdy > 16)
      return

    var xf = 0
    var yf = 0

    if (unit.moveStatus == unit.MoveStatusMoving) {
      // TODO: refactor with unit
      unit.direction match {
        case 0 => xf = 0; yf = -1
        case 1 => xf = 1; yf = -1
        case 2 => xf = 1; yf = 0
        case 3 => xf = 1; yf = 1
        case 4 => xf = 0; yf = 1
        case 5 => xf = -1; yf = 1
        case 6 => xf = -1; yf = 0
        case 7 => xf = -1; yf = -1
      }
    }

    val sx = mdx * 32 + screen.ox + (screen.TileSize * xf * unit.moveDistance).toInt
    val sy = mdy * 32 + screen.oy + (screen.TileSize * yf * unit.moveDistance).toInt

    //canvas.drawRect(sx, sy, sx + 32, sy + 32)
    screen.canvas.drawImage(tileSet(unit.direction), sx, sy)
  }
}
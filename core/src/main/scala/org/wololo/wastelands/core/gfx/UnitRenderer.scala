package org.wololo.wastelands.core.gfx
import org.wololo.wastelands.vmlayer._

class UnitRenderer[T: ClassManifest](screen: Screen[T]) {
  var tileSet = new Array[T](8)

  tileSet = screen.tileSetFactory.createMapTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/unit.png"), BitmapTypes.Translucent)

  def render(unit: org.wololo.wastelands.core.Unit) {
    var mdx = unit.x - screen.mx
    var mdy = unit.y - screen.my

    if (mdx < 0 || mdx > 16 || mdy < 0 || mdy > 16)
      return

    val sx = mdx * 32 + screen.ox + unit.mox
    val sy = mdy * 32 + screen.oy + unit.moy

    //canvas.drawRect(sx, sy, sx + 32, sy + 32)
    screen.canvas.drawImage(tileSet(unit.tc), sx, sy)
  }
}
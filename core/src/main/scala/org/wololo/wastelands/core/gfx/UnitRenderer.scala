package org.wololo.wastelands.core.gfx

import org.wololo.wastelands.core._
import org.wololo.wastelands.vmlayer._

class UnitRenderer[T: ClassManifest](screen: Screen[T]) {
  val tileSet1: Array[T] = screen.tileSetFactory.createUnitTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/unit.png"), BitmapTypes.Translucent).toArray
    
  val tileSet2: Array[T] = screen.tileSetFactory.createUnitTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/unit2.png"), BitmapTypes.Translucent).toArray

  def render(unit: org.wololo.wastelands.core.Unit) {
    val mapDeltaX = unit.x - screen.mx
    val mapDeltaY = unit.y - screen.my

    if (mapDeltaX < 0 || mapDeltaX > 16 || mapDeltaY < 0 || mapDeltaY > 16)
      return

    var moveOffsetX = 0
    var moveOffsetY = 0

    if (unit.moveStatus == unit.MoveStatusMoving) {
      var (dx, dy) = unit.mapDelta
      moveOffsetX = (screen.TileSize * dx * unit.moveDistance).toInt
      moveOffsetY = (screen.TileSize * dy * unit.moveDistance).toInt
    }

    val sx = mapDeltaX * 32 + screen.ox + moveOffsetX
    val sy = mapDeltaY * 32 + screen.oy + moveOffsetY

    val tileSet = unit match {
      case x:TestUnit1 => tileSet1
      case x:TestUnit2 => tileSet2
    }
    
    if (unit.selected) screen.canvas.drawRect(sx, sy, sx + 32, sy + 32)
    screen.canvas.drawImage(tileSet(unit.direction), sx, sy)
  }
}
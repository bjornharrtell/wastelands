package org.wololo.wastelands.core.gfx

import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core._

object UnitRenderer {
  def directionToTileIndex(direction: Direction): Int = {
    direction match {
      case Direction(0, 0) => 0
      case Direction(0, -1) => 0
      case Direction(1, -1) => 1
      case Direction(1, 0) => 2
      case Direction(1, 1) => 3
      case Direction(0, 1) => 4
      case Direction(-1, 1) => 5
      case Direction(-1, 0) => 6
      case Direction(-1, -1) => 7
    }
  }
}

class UnitRenderer[T: ClassManifest](screen: Screen[T]) {
  val tileSet1 = screen.tileSetFactory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/unit.png"), BitmapTypes.Translucent, 8, 1)

  tileSet1.map(tile => tileSet1.append(screen.graphicsContext.bitmapFactory.createShadow(tile)))

  val tileSet2 = screen.tileSetFactory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/unit2.png"), BitmapTypes.Translucent, 8, 1)

  tileSet2.map(tile => tileSet2.append(screen.graphicsContext.bitmapFactory.createShadow(tile)))

  def render(unit: org.wololo.wastelands.core.Unit) {
    var mapOffset = unit.position - screen.mapOffset
    
    if (!screen.MapBounds.contains(mapOffset)) return

    var moveOffsetX = 0
    var moveOffsetY = 0

    if (unit.moveStatus == unit.MoveStatusMoving) {
      moveOffsetX = (screen.TileSize * unit.direction.x * unit.moveDistance).toInt
      moveOffsetY = (screen.TileSize * unit.direction.y * unit.moveDistance).toInt
    }

    val sx = mapOffset.x * screen.TileSize + screen.mapPixelOffset.x + moveOffsetX
    val sy = mapOffset.y * screen.TileSize + screen.mapPixelOffset.y + moveOffsetY

    val tileSet = unit match {
      case x: TestUnit1 => tileSet1
      case x: TestUnit2 => tileSet2
    }

    val tileIndex = UnitRenderer.directionToTileIndex(unit.direction)

    screen.canvas.drawImage(tileSet(tileIndex + 8), sx - 3, sy + 3)
    if (unit.selected) screen.canvas.drawRect(sx, sy, sx + screen.TileSize, sy + screen.TileSize)
    screen.canvas.drawImage(tileSet(tileIndex), sx, sy)
  }
}
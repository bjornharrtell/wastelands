package org.wololo.wastelands.core.gfx

import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core._
import org.wololo.wastelands.core.unit._

class UnitRenderer[T: ClassManifest](val screen: Screen[T]) extends TileReader[T] {

  // TODO: create array of tilesets instead
  val tileSet1 = fileToTiles("tilesets/unit.png", BitmapTypes.Translucent, 8, 1, 16, screen.TileSize)
  tileSet1.map(tile => tileSet1.append(screen.graphicsContext.bitmapFactory.createShadow(tile)))

  val tileSet2 = fileToTiles("tilesets/unit2.png", BitmapTypes.Translucent, 8, 1, 16, screen.TileSize)
  tileSet2.map(tile => tileSet2.append(screen.graphicsContext.bitmapFactory.createShadow(tile)))

  val explosion = fileToTiles("tilesets/bigexplosion.png", BitmapTypes.Translucent, 28, 1, 32, 32 * screen.PixelSize)

  // current render offset in pixels
  private var offset: Coordinate = (0, 0)

  var count = 0

  def render(unit: Unit) {
    calcOffset(unit)

    // TODO: should probably define all unit subtypes as int constants to use here instead
    val tileSet = unit match {
      case x: TestUnit1 => tileSet1
      case x: TestUnit2 => tileSet2
    }

    if (unit.visible && !unit.dead) screen.canvas.drawImage(tileSet(unit.direction + 8), offset.x - 3, offset.y + 3)
    if (unit.selected && unit.visible && !unit.dead) screen.canvas.drawRect(offset.x, offset.y, offset.x + screen.TileSize, offset.y + screen.TileSize)
    if (unit.visible && !unit.dead) screen.canvas.drawImage(tileSet(unit.direction), offset.x, offset.y)

    if (unit.exploding) {
      val o = (32 * screen.PixelSize)/4
      screen.canvas.drawImage(explosion(count / 2), offset.x - o, offset.y - (7*screen.PixelSize) - o)
      count += 1
      if (count > 6 * 2) {
        unit.dead = true
      }
      if (count > 27 * 2) {
        unit.exploding = false
      }
    }
  }

  def calcOffset(unit: Unit) {
    var mapOffset = unit.position - screen.mapOffset

    // bail if unit not in current visible part of map
    if (!screen.MapBounds.contains(mapOffset)) {
      unit.visible = false
      return
    }

    offset.setTo(mapOffset.x * screen.TileSize, mapOffset.y * screen.TileSize)

    offset += screen.mapPixelOffset

    // if unit is moving, add move distance as pixels to offset
    if (unit.moveStatus == Movable.MoveStatusMoving) {
      offset.x += (screen.TileSize * unit.direction.x * unit.moveDistance).toInt
      offset.y += (screen.TileSize * unit.direction.y * unit.moveDistance).toInt
    }

    unit.visible = true
    unit.ScreenBounds.setTo(offset.x, offset.y, offset.x + screen.TileSize, offset.y + screen.TileSize)
  }
}
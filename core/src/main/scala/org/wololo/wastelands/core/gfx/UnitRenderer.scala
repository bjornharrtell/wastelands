package org.wololo.wastelands.core.gfx

import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core._
import org.wololo.wastelands.core.unit._

class UnitRenderer[T: ClassManifest](val screen: Screen[T]) extends TileReader[T] {
  
  // TODO: create array of tilesets instead
  val tileSet1 = fileToTiles("tilesets/unit.png", BitmapTypes.Translucent, 8, 1)
  tileSet1.map(tile => tileSet1.append(screen.graphicsContext.bitmapFactory.createShadow(tile)))

  val tileSet2 = fileToTiles("tilesets/unit2.png", BitmapTypes.Translucent, 8, 1)
  tileSet2.map(tile => tileSet2.append(screen.graphicsContext.bitmapFactory.createShadow(tile)))

  /**
   * Current render offset in pixels, instance is reused.
   */
  var offset: Coordinate = (0, 0)

  def render(unit: Unit) {
    var mapOffset = unit.position - screen.mapOffset

    // bail if unit not in current visible part of map
    if (!screen.MapBounds.contains(mapOffset)) return

    offset.setTo(mapOffset.x * screen.TileSize, mapOffset.y * screen.TileSize)

    offset += screen.mapPixelOffset

    var tileIndex = 0

    unit match {
      case movableUnit: Movable =>
        // if unit is moving, add move distance as pixels to offset
        if (movableUnit.moveStatus == Movable.MoveStatusMoving) {
          offset.x += (screen.TileSize * movableUnit.direction.x * movableUnit.moveDistance).toInt
          offset.y += (screen.TileSize * movableUnit.direction.y * movableUnit.moveDistance).toInt
        }
        tileIndex = movableUnit.direction
    }
    
    // TODO: should probably define all unit subtypes as int constants to use here instead
    val tileSet = unit match {
      case x: TestUnit1 => tileSet1
      case x: TestUnit2 => tileSet2
    }

    screen.canvas.drawImage(tileSet(tileIndex + 8), offset.x - 3, offset.y + 3)
    if (unit.selected) screen.canvas.drawRect(offset.x, offset.y, offset.x + screen.TileSize, offset.y + screen.TileSize)
    screen.canvas.drawImage(tileSet(tileIndex), offset.x, offset.y)
  }
}
package org.wololo.wastelands.core.gfx

import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core._
import org.wololo.wastelands.core.unit._
import scala.collection.mutable.ArrayBuffer
import java.io.File

class UnitRenderer(val screen: Screen) extends TileReader {

  // TODO: create array of tilesets instead
  val tileSet1 = fileToTiles(new File("tilesets/unit.png"), BitmapTypes.Translucent, 8, 1, 16, screen.TileSize)
  tileSet1.map(tile => tileSet1.append(screen.bitmapFactory.createShadow(tile)))

  val tileSet2 = fileToTiles(new File("tilesets/unit2.png"), BitmapTypes.Translucent, 8, 1, 16, screen.TileSize)
  tileSet2.map(tile => tileSet2.append(screen.bitmapFactory.createShadow(tile)))
  
  val selection = fileToTiles(new File("tilesets/other.png"), BitmapTypes.Translucent, 1, 1, 16, screen.TileSize)(0)

  // current render offset in pixels
  private var offset: Coordinate = (0, 0)
  
  private var explosions = new ArrayBuffer[UnitExplosionRenderer]

  def render(unit: Unit) {
    calcOffset(unit)

    // TODO: should probably define all unit subtypes as int constants to use here instead
    val tileSet = unit match {
      case x: TestUnit1 => tileSet1
      case x: TestUnit2 => tileSet2
    }

    if (unit.isOnScreen && unit.visible) {
      screen.canvas.drawImage(tileSet(unit.direction + 8), offset.x - 3, offset.y + 3)
      screen.canvas.drawImage(tileSet(unit.direction), offset.x, offset.y)
      if (unit.selected) screen.canvas.drawImage(selection, offset.x, offset.y)
    }

    if (unit.explode) {
      unit.explode = false
      unit.exploding = true
      explosions += new UnitExplosionRenderer(screen, unit, offset.clone)
    }
    
    explosions.foreach(explosion => {
      if (!explosion.render) {
        explosions -= explosion
        unit.exploding = false
      }
    })
  }

  def calcOffset(unit: Unit) {
    var mapOffset = unit.position - screen.mapOffset

    // bail if unit not in current visible part of map
    if (!screen.MapBounds.contains(mapOffset)) {
      unit.isOnScreen = false
      return
    }

    offset.setTo(mapOffset.x * screen.TileSize, mapOffset.y * screen.TileSize)

    offset += screen.mapPixelOffset

    // if unit is moving, add move distance as pixels to offset
    if (unit.moveStatus == Movable.MoveStatusMoving) {
      offset.x += (screen.TileSize * unit.direction.x * unit.moveDistance).toInt
      offset.y += (screen.TileSize * unit.direction.y * unit.moveDistance).toInt
    }

    unit.isOnScreen = true
    unit.ScreenBounds.setTo(offset.x, offset.y, offset.x + screen.TileSize, offset.y + screen.TileSize)
  }
}
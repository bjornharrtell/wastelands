package org.wololo.wastelands.core.gfx

import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core._
import org.wololo.wastelands.core.unit._
import scala.collection.mutable.ArrayBuffer
import java.io.File

class UnitRenderer(val screen: Screen) extends TileReader {

  val tileSet = fileToTiles(new File("tilesets/unit.png"), BitmapTypes.Translucent, 8, 3, 16, screen.TileSize)

  val tileSetTestUnit1 = tileSet.slice(0, 8)
  tileSetTestUnit1.map(tile => tileSetTestUnit1.append(screen.bitmapFactory.createShadow(tile)))

  val tileSetTestUnit2 = tileSet.slice(8, 16)
  tileSetTestUnit2.map(tile => tileSetTestUnit2.append(screen.bitmapFactory.createShadow(tile)))

  val tileSetHarvester = tileSet.slice(16, 24)
  tileSetHarvester.map(tile => tileSetHarvester.append(screen.bitmapFactory.createShadow(tile)))

  val selection = fileToTiles(new File("tilesets/other.png"), BitmapTypes.Translucent, 1, 1, 16, screen.TileSize)(0)

  // current render offset in pixels
  private var x = 0
  private var y = 0

  private var explosions = new ArrayBuffer[UnitExplosionRenderer]

  def render(unit: UnitClientState) {
    calcOffset(unit)

    // TODO: should probably define all unit subtypes as int constants to use here instead
    val tileSet = unit.unitType match {
      case UnitTypes.TestUnit1 => tileSetTestUnit1
      case UnitTypes.TestUnit2 => tileSetTestUnit2
      case UnitTypes.Harvester => tileSetHarvester
    }

    if (unit.isOnScreen && unit.alive) {
      // TODO: render shadows in a step before *all* units, to make them always draw under unit graphics...
      screen.canvas.drawImage(tileSet(unit.direction.toTileIndex + 8), x - 3, y + 3)
      screen.canvas.drawImage(tileSet(unit.direction.toTileIndex), x, y)
      if (unit.selected) screen.canvas.drawImage(selection, x, y)
    }

    if (unit.explode) {
      unit.explode = false
      unit.exploding = true
      explosions += new UnitExplosionRenderer(screen, unit, (x,y))
    }

    explosions.foreach(explosion => {
      if (!explosion.render) {
        explosions -= explosion
        unit.exploding = false
      }
    })
  }

  def calcOffset(unit: UnitClientState) {
    var mx = unit.position.x - screen.mapOffset.x
    var my = unit.position.y - screen.mapOffset.y

    // bail if unit not in current visible part of map
    if (!screen.MapBounds.contains(mx, my)) {
      unit.isOnScreen = false
      return
    }
    
    // need to "move" unit back to previous location if moving since the unit position is changed before animating the move 
    // TODO: should be able to refactor this to not check for move action twice...
    if (unit.action.isInstanceOf[MoveTileStep]) {
      mx -= unit.direction.x
      my -= unit.direction.y
    }

    x = mx * screen.TileSize
    y = my * screen.TileSize

    // if unit is moving, add move distance as pixels to offset
    if (unit.action.isInstanceOf[MoveTileStep]) {
      // TODO: action length from unittype
      var moveDistance = (unit.gameState.ticks - unit.action.start).toDouble / 50
      //println(unit.gameState.ticks +" " + unit.action.start +" " + moveDistance)
      x += (screen.TileSize * unit.direction.x * moveDistance).toInt
      y += (screen.TileSize * unit.direction.y * moveDistance).toInt
    }
    
    x += screen.mapPixelOffset.x
    y += screen.mapPixelOffset.y

    unit.isOnScreen = true
    unit.screenBounds.setTo(x, y, x + screen.TileSize, y + screen.TileSize)
  }
}
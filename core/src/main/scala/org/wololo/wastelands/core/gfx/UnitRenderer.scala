package org.wololo.wastelands.core.gfx

import java.io.File

import scala.collection.mutable.ArrayBuffer

import org.wololo.wastelands.core.client.ClientUnit
import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.mutable
import org.wololo.wastelands.vmlayer.BitmapTypes

class UnitRenderer(val screen: Screen) extends TileReader {

  val tileSet = fileToTiles(new File("tilesets/unit.png"), BitmapTypes.Translucent, 8, 3, 16, screen.TileSize)

  val tileSetTestUnit1 = tileSet.slice(0, 8)
  tileSetTestUnit1.map(tile => tileSetTestUnit1.append(screen.bitmapFactory.createShadow(tile)))

  val tileSetTestUnit2 = tileSet.slice(8, 16)
  tileSetTestUnit2.map(tile => tileSetTestUnit2.append(screen.bitmapFactory.createShadow(tile)))

  val tileSetHarvester = tileSet.slice(16, 24)
  tileSetHarvester.map(tile => tileSetHarvester.append(screen.bitmapFactory.createShadow(tile)))

  val selection = fileToTiles(new File("tilesets/other.png"), BitmapTypes.Translucent, 1, 1, 16, screen.TileSize)(0)

  val offset = new mutable.Coordinate()

  private var explosions = new ArrayBuffer[UnitExplosionRenderer]

  def render(unit: ClientUnit) {
    screen.calcOffset(unit, offset)

    // TODO: should probably define all unit subtypes as int constants to use here instead
    val tileSet = unit.unitType match {
      case Unit.TestUnit1 => tileSetTestUnit1
      case Unit.TestUnit2 => tileSetTestUnit2
      case Unit.Harvester => tileSetHarvester
    }

    if (unit.isOnScreen && unit.alive) {
      // TODO: render shadows in a step before *all* units, to make them always draw under unit graphics...
      screen.canvas.drawImage(tileSet(unit.direction.toTileIndex + 8), offset.x - 3, offset.y + 3)
      screen.canvas.drawImage(tileSet(unit.direction.toTileIndex), offset.x, offset.y)
      if (unit.selected) screen.canvas.drawImage(selection, offset.x, offset.y)
    }

    if (unit.explode) {
      unit.explode = false
      unit.exploding = true
      explosions += new UnitExplosionRenderer(screen, unit)
    }

    val toBeRemoved = ArrayBuffer[UnitExplosionRenderer]()
    explosions.foreach(explosion => {
      if (!explosion.render) {
        toBeRemoved += explosion
        unit.exploding = false
      }
    })
    explosions --= toBeRemoved
  }

}
package org.wololo.wastelands.core
import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core.unit._
import org.wololo.wastelands.core.gfx._
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.event.Event
import org.wololo.wastelands.core.event.TouchEvent

class GameMapEditor(vmContext: VMContext) extends Game(vmContext) {
  
  override def init() {
    map.tiles.foreach(tile => { tile.shade = false })
  }
  
  /**
   * Perform action on a chosen map tile
   */
  override def mapTileAction(coordinate: Coordinate) {
    map.tiles(coordinate).baseType = TileTypes.Dunes
    map.makeBorderAround(coordinate)
  }
}

package org.wololo.wastelands.core
import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core.unit._
import org.wololo.wastelands.core.gfx._
import scala.collection.mutable.ArrayBuffer
import org.wololo.wastelands.core.event.Event
import org.wololo.wastelands.core.event.TouchEvent

class GameMapEditor(vmContext: VMContext) extends Game(vmContext) {
  
  var tileType: Option[Int] = None
  
  override def init() {
    map.tiles.foreach(tile => { tile.shade = false })
  }
  
  /**
   * Perform action on a chosen map tile
   */
  override def mapTileAction(coordinate: Coordinate) { }
  
  override def touchMove(coordinate: Coordinate) {
    tileType match {
      case x: Some[Int] => {
        val mx = screen.calculateTileIndex(screen.screenOffset.x + coordinate.x)
        val my = screen.calculateTileIndex(screen.screenOffset.y + coordinate.y)
        map.tiles(mx, my).baseType = x.get
        map.tiles(mx, my).subType = 0
        map.makeBorderAround((mx, my))
      }
      case None => super.touchMove(coordinate)
    }
  }
  
  override def keyDown(keyCode: Int) {
    keyCode match {
      case KeyCode.KEY_1 => tileType = None
      case KeyCode.KEY_2 => tileType = Option(TileTypes.Base)
      case KeyCode.KEY_3 => tileType = Option(TileTypes.Dunes)
      case KeyCode.KEY_4 => tileType = Option(TileTypes.Rock)
      case KeyCode.KEY_5 => tileType = Option(TileTypes.Spice)
      case KeyCode.KEY_9 => map.save()
      case KeyCode.KEY_0 => map.load()
      case _ =>
    }
  }
}

package org.wololo.wastelands.core.gfx
import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core._

class TileRenderer[T : ClassManifest](val screen: Screen[T]) extends TileReader[T] {
  val shadeSet = fileToTiles("tilesets/shade.png", BitmapTypes.Bitmask, 18, 5)
  val tileSets = Array.ofDim[T](4, 5 * 18)
  tileSets(TileTypes.Base)(0) = fileToTiles("tilesets/desert.png", BitmapTypes.Opague, 1, 1)(0)
  tileSets(TileTypes.Dunes) = fileToTiles("tilesets/dunes.png", BitmapTypes.Opague, 18, 5).toArray
  tileSets(TileTypes.Rock) = fileToTiles("tilesets/rock.png", BitmapTypes.Opague, 18, 5).toArray
  tileSets(TileTypes.Spice) = fileToTiles("tilesets/spice.png", BitmapTypes.Opague, 18, 5).toArray

  /**
   * Main tile render loop.
   * 
   * NOTE: plus 1 tile size border buffer for scrolling
   */
  def render() {
    // NOTE: while loop used for perf
    var y = -1
    var x = -1
    while (y <= screen.TilesHeight) {
      x = -1
      while (x <= screen.TilesWidth) {
        renderTile(x, y)
        x += 1
      }
      y += 1
    }
  }
  
  /**
   * @param x screen tile coordinate
   * @param y screen tile coordinate
   */
  private def renderTile(x: Int, y: Int) {
    // screen destination coord
    val sx = x * screen.TileSize + screen.mapPixelOffset.x
    val sy = y * screen.TileSize + screen.mapPixelOffset.y

    // map tile coord
    val tx = screen.mapOffset.x + x
    val ty = screen.mapOffset.y + y

    // bail if out of map bounds
    if (tx < 0 || tx > screen.map.Width || ty < 0 || ty > screen.map.Height)
      return

    val tile = screen.map.tiles(tx,ty)

    screen.canvas.drawImage(tileSets(tile.baseType)(tile.subType), sx, sy)

    if (tile.shade) {
      if (tile.shadeSubType == 0) {
        screen.canvas.clearRect(sx, sy, sx + screen.TileSize, sy + screen.TileSize)
      } else {
        screen.canvas.drawImage(shadeSet(tile.shadeSubType), sx, sy)
      }
    }
  }
}
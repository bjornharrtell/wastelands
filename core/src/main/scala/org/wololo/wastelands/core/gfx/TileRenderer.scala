package org.wololo.wastelands.core.gfx
import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core._
import java.io.File
import org.wololo.wastelands.core.map.Tile

class TileRenderer(val screen: Screen) extends TileReader {
  val shadeSet = fileToTiles(new File("tilesets/shade.png"), BitmapTypes.Bitmask, 18, 5, 16, screen.TileSize)
  val tileSets = Array.ofDim[Int](4, 5 * 18)
  tileSets(Tile.Base)(0) = fileToTiles(new File("tilesets/desert.png"), BitmapTypes.Opague, 1, 1, 16, screen.TileSize)(0)
  tileSets(Tile.Dunes) = fileToTiles(new File("tilesets/dunes.png"), BitmapTypes.Opague, 18, 5, 16, screen.TileSize).toArray
  tileSets(Tile.Rock) = fileToTiles(new File("tilesets/rock.png"), BitmapTypes.Opague, 18, 5, 16, screen.TileSize).toArray
  tileSets(Tile.Spice) = fileToTiles(new File("tilesets/spice.png"), BitmapTypes.Opague, 18, 5, 16, screen.TileSize).toArray

  /**
   * Main tile render loop.
   *
   * NOTE: plus 1 tile size border buffer for scrolling
   */
  def render(shade: Boolean) {
    // NOTE: while loop used for perf
    var y = -1
    var x = -1
    while (y <= screen.TilesHeight) {
      x = -1
      while (x <= screen.TilesWidth) {
        renderTile(x, y, shade)
        x += 1
      }
      y += 1
    }
  }

  /**
   * @param x screen tile coordinate
   * @param y screen tile coordinate
   */
  private def renderTile(x: Int, y: Int, shade: Boolean) {
    // screen destination coord
    val sx = x * screen.TileSize + screen.mapPixelOffset.x
    val sy = y * screen.TileSize + screen.mapPixelOffset.y

    // map tile coord
    val tx = screen.mapOffset.x + x
    val ty = screen.mapOffset.y + y

    // bail if out of map bounds
    if (tx < 0 || tx > screen.map.Width || ty < 0 || ty > screen.map.Height)
      return

    val tile = screen.map.tiles(tx, ty)

    if (!shade && (!tile.shade || tile.shadeSubType > 0)) {
      screen.canvas.drawImage(tileSets(tile.baseType)(tile.subType), sx, sy)
    } else if (tile.shade) {
      if (tile.shadeSubType == 0) {
        screen.canvas.clearRect(sx, sy, sx + screen.TileSize, sy + screen.TileSize)
      } else {
        screen.canvas.drawImage(shadeSet(tile.shadeSubType), sx, sy)
      }
    }

  }
}
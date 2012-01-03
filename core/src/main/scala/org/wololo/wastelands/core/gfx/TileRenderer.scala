package org.wololo.wastelands.core.gfx
import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core._

class TileRenderer[T : ClassManifest](screen: Screen[T]) {
  val tileSets = Array.ofDim[T](4, 5 * 18)
  var shadeSet = new Array[T](5 * 18)

  tileSets(TileTypes.Base)(0) = screen.tileSetFactory.createTileFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/desert.png"))
  tileSets(TileTypes.Dunes) = screen.tileSetFactory.createMapTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/dunes.png"), BitmapTypes.Opague)
  tileSets(TileTypes.Rock) = screen.tileSetFactory.createMapTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/rock.png"), BitmapTypes.Opague)
  tileSets(TileTypes.Spice) = screen.tileSetFactory.createMapTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/spice.png"), BitmapTypes.Opague)

  shadeSet = screen.tileSetFactory.createMapTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/shade.png"), BitmapTypes.Bitmask)
  
  def render() {
    // NOTE: while loop used for perf
    var y = -1
    var x = -1
    while (y < 17) {
      x = -1
      while (x < 17) {
        renderTile(x, y)
        x += 1
      }

      y += 1
    }
  }
  
  /**
   * @param mx map tile coordinate
   * @param my map tile coordinate
   * @param x screen tile coordinate
   * @param y screen tile coordinate
   */
  private def renderTile(x: Int, y: Int) {
    // screen destination coord
    val sx = x * 32 + screen.ox
    val sy = y * 32 + screen.oy

    // map tile coord
    val tx = screen.mx + x
    val ty = screen.my + y

    // bail if out of map bounds
    if (tx < 0 || tx > screen.map.Width || ty < 0 || ty > screen.map.Height)
      return

    val tile = screen.map.tiles(tx)(ty)

    screen.canvas.drawImage(tileSets(tile.baseType)(tile.subType), sx, sy)

    if (tile.shade) {
      if (tile.shadeSubType == 0) {
        screen.canvas.clearRect(sx, sy, sx + 32, sy + 32)
      } else {
        screen.canvas.drawImage(shadeSet(tile.shadeSubType), sx, sy)
      }
    }
  }
}
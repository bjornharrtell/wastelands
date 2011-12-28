package org.wololo.dune3.core

import org.wololo.dune3.vmlayer.Canvas
import org.wololo.dune3.vmlayer.TileSetFactory

/**
 * Contains the contents of the game screen with rendering logic.
 */
class Screen(tileSetFactory: TileSetFactory, map: Map) {
  val tileSets = Array.ofDim[Object](4, 5 * 18)
  var shadeSet = new Array[Object](5 * 18);

  val TileSize = 32;
  val MapScreenWidth = map.Width * TileSize
  val MapScreenHeight = map.Height * TileSize

  val unit = new Unit(map, 7,7)
  
  var sx = 0
  var sy = 0

  tileSets(TileTypes.Base)(0) = tileSetFactory.createTileFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/desert.png"))
  tileSets(TileTypes.Dunes) = tileSetFactory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/dunes.png"), false)
  tileSets(TileTypes.Rock) = tileSetFactory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/rock.png"), false)
  tileSets(TileTypes.Spice) = tileSetFactory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/spice.png"), false)

  shadeSet = tileSetFactory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/shade.png"), true)

  def move(dx: Int, dy: Int) {
    sx += dx
    sy += dy

    sx = if (sx < 0) 0 else sx
    sx = if (sx > MapScreenWidth - (32 * TileSize)) MapScreenWidth - (32 * TileSize) else sx
    sy = if (sy < 0) 0 else sy
    sy = if (sy > MapScreenHeight - (32 * TileSize)) MapScreenHeight - (32 * TileSize) else sy
  }

  /**
   * Render the map on screen 16x16 tiles plus 1 tile size border buffer for
   * scrolling.
   */
  def render(canvas: Canvas, w: Int, h: Int) {
    // screen coord to map coord conversion
    val mxd: Double = map.Width * (sx.toDouble / MapScreenWidth)
    val myd: Double = map.Height * (sy.toDouble / MapScreenHeight)

    val mx: Int = mxd.toInt
    val my: Int = myd.toInt
    
    // tile offset calc (for the scrolling buffer)
    val ox: Int = -(mxd % 1 * TileSize).toInt
    val oy: Int = -(myd % 1 * TileSize).toInt

    // NOTE: while loop used for perf
    var y = -1
    var x = -1
    while (y < 17) {
      x = -1
      while (x < 17) {
        renderTile(canvas, x, y, mx, my, ox, oy)
        x += 1
      }

      y += 1
    }
  }

  /**
   * Render a tile with scrolling offset
   */
  def renderTile(canvas: Canvas, x: Int, y: Int, mx: Int, my: Int, ox: Int, oy: Int) {
    // screen destination coord
    val sx = x * 32 + ox
    val sy = y * 32 + oy

    // map tile coord
    val tx = mx + x;
    val ty = my + y;

    // bail if out of map bounds
    if (tx < 0 || tx > map.Width || ty < 0 || ty > map.Height)
      return

    val tile = map.tiles(tx)(ty)

    canvas.drawImage(tileSets(tile.baseType)(tile.subType), sx, sy)

    if (tile.shade) {
      if (tile.shadeSubType == 0) {
        canvas.clearRect(sx, sy, sx+32, sy+32)
      } else {
        canvas.drawImage(shadeSet(tile.shadeSubType), sx, sy)
      }
    }
  }
}

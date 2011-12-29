package org.wololo.wastelands.core

import org.wololo.wastelands.vmlayer.BitmapFactory
import org.wololo.wastelands.vmlayer.BitmapTypes
import org.wololo.wastelands.vmlayer.Canvas
import org.wololo.wastelands.vmlayer.CanvasFactory

/**
 * Contains the contents of the game screen with rendering logic.
 */
class Screen(map: Map, bitmapFactory: BitmapFactory, canvasFactory: CanvasFactory) {
  
  val TileSize = 32
  
  val TilesWidth = 16
  val TilesHeight = 16
  
  val Width = TileSize * TilesWidth
  val Height = TileSize * TilesHeight
    
  val MapScreenWidth = map.Width * TileSize
  val MapScreenHeight = map.Height * TileSize
  
  val bitmap = bitmapFactory.create(Width, Height, BitmapTypes.Opague)
  val canvas = canvasFactory.create(bitmap)
  
  val unit = new Unit(map, 7,7)
  
  var sx = 0
  var sy = 0
  
  val tileSetFactory = new TileSetFactory(bitmapFactory, canvasFactory)
  
  val tileSets = Array.ofDim[Object](4, 5 * 18)
  var shadeSet = new Array[Object](5 * 18)

  tileSets(TileTypes.Base)(0) = tileSetFactory.createTileFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/desert.png"))
  tileSets(TileTypes.Dunes) = tileSetFactory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/dunes.png"), BitmapTypes.Opague)
  tileSets(TileTypes.Rock) = tileSetFactory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/rock.png"), BitmapTypes.Opague)
  tileSets(TileTypes.Spice) = tileSetFactory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/spice.png"), BitmapTypes.Opague)

  shadeSet = tileSetFactory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/shade.png"), BitmapTypes.Bitmask)

  def move(dx: Int, dy: Int) {
    sx += dx
    sy += dy

    // TODO: make sure bounds calc is correct... this is a guess (16*TileSize crashed)
    var maxx = MapScreenWidth - (TilesWidth-1 * TileSize)
    var maxy = MapScreenHeight - (TilesHeight-1 * TileSize)
    sx = if (sx < 0) 0 else sx
    sx = if (sx > maxx) maxx else sx
    sy = if (sy < 0) 0 else sy
    sy = if (sy > maxy) maxy else sy
  }

  /**
   * Render the map on screen 16x16 tiles plus 1 tile size border buffer for
   * scrolling.
   */
  def render() {
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
        renderTile(x, y, mx, my, ox, oy)
        x += 1
      }

      y += 1
    }
    
    unit.render(canvas, mx, my, ox, oy)
  }
  
  def renderUnit() {
    
  }

  /**
   * Render a tile with scrolling offset
   */
  def renderTile(x: Int, y: Int, mx: Int, my: Int, ox: Int, oy: Int) {
    // screen destination coord
    val sx = x * 32 + ox
    val sy = y * 32 + oy

    // map tile coord
    val tx = mx + x
    val ty = my + y

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

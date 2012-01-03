package org.wololo.wastelands.core

import org.wololo.wastelands.vmlayer._


/**
 * Contains the contents of the game screen with rendering logic.
 */
class Screen[T : ClassManifest](game: Game[T], graphicsContext: GraphicsContext[T]) {
  
  val TileSize = 32
  
  val TilesWidth = 16
  val TilesHeight = 16
  
  val Width = TileSize * TilesWidth
  val Height = TileSize * TilesHeight
  
  val map = game.map
    
  val MapScreenWidth = map.Width * TileSize
  val MapScreenHeight = map.Height * TileSize
  
  val bitmap = graphicsContext.bitmapFactory.create(Width, Height, BitmapTypes.Opague)
  val canvas = graphicsContext.canvasFactory.create(bitmap)
  
  // screen pixel scroll offset
  var sx = 0
  var sy = 0
  
  // map tile pixel scroll offset
  var ox = 0
  var oy = 0
  
  val tileSetFactory = game.tileSetFactory
  
  val tileSets = Array.ofDim[T](4, 5 * 18)
  var shadeSet = new Array[T](5 * 18)

  tileSets(TileTypes.Base)(0) = tileSetFactory.createTileFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/desert.png"))
  tileSets(TileTypes.Dunes) = tileSetFactory.createMapTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/dunes.png"), BitmapTypes.Opague)
  tileSets(TileTypes.Rock) = tileSetFactory.createMapTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/rock.png"), BitmapTypes.Opague)
  tileSets(TileTypes.Spice) = tileSetFactory.createMapTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/spice.png"), BitmapTypes.Opague)

  shadeSet = tileSetFactory.createMapTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/shade.png"), BitmapTypes.Bitmask)

  def scroll(dx: Int, dy: Int) {
    sx += dx
    sy += dy

    // TODO: make sure bounds calc is correct... this is a guess (16*TileSize crashed)
    val maxx = MapScreenWidth - (TilesWidth-1 * TileSize)
    val maxy = MapScreenHeight - (TilesHeight-1 * TileSize)
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
    ox = -(mxd % 1 * TileSize).toInt
    oy = -(myd % 1 * TileSize).toInt

    TileRenderer.renderTiles(this, mx, my)
    
    game.unit.render(canvas, mx, my, ox, oy)
  }
  
  def renderUnit() {
    
  }

  /**
   * Render a tile with scrolling offset
   */
  def renderTile(x: Int, y: Int, mx: Int, my: Int, ox: Int, oy: Int) {
    
  }
}

package org.wololo.wastelands.core.gfx

import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core._

/**
 * Contains the state of the game screen.
 */
class Screen[T: ClassManifest](game: Game[T], graphicsContext: GraphicsContext[T]) {
  
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
  
  // TODO: Screen/Game probably doesn't need TileSetFactory, try to refactor it into TileRenderer internals
  val tileSetFactory = game.tileSetFactory
  val tileRenderer = new TileRenderer(this)
  val unitRenderer = new UnitRenderer(this)
  
  // screen pixel scroll offset
  var sx = 0
  var sy = 0
  
  // map tile scroll offset
  var mx = 0
  var my = 0
  
  // map tile pixel scroll offset
  var ox = 0
  var oy = 0

  def scroll(dx: Int, dy: Int) {
    sx += dx
    sy += dy

    // TODO: make sure bounds calc is correct... this is a guess (16*TileSize crashed)
    val maxx = MapScreenWidth - ((TilesWidth+1) * TileSize)
    val maxy = MapScreenHeight - ((TilesHeight+1) * TileSize)
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
    import TileRenderer._

    //calculate the tile index for x and y axis
    mx = calculateTileIndex(sx,  Tile.Size)
    my = calculateTileIndex(sy,  Tile.Size)

    //calculate the tile pixel offset for x and y axis
    ox = calculateTilePixelOffset(sx, mx, Tile.Size)
    oy = calculateTilePixelOffset(sy, my, Tile.Size)

    tileRenderer.render()
    game.units.foreach(unitRenderer.render(_))
  }
}

package org.wololo.wastelands.core.gfx

import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core._

/**
 * Contains the state of the game screen.
 */
class Screen[T: ClassManifest](game: Game[T]) {

  val graphicsContext = game.graphicsContext

  // count iterations with first iteration 0
  var tileSizeCalcIterations = -1
  // recursive calc from 2^f until less than 21 tiles fits in screen width
  def tileSizeCalc(f: Int) : Int = {
    tileSizeCalcIterations += 1
    val tileSize = math.pow(2, f).toInt
    if (graphicsContext.screenWidth / tileSize < 21) tileSize else tileSizeCalc(f+1)
  }
  
  // start calc at tilesize 2^4
  val TileSize = tileSizeCalc(4)
  val TilesWidth = (graphicsContext.screenWidth / TileSize).toInt
  val TilesHeight = (graphicsContext.screenHeight / TileSize).toInt
  val PixelSize = math.pow(2, tileSizeCalcIterations).toInt

  val Width = TileSize * TilesWidth
  val Height = TileSize * TilesHeight

  val map = game.map

  val MapScreenWidth = map.Width * TileSize
  val MapScreenHeight = map.Height * TileSize

  val bitmap = graphicsContext.bitmapFactory.create(Width, Height, BitmapTypes.Opague)
  val canvas = graphicsContext.canvasFactory.create(bitmap)

  val tileSetFactory = new TileSetFactory[T](graphicsContext, TileSize)
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
    val maxx = MapScreenWidth - ((TilesWidth + 1) * TileSize)
    val maxy = MapScreenHeight - ((TilesHeight + 1) * TileSize)
    sx = if (sx < 0) 0 else sx
    sx = if (sx > maxx) maxx else sx
    sy = if (sy < 0) 0 else sy
    sy = if (sy > maxy) maxy else sy
  }

  /**
   * Render the map on screen
   */
  def render() {
    //calculate the tile index for x and y axis
    mx = calculateTileIndex(sx)
    my = calculateTileIndex(sy)

    //calculate the tile pixel offset for x and y axis
    ox = calculateTilePixelOffset(sx, mx)
    oy = calculateTilePixelOffset(sy, my)

    tileRenderer.render()
    game.units.foreach(unitRenderer.render(_))
  }

  /**
   * Calculates the index of a tile on the map
   *
   * @param screenPixelCoord partial coordinate for the first displayed pixel on the screen, if going from top-left
   * to bottom-right
   * @param tilePixelSize the size of a tile on the map by pixels
   * @return index for the first displayed tile on the axis in question
   */
  def calculateTileIndex(screenPixelCoord: Int): Int = {
    screenPixelCoord / TileSize
  }

  /**
   * Calculates the offset of a tile in pixels, which occurs because of smooth scrolling.
   *
   * @param screenPixelCoord partial coordinate for the first displayed pixel on the screen, if going from top-left
   * to bottom-right
   * @param tileIndex index for the first displayed tile on the axis in question
   * @param tilePixelSize the size of a tile on the map by pixels
   *
   * @return the offset in pixels on a tile for the axis in question
   */
  def calculateTilePixelOffset(screenPixelCoord: Int, tileIndex: Int) = {
    if (screenPixelCoord >= 0) {
      -(screenPixelCoord - (tileIndex * TileSize))
    } else {
      (tileIndex * TileSize) - screenPixelCoord
    }
  }
}

package org.wololo.wastelands.core.gfx

import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core._

/**
 * Contains the state of the game screen.
 */
class Screen(client: Client) {

  val vmContext = client.vmContext
  val bitmapFactory = vmContext.bitmapFactory
  val canvasFactory = vmContext.canvasFactory
  
  val Width = vmContext.screenWidth
  val Height = vmContext.screenHeight
  val Bounds: Rect = (0, 0, Width, Height)

  System.out.println("Width:" + Width + " Height:" + Height)

  var tileSizeCalcIterations = 0
  // recursive calc from 2^f until less than 21 tiles fits in largest screen dim
  def tileSizeCalc(f: Int): Int = {
    tileSizeCalcIterations += 1
    val tileSize = math.pow(2, f).toInt
    val screenDim = if (Height > Width) Height else Width
    if (screenDim / tileSize < 21) tileSize else tileSizeCalc(f + 1)
  }

  // start calc at tilesize 2^5
  val TileSize = tileSizeCalc(5)
  val TilesWidth = (Width / TileSize).toInt + 1
  val TilesHeight = (Height / TileSize).toInt + 1
  val PixelSize = math.pow(2, tileSizeCalcIterations).toInt

  val MapBounds: Rect = (-1, -1, TilesWidth, TilesHeight)

  System.out.println("TileSize:" + TileSize + " PixelSize:" + PixelSize)

  val map = client.map

  val MapScreenWidth = map.Width * TileSize
  val MapScreenHeight = map.Height * TileSize
  
  val bitmap = bitmapFactory.create(Width, Height, BitmapTypes.Opague)
  val canvas = canvasFactory.create(bitmap)

  val tileSetFactory = new TileSetFactory(vmContext)
  val tileRenderer = new TileRenderer(this)
  val unitRenderer = new UnitRenderer(this)
  
 
  private var projectileRenderer = new ProjectileRenderer(this)


  // screen pixel scroll offset
  var screenOffset: Coordinate = (0, 0)

  // map tile scroll offset
  var mapOffset: Coordinate = (0, 0)

  // map tile pixel scroll offset
  var mapPixelOffset: Coordinate = (0, 0)

  def scroll(delta: Coordinate) {
    screenOffset += delta

    val maxx = MapScreenWidth - ((TilesWidth + 1) * TileSize)
    val maxy = MapScreenHeight - ((TilesHeight + 1) * TileSize)
    screenOffset.x = if (screenOffset.x < 0) 0 else screenOffset.x
    screenOffset.x = if (screenOffset.x > maxx) maxx else screenOffset.x
    screenOffset.y = if (screenOffset.y < 0) 0 else screenOffset.y
    screenOffset.y = if (screenOffset.y > maxy) maxy else screenOffset.y
  }

  /**
   * Render the map on screen
   */
  def render() {
    //calculate the tile index for x and y axis
    mapOffset.x = calculateTileIndex(screenOffset.x)
    mapOffset.y = calculateTileIndex(screenOffset.y)

    //calculate the tile pixel offset for x and y axis
    mapPixelOffset.x = calculateTilePixelOffset(screenOffset.x, mapOffset.x)
    mapPixelOffset.y = calculateTilePixelOffset(screenOffset.y, mapOffset.y)

    tileRenderer.render(false)
    client.unitStates.values.foreach(unitRenderer.render(_))
    client.projectiles.foreach(projectileRenderer.render(_))
    tileRenderer.render(true)
  }

  /**
   * Calculates the index of a tile on the map
   *
   * @param screenPixelCoord partial coordinate for the first displayed pixel on the screen, if going from top-left
   * to bottom-right
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

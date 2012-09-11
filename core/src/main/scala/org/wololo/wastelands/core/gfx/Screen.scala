package org.wololo.wastelands.core.gfx

import org.wololo.wastelands.vmlayer._
import org.wololo.wastelands.core._
import org.wololo.wastelands.core.client.Client
import org.wololo.wastelands.core.client.ClientUnit
import org.wololo.wastelands.core.unit.MoveTileStep

/**
 * Contains the state of the game screen.
 */
class Screen(val client: Client) {

  val vmContext = client.vmContext
  val bitmapFactory = vmContext.bitmapFactory
  val canvasFactory = vmContext.canvasFactory
  
  val Width = vmContext.screenWidth
  val Height = vmContext.screenHeight
  val Bounds: Rect = (0, 0, Width, Height)

  //println("Width:" + Width + " Height:" + Height)

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

  //println("TileSize:" + TileSize + " PixelSize:" + PixelSize)

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
  var screenOffset = new mutable.Coordinate()

  // map tile scroll offset
  var mapOffset = new mutable.Coordinate()

  // map tile pixel scroll offset
  var mapPixelOffset = new mutable.Coordinate()

  def scroll(x: Int, y: Int) {
    screenOffset.setTo(screenOffset.x+x, screenOffset.y+y)

    val maxx = MapScreenWidth - ((TilesWidth + 1) * TileSize)
    val maxy = MapScreenHeight - ((TilesHeight + 1) * TileSize)
    // TODO: must be able to write this bounds logic in a more sane way...
    screenOffset.setTo(if (screenOffset.x < 0) 0 else screenOffset.x, if (screenOffset.y < 0) 0 else screenOffset.y)
    screenOffset.setTo(if (screenOffset.x > maxx) maxx else screenOffset.x, if (screenOffset.y > maxy) maxy else screenOffset.y)
  }

  /**
   * Render the map on screen
   */
  def render() {
    //calculate the tile index for x and y axis
    mapOffset.setTo(calculateTileIndex(screenOffset.x), calculateTileIndex(screenOffset.y))

    //calculate the tile pixel offset for x and y axis
    mapPixelOffset.setTo(calculateTilePixelOffset(screenOffset.x, mapOffset.x), calculateTilePixelOffset(screenOffset.y, mapOffset.y))

    tileRenderer.render(false)
    client.units.values.foreach(unitRenderer.render)
    client.projectiles.foreach(projectileRenderer.render)
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
  
  /**
   * calc screen offset for a client unit
   * optimized (prematurely?) with offset to be calced passed in as a mutable coord
   */
  def calcOffset(unit: ClientUnit, offset: mutable.Coordinate) {
    var x = unit.position.x - mapOffset.x
    var y = unit.position.y - mapOffset.y

    // bail if unit not in current visible part of map
    if (!MapBounds.contains(x, y)) {
      // TODO: remove isOnScreen, just return bool?
      unit.isOnScreen = false
      return
    }
    
    // need to "move" unit back to previous location if moving since the unit position is changed before animating the move 
    // TODO: should be able to refactor this to not check for move action twice... and use pattern match instead of instanceof probably
    if (unit.action.isInstanceOf[MoveTileStep]) {
      x -= unit.direction.x
      y -= unit.direction.y
    }

    x = x * TileSize
    y = y * TileSize

    // if unit is moving, add move distance as pixels to offset
    if (unit.action.isInstanceOf[MoveTileStep]) {
      // TODO: action length from unittype
      var moveDistance = (unit.game.ticks - unit.action.start).toDouble / unit.actionLength(unit.action.actionType)
      //println(unit.game.ticks + " " + unit.action.start + " " + moveDistance)
      x += (TileSize * unit.direction.x * moveDistance).toInt
      y += (TileSize * unit.direction.y * moveDistance).toInt
    }
    
    x += mapPixelOffset.x
    y += mapPixelOffset.y

    // TODO: remove isOnScreen, just return bool?
    unit.isOnScreen = true
    unit.screenBounds.setTo(x, y, x + TileSize, y + TileSize)
    
    offset.setTo(x,y)
  }
}

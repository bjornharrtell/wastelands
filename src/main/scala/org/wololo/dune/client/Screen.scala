package org.wololo.dune.client
import java.awt.image.BufferedImage
import org.wololo.dune.game.Map
import org.wololo.dune.game.TileTypes
import java.awt.Graphics
import java.awt.Color

/**
 * Contains the contents of the game screen with rendering logic.
 */
class Screen(map: Map) {
  val tileSets = Array.ofDim[BufferedImage](4, 5 * 18)

  val TileSize = 32;
  val MapScreenWidth = map.Width * TileSize
  val MapScreenHeight = map.Height * TileSize

  var sx = 0
  var sy = 0

  val factory = new TileSetFactory(TileSize)

  tileSets(TileTypes.Base)(0) = factory.createTileFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/desert.png"))
  tileSets(TileTypes.Dunes) = factory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/dunes.png"))
  tileSets(TileTypes.Rock) = factory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/rock.png"))
  tileSets(TileTypes.Spice) = factory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/spice.png"))

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
  def render(graphics: Graphics, w: Int, h: Int) {
    for{
      y <- -1 to 16
      x <- -1 to 16
    }{ renderTile(graphics, x, y) }
  }

  /**
   * Render a tile with scrolling offset
   */
  def renderTile(graphics: Graphics, x: Int, y: Int) {
    val mxd: Double = map.Width * (sx.toDouble / MapScreenWidth)
    val myd: Double = map.Height * (sy.toDouble / MapScreenHeight)

    val ox: Int = -(mxd % 1 * TileSize).toInt
    val oy: Int = -(myd % 1 * TileSize).toInt

    val dx1 = x * 32 + ox
    val dy1 = y * 32 + oy
    val dx2 = x * 32 + 32 + ox
    val dy2 = y * 32 + 32 + oy

    val mx: Int = mxd.toInt
    val my: Int = myd.toInt

    val tile = map.tiles(mx + x, my + y)

    if (tile == null) {
      graphics.setColor(Color.BLACK)
      graphics.fillRect(dx1, dy2, TileSize, TileSize)
    } else {
      val image = tileSets(tile.baseType)(tile.subType)
      graphics.drawImage(image, dx1, dy1, dx2, dy2, 0, 0, TileSize, TileSize, null)
    }
  }
}
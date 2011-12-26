package org.wololo.dune3.core

import java.awt.image.BufferedImage
import java.awt.Graphics
import java.awt.Color
import org.wololo.dune3.vmlayer.TileSetFactory
import org.wololo.dune3.vmlayer.Image
import org.wololo.dune3.vmlayer.Canvas

/**
 * Contains the contents of the game screen with rendering logic.
 */
class Screen(tileSetFactory: TileSetFactory, map: Map) {
  val tileSets = Array.ofDim[Image](4, 5 * 18)
  var shadeSet = new Array[Image](5 * 18);

  val TileSize = 32;
  val MapScreenWidth = map.Width * TileSize
  val MapScreenHeight = map.Height * TileSize

  var sx = 0
  var sy = 0

  tileSets(TileTypes.Base)(0) = tileSetFactory.createTileFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/desert.png"))
  tileSets(TileTypes.Dunes) = tileSetFactory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/dunes.png"), BufferedImage.TYPE_INT_RGB)
  tileSets(TileTypes.Rock) = tileSetFactory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/rock.png"), BufferedImage.TYPE_INT_RGB)
  tileSets(TileTypes.Spice) = tileSetFactory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/spice.png"), BufferedImage.TYPE_INT_RGB)

  shadeSet = tileSetFactory.createTileSetFromFile(getClass
    .getClassLoader.getResourceAsStream("tilesets/shade.png"), BufferedImage.TYPE_INT_ARGB)

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
    for {
      y <- -1 to 16
      x <- -1 to 16
    } { renderTile(canvas, x, y) }
  }

  /**
   * Render a tile with scrolling offset
   */
  def renderTile(canvas: Canvas, x: Int, y: Int) {
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

    val tx = mx + x;
    val ty = my + y;

    if (tx < 0 || tx > map.Width || ty < 0 || ty > map.Height)
      return

    val tile = map.tiles(mx + x)(my + y)

    val image = tileSets(tile.baseType)(tile.subType)
    canvas.drawImage(image, dx1, dy1, dx2, dy2, 0, 0, TileSize, TileSize)

    if (tile.shade) {
      val shadeImage = shadeSet(tile.shadeSubType);
      canvas.drawImage(shadeImage, dx1, dy1, dx2, dy2, 0, 0, TileSize, TileSize)
    }
  }
}

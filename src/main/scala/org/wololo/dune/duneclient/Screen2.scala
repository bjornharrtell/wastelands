package org.wololo.dune.duneclient
import java.awt.image.BufferedImage
import org.wololo.dune.dunegame.TileTypes
import java.awt.Graphics
import java.awt.Color

/**
 * Contains the contents of the game screen with rendering logic.
 */
class Screen2(map: org.wololo.dune.dunegame.Map) {
  val tileSets = Array.ofDim[BufferedImage](4, 5 * 18);

  val TILESIZE = 32;
  val MAP_SCREEN_WIDTH = map.WIDTH * TILESIZE;
  val MAP_SCREEN_HEIGHT = map.HEIGHT * TILESIZE;

  var sx = 0;
  var sy = 0;

  val factory = new TileSetFactory2(TILESIZE);

  tileSets(TileTypes.TYPE_BASE)(0) = factory.createTileFromFile(getClass()
    .getClassLoader().getResourceAsStream("tilesets/desert.png"));
  tileSets(TileTypes.TYPE_DUNES) = factory.createTileSetFromFile(getClass()
    .getClassLoader().getResourceAsStream("tilesets/dunes.png"));
  tileSets(TileTypes.TYPE_ROCK) = factory.createTileSetFromFile(getClass()
    .getClassLoader().getResourceAsStream("tilesets/rock.png"));
  tileSets(TileTypes.TYPE_SPICE) = factory.createTileSetFromFile(getClass()
    .getClassLoader().getResourceAsStream("tilesets/spice.png"));

  def move(dx: Int, dy: Int) {
    sx += dx;
    sy += dy;

    sx = if (sx < 0) 0 else sx;
    sx = if (sx > MAP_SCREEN_WIDTH - (32 * TILESIZE)) MAP_SCREEN_WIDTH - (32 * TILESIZE) else sx;
    sy = if (sy < 0) 0 else sy;
    sy = if (sy > MAP_SCREEN_HEIGHT - (32 * TILESIZE)) MAP_SCREEN_HEIGHT - (32 * TILESIZE) else sy;
  }

  /**
   * Render the map on screen 16x16 tiles plus 1 tile size border buffer for
   * scrolling.
   */
  def render(graphics: Graphics, w: Int, h: Int) {
    for (y <- -1 until 17) {
      for (x <- -1 until 17) {
        renderTile(graphics, x, y);
      }
    }
  }

  /**
   * Render a tile with scrolling offset
   */
  def renderTile(graphics: Graphics, x: Int, y: Int) {
    val mxd : Double = map.WIDTH * (sx.toDouble / MAP_SCREEN_WIDTH);
    val myd : Double = map.HEIGHT * (sy.toDouble / MAP_SCREEN_HEIGHT);

    val ox: Int = -(mxd % 1 * TILESIZE).toInt;
    val oy: Int = -(myd % 1 * TILESIZE).toInt;

    val dx1 = x * 32 + ox;
    val dy1 = y * 32 + oy;
    val dx2 = x * 32 + 32 + ox;
    val dy2 = y * 32 + 32 + oy;

    val mx: Int = mxd.toInt;
    val my: Int = myd.toInt;

    val tile = map.tiles(mx + x, my + y);

    if (tile == null) {
      graphics.setColor(Color.BLACK);
      graphics.fillRect(dx1, dy2, TILESIZE, TILESIZE);
      return ;
    }

    val image = tileSets(tile.baseType)(tile.subType);

    graphics.drawImage(image, dx1, dy1, dx2, dy2, 0, 0, TILESIZE, TILESIZE, null);
  }
}
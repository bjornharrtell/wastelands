package org.wololo.wastelands.core

object TileRenderer {
  def renderTiles[T](screen: Screen[T], mx: Int, my: Int) {
    // NOTE: while loop used for perf
    var y = -1
    var x = -1
    while (y < 17) {
      x = -1
      while (x < 17) {
        renderTile(screen, mx, my, x, y)
        x += 1
      }

      y += 1
    }
  }
  
  /**
   * @param mx map tile coordinate
   * @param my map tile coordinate
   * @param x screen tile coordinate
   * @param y screen tile coordinate
   */
  def renderTile[T](screen: Screen[T], mx: Int, my: Int, x: Int, y: Int) {
    // screen destination coord
    val sx = x * 32 + screen.ox
    val sy = y * 32 + screen.oy

    // map tile coord
    val tx = mx + x
    val ty = my + y

    // bail if out of map bounds
    if (tx < 0 || tx > screen.map.Width || ty < 0 || ty > screen.map.Height)
      return

    val tile = screen.map.tiles(tx)(ty)

    screen.canvas.drawImage(screen.tileSets(tile.baseType)(tile.subType), sx, sy)

    if (tile.shade) {
      if (tile.shadeSubType == 0) {
        screen.canvas.clearRect(sx, sy, sx + 32, sy + 32)
      } else {
        screen.canvas.drawImage(screen.shadeSet(tile.shadeSubType), sx, sy)
      }
    }
  }
}
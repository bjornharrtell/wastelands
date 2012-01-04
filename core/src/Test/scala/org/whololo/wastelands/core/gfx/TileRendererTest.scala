package org.whololo.wastelands.core.gfx

import org.wololo.wastelands.core.gfx.TileRenderer
import org.junit._
import Assert._
import org.wololo.wastelands.core.Tile

class TileRendererTest {

  @Test
  def tileOnMap(){
    assertEquals(-1, TileRenderer.calculateTileIndex(-33, 32))
    assertEquals(-1, TileRenderer.calculateTileIndex(-32, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(-5, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(-1, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(0, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(1, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(2, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(3, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(4, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(5, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(6, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(7, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(8, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(9, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(10, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(11, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(12, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(13, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(14, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(15, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(16, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(17, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(18, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(19, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(20, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(21, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(22, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(23, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(24, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(25, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(26, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(27, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(28, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(29, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(30, 32))
    assertEquals(0, TileRenderer.calculateTileIndex(31, 32))
    assertEquals(1, TileRenderer.calculateTileIndex(32, 32))
    assertEquals(2, TileRenderer.calculateTileIndex(64, 32))
    assertEquals(4, TileRenderer.calculateTileIndex(130, 32))
  }

  @Test
  def tileOnMap2(){
    for(i <- -33 to 127){
      assertEquals("i is " + i,  oldMapCoord(i), TileRenderer.calculateTileIndex(i, 32))
    }
  }

  @Test
  def tileOffset(){
    assertEquals(1, TileRenderer.calculateTilePixelOffset(-33, -1, 32))
    assertEquals(0, TileRenderer.calculateTilePixelOffset(-32, -1, 32))
    assertEquals(5, TileRenderer.calculateTilePixelOffset(-5, 0, 32))
    assertEquals(1, TileRenderer.calculateTilePixelOffset(-1, 0, 32))
    assertEquals(0, TileRenderer.calculateTilePixelOffset(0, 0, 32))
    assertEquals(-1, TileRenderer.calculateTilePixelOffset(1, 0, 32))
    assertEquals(-2, TileRenderer.calculateTilePixelOffset(2, 0, 32))
    assertEquals(-3, TileRenderer.calculateTilePixelOffset(3, 0, 32))
    assertEquals(-4, TileRenderer.calculateTilePixelOffset(4, 0, 32))
    assertEquals(-5, TileRenderer.calculateTilePixelOffset(5, 0, 32))
    assertEquals(-6, TileRenderer.calculateTilePixelOffset(6, 0, 32))
    assertEquals(-7, TileRenderer.calculateTilePixelOffset(7, 0, 32))
    assertEquals(-8, TileRenderer.calculateTilePixelOffset(8, 0, 32))
    assertEquals(-9, TileRenderer.calculateTilePixelOffset(9, 0, 32))
    assertEquals(-10, TileRenderer.calculateTilePixelOffset(10, 0, 32))
    assertEquals(-11, TileRenderer.calculateTilePixelOffset(11, 0, 32))
    assertEquals(-12, TileRenderer.calculateTilePixelOffset(12, 0, 32))
    assertEquals(-13, TileRenderer.calculateTilePixelOffset(13, 0, 32))
    assertEquals(-14, TileRenderer.calculateTilePixelOffset(14, 0, 32))
    assertEquals(-15, TileRenderer.calculateTilePixelOffset(15, 0, 32))
    assertEquals(-16, TileRenderer.calculateTilePixelOffset(16, 0, 32))
    assertEquals(-17, TileRenderer.calculateTilePixelOffset(17, 0, 32))
    assertEquals(-18, TileRenderer.calculateTilePixelOffset(18, 0, 32))
    assertEquals(-19, TileRenderer.calculateTilePixelOffset(19, 0, 32))
    assertEquals(-20, TileRenderer.calculateTilePixelOffset(20, 0, 32))
    assertEquals(-21, TileRenderer.calculateTilePixelOffset(21, 0, 32))
    assertEquals(-22, TileRenderer.calculateTilePixelOffset(22, 0, 32))
    assertEquals(-23, TileRenderer.calculateTilePixelOffset(23, 0, 32))
    assertEquals(-24, TileRenderer.calculateTilePixelOffset(24, 0, 32))
    assertEquals(-25, TileRenderer.calculateTilePixelOffset(25, 0, 32))
    assertEquals(-26, TileRenderer.calculateTilePixelOffset(26, 0, 32))
    assertEquals(-27, TileRenderer.calculateTilePixelOffset(27, 0, 32))
    assertEquals(-28, TileRenderer.calculateTilePixelOffset(28, 0, 32))
    assertEquals(-29, TileRenderer.calculateTilePixelOffset(29, 0, 32))
    assertEquals(-30, TileRenderer.calculateTilePixelOffset(30, 0, 32))
    assertEquals(-31, TileRenderer.calculateTilePixelOffset(31, 0, 32))
    assertEquals(-32, TileRenderer.calculateTilePixelOffset(32, 0, 32))
    assertEquals(-2, TileRenderer.calculateTilePixelOffset(130, 4, 32))
  }

  @Test
  def tileOffset2(){
    for(i <- -31 to 31){
      assertEquals(oldWay(i), TileRenderer.calculateTilePixelOffset(i, 0, 32))
    }

    for(i <- 33 to 63){
      assertEquals(oldWay(i), TileRenderer.calculateTilePixelOffset(i, 1, 32))
    }

    for(i <- 64 to 95){
      assertEquals(oldWay(i), TileRenderer.calculateTilePixelOffset(i, 2, 32))
    }

    for(i <- 96 to 127){
      assertEquals(oldWay(i), TileRenderer.calculateTilePixelOffset(i, 3, 32))
    }
  }

  def oldWay(s: Int) = {
    val mxd: Double = 64 * (s.toDouble / (Tile.Size * 64))

    val mx = mxd.toInt

    // tile offset calc (for the scrolling buffer)
    val ox = -(mxd % 1 * 32).toInt

    ox
  }

  def oldMapCoord(s: Int) = {
    val mxd: Double = 64 * (s.toDouble / (Tile.Size * 64))

    val mx = mxd.toInt

    mx
  }
}

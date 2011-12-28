package org.wololo.dune3.core

class Map {
  val Width = 64
  val Height = 64

  val tiles = Array.ofDim[Tile](Width, Height)

  val subDef = Array(2, 4, 2, 36, 5, 24, 41, 24, 2, 4, 2, 36, 42, 16, 44, 16,
    3, 51, 3, 53, 25, 7, 13, 7, 39, 54, 39, 67, 25, 7, 13, 7, 2, 4, 2,
    36, 5, 24, 41, 24, 2, 4, 2, 36, 42, 16, 44, 16, 40, 56, 40, 79, 14,
    34, 19, 34, 43, 62, 43, 74, 14, 34, 19, 34, 6, 26, 6, 11, 52, 10,
    60, 10, 6, 26, 6, 11, 58, 28, 64, 28, 23, 8, 23, 29, 9, 1, 32, 1,
    15, 30, 15, 49, 9, 1, 32, 1, 38, 12, 38, 20, 57, 27, 82, 27, 38,
    12, 38, 20, 69, 50, 76, 50, 23, 8, 23, 29, 9, 1, 32, 1, 15, 30, 15,
    49, 9, 1, 32, 1, 2, 35, 2, 46, 5, 17, 41, 17, 2, 35, 2, 46, 42, 21,
    44, 21, 3, 55, 3, 61, 25, 33, 13, 33, 39, 80, 39, 73, 25, 33, 13,
    33, 2, 35, 2, 46, 5, 17, 41, 17, 2, 35, 2, 46, 42, 21, 44, 21, 40,
    68, 40, 71, 14, 48, 19, 48, 43, 72, 43, 65, 14, 48, 19, 48, 37, 26,
    37, 11, 59, 10, 70, 10, 37, 26, 37, 11, 81, 28, 78, 28, 18, 8, 18,
    29, 31, 1, 47, 1, 22, 30, 22, 49, 31, 1, 47, 1, 45, 12, 45, 20, 63,
    27, 77, 27, 45, 12, 45, 20, 75, 50, 66, 50, 18, 8, 18, 29, 31, 1,
    47, 1, 22, 30, 22, 49, 31, 1, 47, 1)

  for {
    y <- 0 until Width;
    x <- 0 until Height
  } {
    val tile = new Tile()
    tile.baseType = TileTypes.Base
    tiles(x)(y) = tile
  }

  // TODO: test data, replace with serialization and map editor.
  tiles(0)(0).baseType = TileTypes.Dunes
  tiles(1)(1).baseType = TileTypes.Dunes

  tiles(15)(5).baseType = TileTypes.Dunes
  tiles(16)(5).baseType = TileTypes.Dunes
  tiles(17)(5).baseType = TileTypes.Dunes
  tiles(15)(6).baseType = TileTypes.Dunes
  tiles(16)(6).baseType = TileTypes.Dunes
  tiles(17)(6).baseType = TileTypes.Dunes
  tiles(15)(7).baseType = TileTypes.Dunes
  tiles(16)(7).baseType = TileTypes.Dunes
  tiles(17)(7).baseType = TileTypes.Dunes

  tiles(25)(5).baseType = TileTypes.Rock
  tiles(26)(5).baseType = TileTypes.Rock
  tiles(27)(5).baseType = TileTypes.Rock
  tiles(25)(6).baseType = TileTypes.Rock
  tiles(26)(6).baseType = TileTypes.Rock
  tiles(27)(6).baseType = TileTypes.Rock
  tiles(25)(7).baseType = TileTypes.Rock
  tiles(26)(7).baseType = TileTypes.Rock
  tiles(27)(7).baseType = TileTypes.Rock

  tiles(25)(8).shade = true
  tiles(26)(8).shade = true
  tiles(27)(8).shade = true
  tiles(25)(9).shade = true
  tiles(26)(9).shade = true
  tiles(27)(9).shade = true
  tiles(25)(10).shade = true
  tiles(26)(10).shade = true
  tiles(27)(10).shade = true

  tiles(23)(8).baseType = TileTypes.Rock
  tiles(24)(8).baseType = TileTypes.Rock
  tiles(24)(7).baseType = TileTypes.Rock

  for {
    y <- 0 until Height;
    x <- 0 until Width
  } {
    makeBorder(x, y)
    tiles(x)(y).shade = true
  }

  /**
   * Calculates subtype for tile if it's suitable
   */
  def makeBorder(x: Int, y: Int) {
    val tile = tiles(x)(y)

    if (tile.baseType != TileTypes.Base) {
      tile.subType = calcSubType(x, y, tile.baseType)
    }
  }

  /**
   * Calculates shade (fog of war) for tile
   */
  def makeShade(x: Int, y: Int) {
    val tile = tiles(x)(y)

    if (tile.shade) {
      tile.shadeSubType = calcShadeSubType(x, y)
    }
  }

  /**
   * Looks at tiles around tile at x,y and calculates a value 0-255 which correspond to the matrix.png asset.
   *
   * Order of investigation:
   *
   * 8 1 2
   * 7 * 3
   * 6 5 4
   */
  def calcSubType(x: Int, y: Int, baseType: Int): Int = {
    val x1 = if (x < 1) 0 else -1
    val x2 = if (x > Width) 0 else 1
    val y1 = if (y < 1) 0 else -1
    val y2 = if (y > Height) 0 else 1

    var subType = 0
    subType = if (tiles(x)(y + y1).baseType == baseType) 1 else 0
    subType = if (tiles(x + x2)(y + y1).baseType == baseType) subType | 2 else subType
    subType = if (tiles(x + x2)(y).baseType == baseType) subType | 4 else subType
    subType = if (tiles(x + x2)(y + y2).baseType == baseType) subType | 8 else subType
    subType = if (tiles(x)(y + y2).baseType == baseType) subType | 16 else subType
    subType = if (tiles(x + x1)(y + y2).baseType == baseType) subType | 32 else subType
    subType = if (tiles(x + x1)(y).baseType == baseType) subType | 64 else subType
    subType = if (tiles(x + x1)(y + y1).baseType == baseType) subType | 128 else subType
    subDef(subType) - 1
  }

  // TODO: refactor, too similar to above
  def calcShadeSubType(x: Int, y: Int): Int = {
    val x1 = if (x < 1) 0 else -1
    val x2 = if (x > Width) 0 else 1
    val y1 = if (y < 1) 0 else -1
    val y2 = if (y > Height) 0 else 1

    var subType = 0
    subType = if (tiles(x)(y + y1).shade) 1 else 0
    subType = if (tiles(x + x2)(y + y1).shade) subType | 2 else subType
    subType = if (tiles(x + x2)(y).shade) subType | 4 else subType
    subType = if (tiles(x + x2)(y + y2).shade) subType | 8 else subType
    subType = if (tiles(x)(y + y2).shade) subType | 16 else subType
    subType = if (tiles(x + x1)(y + y2).shade) subType | 32 else subType
    subType = if (tiles(x + x1)(y).shade) subType | 64 else subType
    subType = if (tiles(x + x1)(y + y1).shade) subType | 128 else subType
    subDef(subType) - 1
  }

  def removeShade(x: Int, y: Int) {
    tiles(x)(y).shade = false
    
    makeShade(x,y-1)
    makeShade(x+1,y-1)
    makeShade(x+1,y)
    makeShade(x+1,y+1)
    makeShade(x,y+1)
    makeShade(x-1,y+1)
    makeShade(x-1,y)
    makeShade(x-1,y-1)
  }
}
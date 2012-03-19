package org.wololo.wastelands.core
import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Direction
import org.wololo.wastelands.core.unit.TileStepEvent
import scala.collection.mutable.ArrayBuffer

class TileOccupationEvent(val tile: Tile, val unit: Unit) extends Event

class GameMap extends Publisher with Subscriber {
  type Pub = GameMap

  val Width = 64
  val Height = 64

  val Bounds: Rect = (0, 0, Width, Height)

  val tiles = Array.fill(Width * Height) { new Tile }

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

  // TODO: test data, replace with serialization and map editor.
  tiles(0, 0).baseType = TileTypes.Dunes
  tiles(1, 1).baseType = TileTypes.Dunes

  tiles(15, 5).baseType = TileTypes.Dunes
  tiles(16, 5).baseType = TileTypes.Dunes
  tiles(17, 5).baseType = TileTypes.Dunes
  tiles(15, 6).baseType = TileTypes.Dunes
  tiles(16, 6).baseType = TileTypes.Dunes
  tiles(17, 6).baseType = TileTypes.Dunes
  tiles(15, 7).baseType = TileTypes.Dunes
  tiles(16, 7).baseType = TileTypes.Dunes
  tiles(17, 7).baseType = TileTypes.Dunes

  tiles(25, 5).baseType = TileTypes.Rock
  tiles(26, 5).baseType = TileTypes.Rock
  tiles(27, 5).baseType = TileTypes.Rock
  tiles(25, 6).baseType = TileTypes.Rock
  tiles(26, 6).baseType = TileTypes.Rock
  tiles(27, 6).baseType = TileTypes.Rock
  tiles(25, 7).baseType = TileTypes.Rock
  tiles(26, 7).baseType = TileTypes.Rock
  tiles(27, 7).baseType = TileTypes.Rock

  tiles(25, 8).shade = true
  tiles(26, 8).shade = true
  tiles(27, 8).shade = true
  tiles(25, 9).shade = true
  tiles(26, 9).shade = true
  tiles(27, 9).shade = true
  tiles(25, 10).shade = true
  tiles(26, 10).shade = true
  tiles(27, 10).shade = true

  tiles(23, 8).baseType = TileTypes.Rock
  tiles(24, 8).baseType = TileTypes.Rock
  tiles(24, 7).baseType = TileTypes.Rock

  for {
    y <- 0 until Height;
    x <- 0 until Width
  } {
    makeBorder((x, y))
    tiles((x, y)).shade = true
  }

  def tiles(coordinate: Coordinate): Tile = {
    tiles(coordinate.y * Height + coordinate.x)
  }

  def tiles(x: Int, y: Int): Tile = {
    tiles(y * Height + x)
  }

  /**
   * Calculates subtype for tile if it's suitable
   */
  def makeBorder(coordinate: Coordinate) {
    if (Bounds.contains(coordinate) == false) return

    val tile = tiles(coordinate)

    if (tile.baseType != TileTypes.Base) tile.subType = calcSubType(coordinate, tile.baseType)
  }

  /**
   * Calculates shade (fog of war) for tile
   */
  def makeShade(coordinate: Coordinate) {
    if (Bounds.contains(coordinate) == false) return

    val tile = tiles(coordinate)

    if (tile.shade) tile.shadeSubType = calcShadeSubType(coordinate)
  }

  private def calcSubType(coordinate: Coordinate, baseType: Int): Int = {
    calcSubType(coordinate, _.baseType == baseType)
  }

  private def calcShadeSubType(coordinate: Coordinate): Int = {
    calcSubType(coordinate, _.shade)
  }

  /**
   * Looks at tiles around tile at x,y and calculates a value 0-255 which correspond to the matrix.png asset.
   *
   * Order of investigation:
   *
   * 8 1 2
   * 7 * 3
   * 6 5 4
   *
   * @param comparator Function to evaluate boolean per tile
   */
  private def calcSubType(coordinate: Coordinate, comparator: Tile => Boolean): Int = {
    var x = coordinate.x
    var y = coordinate.y

    // modify tiles to calc when at border of map
    val x1 = if (x < 1) 0 else -1
    val x2 = if (x > Width - 2) 0 else 1
    val y1 = if (y < 1) 0 else -1
    val y2 = if (y > Height - 2) 0 else 1

    var subType = 0
    subType = if (comparator(tiles(x, y + y1))) 1 else 0
    subType = if (comparator(tiles(x + x2, y + y1))) subType | 2 else subType
    subType = if (comparator(tiles(x + x2, y))) subType | 4 else subType
    subType = if (comparator(tiles(x + x2, y + y2))) subType | 8 else subType
    subType = if (comparator(tiles(x, y + y2))) subType | 16 else subType
    subType = if (comparator(tiles(x + x1, y + y2))) subType | 32 else subType
    subType = if (comparator(tiles(x + x1, y))) subType | 64 else subType
    subType = if (comparator(tiles(x + x1, y + y1))) subType | 128 else subType
    subDef(subType) - 1
  }

  def removeShade(coordinate: Coordinate) {
    if (!Bounds.contains(coordinate)) return

    tiles(coordinate).shade = false

    for (coordinate <- surroundingCoordinates(coordinate)) makeShade(coordinate)
  }

  def removeShadeAround(coordinate: Coordinate, extra: Boolean = false) {
    removeShade(coordinate)
    for (coordinate <- surroundingCoordinates(coordinate)) removeShade(coordinate)

    if (extra) {
      var x = coordinate.x
      var y = coordinate.y
      removeShade((x, y + 2))
      removeShade((x, y - 2))
      removeShade((x + 2, y))
      removeShade((x - 2, y))
    }
  }

  /**
   * TODO: replace with real pathfinding
   */
  def calcDirection(from: Coordinate, to: Coordinate): Option[Direction] = {
    val delta = to - from

    val isDestinationReached = delta == (0, 0)

    if (!isDestinationReached) {
      // calculate tile directions per axis
      val dx = math.signum(delta.x)
      val dy = math.signum(delta.y)

      var direction = Direction(dx, dy)

      // if direction is obstructed try left/right
      if (tiles(from + direction).isOccupied) {
        direction = direction.leftOf
        if (tiles(from + direction).isOccupied) {
          direction = Direction(dx, dy)
          direction = direction.rightOf
          if (tiles(from + direction).isOccupied) return None
        }
      }

      return Option(direction)
    } else {
      return None
    }
  }

  def notify(pub: Publisher, event: Event) {
    event match {
      case x: TileStepEvent => onTileStep(x)
      case _ =>
    }
  }

  def surroundingCoordinates(position: Coordinate, range: Int = 1) : ArrayBuffer[Coordinate] = {
    val array = new ArrayBuffer[Coordinate]

    for {
      y <- position.y - range to position.y + range;
      x <- position.x - range to position.x + range
    } {
      val coordinate = (x,y)
      if (Bounds.contains(coordinate) && coordinate != position) array += coordinate
    }
    
    array
  }
  
  def surroundingTiles(position: Coordinate, range: Int = 1): ArrayBuffer[Tile] = {
    surroundingCoordinates(position, range).map((coordinate: Coordinate) => tiles(coordinate))
  }

  def onTileStep(e: TileStepEvent) {
    tiles(e.from).unit = None
    tiles(e.to).unit = Option(e.unit)

    publish(new TileOccupationEvent(tiles(e.to), e.unit))
  }
}
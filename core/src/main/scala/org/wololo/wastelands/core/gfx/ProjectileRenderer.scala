package org.wololo.wastelands.core.gfx
import org.wololo.wastelands.core.unit.Projectile
import org.wololo.wastelands.core.TileReader
import java.io.File
import org.wololo.wastelands.vmlayer.BitmapTypes
import org.wololo.wastelands.core.Coordinate

class ProjectileRenderer(val screen: Screen) extends TileReader {
  val id = fileToTiles(new File("tilesets/smallother.png"), BitmapTypes.Translucent, 1, 1, 4, 4 * screen.PixelSize)(0)

  private var offset: Coordinate = (0, 0)

  val ro = screen.TileSize / 2 - 2

  def render(projectile: Projectile) {
    var mapOffset = projectile.fromPos - screen.mapOffset

    // bail if unit not in current visible part of map
    if (!screen.MapBounds.contains(mapOffset)) {
      return
    }

    offset.setTo(mapOffset.x * screen.TileSize, mapOffset.y * screen.TileSize)

    offset += screen.mapPixelOffset

    val ox = ((projectile.fromPos.x - projectile.toPos.x) * projectile.distance * screen.TileSize).toInt
    val oy = ((projectile.fromPos.y - projectile.toPos.y) * projectile.distance * screen.TileSize).toInt

    screen.canvas.drawImage(id, offset.x + ox + ro, offset.y + oy + ro)
  }
}
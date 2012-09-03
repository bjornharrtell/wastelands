package org.wololo.wastelands.core.gfx
import org.wololo.wastelands.core.unit.Projectile
import org.wololo.wastelands.core.TileReader
import java.io.File
import org.wololo.wastelands.vmlayer.BitmapTypes
import org.wololo.wastelands.core.Coordinate

class ProjectileRenderer(val screen: Screen) extends TileReader {
  val id = fileToTiles(new File("tilesets/smallother.png"), BitmapTypes.Translucent, 1, 1, 4, 4 * screen.PixelSize)(0)

  // offset to middle of tile
  val ro = screen.TileSize / 2 - 2

  def render(projectile: Projectile) {
    // get map offset coordinate
    var mapOffset = projectile.from - screen.mapOffset

    // bail if unit not in current visible part of map
    if (!screen.MapBounds.contains(mapOffset)) {
      return
    }

    // get screen coordinate for projectile origin
    val origin = new Coordinate(mapOffset.x * screen.TileSize + ro, mapOffset.y * screen.TileSize + ro) + screen.mapPixelOffset

    // calculate elapsed time factor
    val elapsed: Float = (screen.client.ticks - projectile.start) / projectile.duration.toFloat
    
    // calculate offset pixel
    val offset = origin + ((projectile.to-projectile.from) * screen.TileSize * elapsed)
    
    screen.canvas.drawImage(id, offset.x, offset.y)
  }
}
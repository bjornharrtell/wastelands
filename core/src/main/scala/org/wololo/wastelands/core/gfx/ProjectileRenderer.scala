package org.wololo.wastelands.core.gfx
import org.wololo.wastelands.core.unit.Projectile
import java.io.File
import org.wololo.wastelands.vmlayer.BitmapTypes
import org.wololo.wastelands.core.Coordinate

class ProjectileRenderer(val screen: Screen) extends TileReader {
  val id = fileToTiles(new File("tilesets/smallother.png"), BitmapTypes.Translucent, 1, 1, 4, 4 * screen.PixelSize)(0)

  // offset to middle of tile
  val ro = screen.TileSize / 2 - 2

  def render(projectile: Projectile) {
    // calc map offset coordinate
    var x = projectile.from.x - screen.mapOffset.x
    var y = projectile.from.y - screen.mapOffset.y

    // bail if unit not in current visible part of map
    if (!screen.MapBounds.contains(x, y)) {
      return
    }
    
    // calc screen coordinate for projectile origin
    x = x * screen.TileSize + ro + screen.mapPixelOffset.x
    y = y * screen.TileSize + ro + screen.mapPixelOffset.y

    // calculate elapsed time factor
    val elapsed: Float = (screen.client.ticks - projectile.start) / projectile.duration.toFloat
    
    // calculate offset pixel
    x = x + ((projectile.to.x-projectile.from.x) * screen.TileSize * elapsed).toInt
    y = y + ((projectile.to.y-projectile.from.y) * screen.TileSize * elapsed).toInt
    
    screen.canvas.drawImage(id, x, y)
  }
}
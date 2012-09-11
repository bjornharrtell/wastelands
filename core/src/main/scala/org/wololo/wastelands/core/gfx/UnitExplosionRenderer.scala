package org.wololo.wastelands.core.gfx

import java.io.File

import org.wololo.wastelands.core.client.ClientUnit
import org.wololo.wastelands.core.mutable
import org.wololo.wastelands.vmlayer.BitmapTypes

class UnitExplosionRenderer(val screen: Screen, unit: ClientUnit) extends TileReader {
  private val Size = 32 * screen.PixelSize
  private val SizeOffset = -Size / 4
  private val ExplosionOffsetY = -7 * screen.PixelSize
  private val Frames = 27
  private val Step = 8
  private val Explosion = fileToTiles(new File("tilesets/bigexplosion.png"), BitmapTypes.Translucent, 28, 1, 32, Size)

  private var frame = 0
  private var stepCounter = 0

  val offset = new mutable.Coordinate()

  def render(): Boolean = {
    screen.calcOffset(unit, offset)

    screen.canvas.drawImage(Explosion(frame), offset.x + SizeOffset, offset.y + ExplosionOffsetY + SizeOffset)

    stepCounter += 1

    if (stepCounter >= Step) {
      stepCounter = 0
      frame += 1
    }

    // TODO: refactor away, renderer shouldn't change unit state
    // TODO: when refactored unit.alive could/should probably be a derived value from hp
    if (stepCounter == 0 && frame == 6) {
      unit.alive = false
    }

    !(stepCounter == 0 && frame == Frames)
  }
}
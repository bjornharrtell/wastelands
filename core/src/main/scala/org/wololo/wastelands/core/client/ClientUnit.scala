package org.wololo.wastelands.core.client
import org.wololo.wastelands.core.Coordinate
import org.wololo.wastelands.core.unit.Direction
import akka.actor.ActorRef
import org.wololo.wastelands.core.Rect
import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Selectable
import org.wololo.wastelands.core.unit.Fire
import org.wololo.wastelands.core.unit.Projectile

/**
 * Additional state for graphical client units
 */
class ClientUnit(val player: ActorRef, val game: ClientGame, val unitType: Int, var position: Coordinate, var direction: Direction) extends Unit with Selectable {
  val screenBounds = new Rect(10, 10, 10, 10)
  var isOnScreen = false
  var exploding = false
  var explode = false

  override def fire(action: Fire) {
    game.projectiles = game.projectiles :+ new Projectile(game.ticks, position, action.target)
  }
}
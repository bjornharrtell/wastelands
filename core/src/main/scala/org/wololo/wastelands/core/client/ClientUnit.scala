package org.wololo.wastelands.core.client
import org.wololo.wastelands.core.Coordinate
import org.wololo.wastelands.core.unit.Direction
import akka.actor.ActorRef
import org.wololo.wastelands.core.Rect
import org.wololo.wastelands.core.unit.Unit
import org.wololo.wastelands.core.unit.Selectable
import org.wololo.wastelands.core.unit.Fire
import org.wololo.wastelands.core.unit.Projectile

class ClientUnit(player: ActorRef, game: ClientGame, unitType: Int, position: Coordinate, direction: Direction) extends Unit(player, game, unitType, position, direction) with Selectable {
  val screenBounds = new Rect(10, 10, 10, 10)
  var isOnScreen = false
  var exploding = false
  var explode = false

  override def fire(action: Fire) {
    game.projectiles = game.projectiles :+ new Projectile(game.ticks, position, action.target)
  }
}
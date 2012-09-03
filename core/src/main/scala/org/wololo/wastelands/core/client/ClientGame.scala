package org.wololo.wastelands.core.client
import org.wololo.wastelands.core.PlayerGame
import akka.actor.ActorRef
import org.wololo.wastelands.core.unit.Projectile
import org.wololo.wastelands.core.Game

/**
 * Additional game state for graphical clients
 */
trait ClientGame extends PlayerGame[ClientUnit] {
  var projectiles = Vector[Projectile]()
}
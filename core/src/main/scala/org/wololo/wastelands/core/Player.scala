package org.wololo.wastelands.core

import akka.actor._
import org.wololo.wastelands.core.unit._

trait Player extends PlayerGame with Actor {
  var game: ActorRef = null

  def receive = playerReceive

  def playerReceive: Receive = {
    case e: event.GameCreated => join(e.game)
    case e: event.Joined =>
      if (self == e.player) {
        // TODO: create units from scenario definition
        game ! event.CreateUnit(Unit.TestUnit2, (5, 4), Direction.random)
        game ! event.CreateUnit(Unit.TestUnit2, (6, 6), Direction.random)
        game ! event.CreateUnit(Unit.Harvester, (5, 6), Direction.random)
      } else {
        println(e.player + " joined the game.")
      }
    case e: event.TileMapData =>
    // TODO: use map data...
    //gameState.map = e.map
    case e: event.UnitCreated => onUnitCreated(e)
    case e: event.Action =>
      units.get(sender).get.onAction(e)
    case e: event.UnitDestroyed =>
      //units.get(sender).get.explode = true
      map.tiles(units.get(sender).get.position).unit = None
    case e: event.Tick =>
      ticks += 1
      units.values.foreach(_.onTick())
    //projectiles = gameState.projectiles.filterNot(_.start+Projectile.Duration<gameState.ticks)
    case _ => println(self + " received unknown event")
  }

  def onUnitCreated(e: event.UnitCreated) {
    if (self == e.player) map.removeShadeAround(e.position)
    var unitState = new PlayerUnit(e.player, this, e.unitType, e.position, e.direction)
    units += (e.unit -> unitState)
  }

  def join(game: ActorRef) {
    this.game = game
    game ! event.Join()
  }
}
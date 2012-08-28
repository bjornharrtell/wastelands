package org.wololo.wastelands.core

import akka.actor._
import org.wololo.wastelands.core.unit._

class Player(gameState: GamePlayerState) extends Actor {
  var game: ActorRef = null
  
  override def postStop() {
    println("Unit actor stopped: " + self)
  }
  
  def receive = {
    case e: event.Event =>
      if (!e.isInstanceOf[event.Tick]) println("Player " + self + " received " + e)
      e match {
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
      case e: event.UnitCreated =>
        if (self == e.player) gameState.map.removeShadeAround(e.position)
        var unitState = new UnitClientState(e.player, gameState, e.unitType, e.position, e.direction)
        gameState.units += (e.unit -> unitState)
      case e: event.Action =>
        gameState.units.get(sender).get.onAction(e)
      case e: event.UnitDestroyed =>
        gameState.units.get(sender).get.explode = true
        gameState.map.tiles(gameState.units.get(sender).get.position).unit = None
      case e: event.Tick =>        
        gameState.ticks += 1
        gameState.units.values.foreach(_.onTick())
        gameState.projectiles = gameState.projectiles.filterNot(_.start+Projectile.Duration<gameState.ticks)
      case _ =>
    }
  }
  
  def join(game: ActorRef) {
    this.game = game 
    game ! event.Join()
  }
}
package org.wololo.wastelands.core.unit

/**
 * Base trait for units that need to react to game ticks.
 * 
 * Should be mixed in before any other Tickable dependant traits to make this implementation called last.
 * 
 * @deprecated in favor of order/action stuff
 */
trait Tickable {
  self: Unit =>

  val map = game.map

  def tick() : Unit = {
    this
  }
}
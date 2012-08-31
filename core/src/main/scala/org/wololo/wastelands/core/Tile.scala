package org.wololo.wastelands.core

import org.wololo.wastelands.core.unit._
import akka.actor.ActorRef

object Tile {
  val Size = 32
  
  val Base = 0
  val Dunes = 1
  val Rock = 2
  val Spice = 3
}

@SerialVersionUID(1034565814679710344L)
class Tile() extends Serializable {
  /**
   * The tileset this tile belongs to.
   */
  var baseType = Tile.Base

  /**
   * Subtype for tileset borders or specific tile from the base
   * tileset.
   *
   * Subtypes are a value between 0-255 based on the asset blocks.png table.
   */
  var subType = 0
  
  @transient
  var shade = false
  
  @transient
  var shadeSubType = 0
  
  @transient
  def copyFrom(tile: Tile) {
    baseType = tile.baseType
    subType = tile.subType
  }
  
  /**
   * Tile can be occupied by one unit and only one
   * 
   * TODO: should be updated by event instead of inside Unit code?
   */
  @transient
  var unit: Option[Unit] = None
  
  @transient
  def isOccupied = unit.isDefined
}
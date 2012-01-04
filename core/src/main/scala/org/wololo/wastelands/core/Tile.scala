package org.wololo.wastelands.core

object Tile {
  val Size = 32
}

class Tile {

  /**
   * The tileset this tile belongs to.
   */
  var baseType = TileTypes.Base;

  /**
   * Subtype for tileset borders or specific tile from the base
   * tileset.
   *
   * Subtypes are a value between 0-255 based on the asset blocks.png table.
   */
  var subType = 0;
  
  var shade = false;
  
  var shadeSubType = 0;
}
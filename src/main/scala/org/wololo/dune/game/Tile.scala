package org.wololo.dune.game

class Tile {

  /**
   * The tileset this tile belongs to.
   */
  var baseType = TileTypes.TYPE_BASE;

  /**
   * Subtype for tileset borders or specific tile from the base
   * tileset.
   *
   * Subtypes are a value between 0-255 based on the asset blocks.png table.
   */
  var subType = 0;
}
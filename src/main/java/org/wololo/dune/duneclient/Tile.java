package org.wololo.dune.duneclient;

public class Tile {

	final static int TYPE_BASE = 0;
	final static int TYPE_DUNES = 1;
	final static int TYPE_ROCK = 2;
	final static int TYPE_SPICE = 3;

	/**
	 * One of the above defined tilesets
	 */
	int baseType = TYPE_BASE;

	/**
	 * Calculated subtype for tileset borders or specific tile from the base
	 * tileset.
	 * 
	 * Subtypes are a value between 0-255 based on the asset blocks.png table.
	 * @see Map
	 */
	int subType = 0;

	int getBaseType() {
		return baseType;
	}

	void setBaseType(int type) {
		this.baseType = type;
	}

	int getSubType() {
		return subType;
	}

	void setSubType(int subType) {
		this.subType = subType;
	}
}

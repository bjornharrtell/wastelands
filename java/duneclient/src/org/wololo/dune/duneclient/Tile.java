package org.wololo.dune.duneclient;

public class Tile {
	
	final static int TYPE_BASE = 0;
	final static int TYPE_DUNES = 1;
	final static int TYPE_ROCK = 2;
	final static int TYPE_SPICE = 3;
	
	/**
	 * One of the above defined tilesets
	 */
	int baseType;
	
	/**
	 * Calculated subtype for tileset borders or specific tile from the base tileset
	 */
	int subType;

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

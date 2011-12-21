package org.wololo.dune.duneclient;

import java.awt.image.BufferedImage;

public class Tile {
	
	final static int TYPE_DESERT = 0;
	final static int TYPE_DUNES = 1;
	final static int TYPE_ROCK = 2;
	final static int TYPE_SPICE = 3;
	
	int baseType;
	private int subType;

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

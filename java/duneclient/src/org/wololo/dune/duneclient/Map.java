package org.wololo.dune.duneclient;

public class Map {
	final static int WIDTH = 256;
	final static int HEIGHT = 256;
	
	Tile[] tiles = new Tile[WIDTH*HEIGHT];
	
	Map() {
		for (int y = 0; y < WIDTH; y++) {
			for (int x = 0; x <HEIGHT; x++) {
				Tile tile = new Tile();
				tile.setBaseType(Tile.TYPE_DESERT);
				tiles[y*WIDTH+x] = tile;
			}
		}
		
		tiles[0].setBaseType(Tile.TYPE_DUNES);
	}
	
	Tile getTile(int x, int y) {
		return tiles[y*WIDTH+x];
	}
}

package org.wololo.dune.duneclient;

public class Map {
	final static int WIDTH = 64;
	final static int HEIGHT = 64;

	Tile[] tiles = new Tile[WIDTH * HEIGHT];

	Map() {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				Tile tile = new Tile();
				tile.setBaseType(Tile.TYPE_BASE);
				tiles[y * WIDTH + x] = tile;
			}
		}

		getTile(0, 0).setBaseType(Tile.TYPE_DUNES);
		getTile(1, 1).setBaseType(Tile.TYPE_DUNES);
		
		getTile(15, 5).setBaseType(Tile.TYPE_DUNES);
		getTile(16, 5).setBaseType(Tile.TYPE_DUNES);
		getTile(17, 5).setBaseType(Tile.TYPE_DUNES);
	}

	Tile getTile(int x, int y) {
		if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
			return null;
		}

		return tiles[y * HEIGHT + x];
	}
}

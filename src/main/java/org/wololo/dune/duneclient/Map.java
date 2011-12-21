package org.wololo.dune.duneclient;

public class Map {
	final static int WIDTH = 64;
	final static int HEIGHT = 64;

	Tile[] tiles = new Tile[WIDTH * HEIGHT];
	
	/**
	 * Each index corresponds to a position in asset blocks.png and each value is the corresponding tileset image.
	 */
	byte[] subDef = { 2, 4, 2, 36, 5, 24, 41, 24, 2, 4, 2, 36, 42, 16, 44, 16,
			3, 51, 3, 53, 25, 7, 13, 7, 39, 54, 39, 67, 25, 7, 13, 7, 2, 4, 2,
			36, 5, 24, 41, 24, 2, 4, 2, 36, 42, 16, 44, 16, 40, 56, 40, 79, 14,
			34, 19, 34, 43, 62, 43, 74, 14, 34, 19, 34, 6, 26, 6, 11, 52, 10,
			60, 10, 6, 26, 6, 11, 58, 28, 64, 28, 23, 8, 23, 29, 9, 1, 32, 1,
			15, 30, 15, 49, 9, 1, 32, 1, 38, 12, 38, 20, 57, 27, 82, 27, 38,
			12, 38, 20, 69, 50, 76, 50, 23, 8, 23, 29, 9, 1, 32, 1, 15, 30, 15,
			49, 9, 1, 32, 1, 2, 35, 2, 46, 5, 17, 41, 17, 2, 35, 2, 46, 42, 21,
			44, 21, 3, 55, 3, 61, 25, 33, 13, 33, 39, 80, 39, 73, 25, 33, 13,
			33, 2, 35, 2, 46, 5, 17, 41, 17, 2, 35, 2, 46, 42, 21, 44, 21, 40,
			68, 40, 71, 14, 48, 19, 48, 43, 72, 43, 65, 14, 48, 19, 48, 37, 26,
			37, 11, 59, 10, 70, 10, 37, 26, 37, 11, 81, 28, 78, 28, 18, 8, 18,
			29, 31, 1, 47, 1, 22, 30, 22, 49, 31, 1, 47, 1, 45, 12, 45, 20, 63,
			27, 77, 27, 45, 12, 45, 20, 75, 50, 66, 50, 18, 8, 18, 29, 31, 1,
			47, 1, 22, 30, 22, 49, 31, 1, 47, 1 };

	Map() {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				Tile tile = new Tile();
				tile.setBaseType(Tile.TYPE_BASE);
				tiles[y * WIDTH + x] = tile;
			}
		}

		// TODO: test data, replace with serialization and map editor.
		getTile(0, 0).setBaseType(Tile.TYPE_DUNES);
		getTile(1, 1).setBaseType(Tile.TYPE_DUNES);
		
		getTile(15, 5).setBaseType(Tile.TYPE_DUNES);
		getTile(16, 5).setBaseType(Tile.TYPE_DUNES);
		getTile(17, 5).setBaseType(Tile.TYPE_DUNES);
		getTile(15, 6).setBaseType(Tile.TYPE_DUNES);
		getTile(16, 6).setBaseType(Tile.TYPE_DUNES);
		getTile(17, 6).setBaseType(Tile.TYPE_DUNES);
		getTile(15, 7).setBaseType(Tile.TYPE_DUNES);
		getTile(16, 7).setBaseType(Tile.TYPE_DUNES);
		getTile(17, 7).setBaseType(Tile.TYPE_DUNES);
		
		
		getTile(25, 6).setBaseType(Tile.TYPE_ROCK);
		getTile(26, 6).setBaseType(Tile.TYPE_ROCK);
		getTile(27, 6).setBaseType(Tile.TYPE_ROCK);
		getTile(25, 7).setBaseType(Tile.TYPE_ROCK);
		getTile(26, 7).setBaseType(Tile.TYPE_ROCK);
		getTile(27, 7).setBaseType(Tile.TYPE_ROCK);
		getTile(25, 8).setBaseType(Tile.TYPE_ROCK);
		getTile(26, 8).setBaseType(Tile.TYPE_ROCK);
		getTile(27, 8).setBaseType(Tile.TYPE_ROCK);
		
		getTile(24, 7).setBaseType(Tile.TYPE_ROCK);
		getTile(23, 8).setBaseType(Tile.TYPE_ROCK);
		getTile(24, 8).setBaseType(Tile.TYPE_ROCK);
		
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				makeBorder(x, y);
			}
		}
	}

	Tile getTile(int x, int y) {
		if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
			return null;
		}

		return tiles[y * HEIGHT + x];
	}
	
	/**
	 * Calculates subtype for tile if it's suitable
	 */
	void makeBorder(int x, int y) {
		if (x<1 || x>=WIDTH || y<1 || y>=HEIGHT)
			return;
		
		Tile tile = tiles[y * WIDTH + x];
		
		int baseType = tile.getBaseType();
		
		if (baseType == Tile.TYPE_BASE) return;
		
		tile.setSubType(subDef[calcSubType(tile.getBaseType(), x, y)]-1);
	}
	
	/**
	 * Looks at tiles around tile at x,y and calculates a value 0-255 which correspond to the matrix.png asset.
	 * 
	 * Order of investigation:
	 * 
	 * 8 1 2
	 * 7 * 3
	 * 6 5 4
	 */
	int calcSubType(int baseType, int x, int y) {
		int subType;
		subType = tiles[(y-1) * WIDTH + x].getBaseType() == baseType ? 1 : 0;
		subType = tiles[(y-1) * WIDTH + (x+1)].getBaseType() == baseType ? subType | 2 : subType;
		subType = tiles[y * WIDTH + (x+1)].getBaseType() ==baseType ? subType | 4 : subType;
		subType = tiles[(y+1) * WIDTH + (x+1)].getBaseType() == baseType? subType | 8 : subType;
		subType = tiles[(y+1) * WIDTH + x].getBaseType() == baseType ? subType | 16 : subType;
		subType = tiles[(y+1) * WIDTH + (x-1)].getBaseType() == baseType ? subType | 32 : subType;
		subType = tiles[y * WIDTH + (x-1)].getBaseType() == baseType ? subType | 64 : subType;
		subType = tiles[(y-1) * WIDTH + (x-1)].getBaseType() == baseType ? subType | 128 : subType;
		return subType;
	}
}

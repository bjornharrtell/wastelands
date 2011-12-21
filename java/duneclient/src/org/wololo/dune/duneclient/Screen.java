package org.wololo.dune.duneclient;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Screen {

	BufferedImage desertTile;
	BufferedImage[][] tileSets = new BufferedImage[3][5 * 18];

	int TILESET_TYPE_DUNES = 0;
	int TILESET_TYPE_ROCK = 1;
	
	int ox = 0;
	int oy = 0;
	
	int mx = 0;
	int my = 0;
	
	Map map;

	Screen(Map map) throws IOException {
		TileSetFactory factory = new TileSetFactory();

		desertTile = factory.createTileFromFile(new File(
				"../../resources/tilesets/desert.png"));
		tileSets[0] = factory.createTileSetFromFile(new File(
				"../../resources/tilesets/dunes.png"));
		tileSets[1] = factory.createTileSetFromFile(new File(
				"../../resources/tilesets/spice.png"));
		
		this.map = map;
	}
	
	void scrollX() {
		
		ox++;
		
		if (ox>32)
			ox = 0;
	}

	void render(Graphics graphics, int w, int h) {
		for (int y = -1; y < 17; y++) {
			for (int x = -1; x < 17; x++) {
				
				Tile tile = map.getTile(x+1,y+1);
				int baseType = tile.getBaseType();
				
				BufferedImage image = desertTile;
				
				if (baseType == Tile.TYPE_DUNES) {
					image = tileSets[0][0];
				}

				int dx1 = x * 32 + ox;
				int dy1 = y * 32 + oy;
				int dx2 = x * 32 + 32 + ox;
				int dy2 = y * 32 + 32 + oy;

				int sx1 = 0;
				int sy1 = 0;
				int sx2 = 16;
				int sy2 = 16;

				graphics.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2,
						sy2, null);
			}
		}
	}

}

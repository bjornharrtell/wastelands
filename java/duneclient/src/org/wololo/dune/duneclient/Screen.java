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

	Screen() throws IOException {
		TileSetFactory factory = new TileSetFactory();

		desertTile = factory.createTileFromFile(new File(
				"../../resources/tilesets/desert.png"));
		tileSets[0] = factory.createTileSetFromFile(new File(
				"../../resources/tilesets/dunes.png"));
		tileSets[1] = factory.createTileSetFromFile(new File(
				"../../resources/tilesets/spice.png"));
	}

	void render(Graphics graphics, int w, int h) {
		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				BufferedImage tile = desertTile;
				if (x<10) {
					tile = tileSets[0][x];
				}
				if (x<5) {
					tile = tileSets[1][x];
				}
				
				graphics.drawImage(tile, x * 32, y * 32, x * 32 + 32,
						y * 32 + 32, 0, 0, 16, 16, null);
			}
		}
	}

}

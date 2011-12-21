package org.wololo.dune.duneclient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Screen {

	BufferedImage[][] tileSets = new BufferedImage[4][5 * 18];

	int tileSize;

	int ox = 0;
	int oy = 0;

	int mx = 0;
	int my = 0;

	Map map;

	Screen(Map map, int tileSize) throws IOException {
		this.tileSize = tileSize;

		TileSetFactory factory = new TileSetFactory(tileSize);

		tileSets[Tile.TYPE_BASE][0] = factory.createTileFromFile(new File(
				"../../resources/tilesets/desert.png"));
		tileSets[Tile.TYPE_DUNES] = factory.createTileSetFromFile(new File(
				"../../resources/tilesets/dunes.png"));
		tileSets[Tile.TYPE_SPICE] = factory.createTileSetFromFile(new File(
				"../../resources/tilesets/spice.png"));

		this.map = map;
	}

	void scrollX() {

		ox--;

		if (ox < -32) {
			ox = 0;
			mx++;
		}
	}

	/**
	 * Render the map on screen 16x16 tiles double size scaled. 1 tile size
	 * buffer for scrolling.
	 */
	void render(Graphics graphics, int w, int h) {
		for (int y = -1; y < 17; y++) {
			for (int x = -1; x < 17; x++) {
				renderTile(graphics, x, y);
			}
		}
	}

	void renderTile(Graphics graphics, int x, int y) {
		int dx1 = x * 32 + ox;
		int dy1 = y * 32 + oy;
		int dx2 = x * 32 + 32 + ox;
		int dy2 = y * 32 + 32 + oy;

		int sx1 = 0;
		int sy1 = 0;
		int sx2 = tileSize;
		int sy2 = tileSize;

		Tile tile = map.getTile(mx + x, y);

		if (tile == null) {
			graphics.setColor(Color.BLACK);
			graphics.fillRect(dx1, dy2, tileSize, tileSize);
			return;
		}

		int baseType = tile.getBaseType();
		BufferedImage image = tileSets[baseType][0];

		graphics.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
	}

}

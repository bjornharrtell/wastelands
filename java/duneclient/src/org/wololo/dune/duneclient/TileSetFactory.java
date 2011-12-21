package org.wololo.dune.duneclient;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Creates tilesets from resources
 */
public class TileSetFactory {

	int size;

	TileSetFactory(int size) {
		this.size = size;
	}

	BufferedImage createTileFromFile(File file) throws IOException {

		BufferedImage image = ImageIO.read(file);

		BufferedImage tile = new BufferedImage(size, size,
				BufferedImage.TYPE_INT_RGB);
		tile.getGraphics().drawImage(image, 0, 0, size, size, 0, 0, 16, 16,
				null);

		return tile;
	}

	BufferedImage[] createTileSetFromFile(File file) throws IOException {

		BufferedImage image = ImageIO.read(file);

		BufferedImage[] tileSet = new BufferedImage[5 * 18];

		int count = 0;

		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 18; x++) {
				BufferedImage tile = new BufferedImage(size, size,
						BufferedImage.TYPE_INT_RGB);

				int sx1 = x * 16 + 1 + x;
				int sy1 = y * 16 + 1 + y;
				int sx2 = sx1 + 16;
				int sy2 = sy1 + 16;

				tile.getGraphics().drawImage(image, 0, 0, size, size, sx1, sy1,
						sx2, sy2, null);

				tileSet[count++] = tile;
			}
		}

		return tileSet;
	}

}

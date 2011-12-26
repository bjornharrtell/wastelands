package org.wololo.dune3.jvm
import java.io.InputStream
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import org.wololo.dune3.vmlayer.TileSetFactory
import org.wololo.dune3.vmlayer.Image

class AWTTileSetFactory(size: Int) extends TileSetFactory {

  def createTileFromFile(inputStream: InputStream): Image = {
    val image = ImageIO.read(inputStream)

    val tile = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    tile.getGraphics.drawImage(image, 0, 0, size, size, 0, 0, 16, 16, null);

    new AWTImage(tile)
  }

  def createTileSetFromFile(inputStream: InputStream, imageType: Int): Array[Image] = {
    val image = ImageIO.read(inputStream)

    val tileSet = new Array[Image](5 * 18)

    var count = 0

    for (
      y <- 0 until 5;
      x <- 0 until 18
    ) {
      val tile = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)

      val sx1 = x * 16 + 1 + x
      val sy1 = y * 16 + 1 + y
      val sx2 = sx1 + 16
      val sy2 = sy1 + 16

      tile.getGraphics.drawImage(image, 0, 0, size, size, sx1, sy1, sx2, sy2, null)

      tileSet(count) = new AWTImage(tile)
      count += 1
    }

    tileSet
  }
}

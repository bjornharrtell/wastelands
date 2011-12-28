package org.wololo.dune3.jvm
import java.io.InputStream
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import org.wololo.dune3.vmlayer.TileSetFactory
import java.awt.GraphicsEnvironment
import java.awt.Transparency
import org.wololo.dune3.core.TileTypes

class AWTTileSetFactory(size: Int) extends TileSetFactory {

  val ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
  val gs = ge.getDefaultScreenDevice();
  val gc = gs.getDefaultConfiguration();

  def createTileFromFile(inputStream: InputStream): Object = {
    val image = ImageIO.read(inputStream)

    val tile = gc.createCompatibleImage(size, size, Transparency.OPAQUE)

    tile.getGraphics.drawImage(image, 0, 0, size, size, 0, 0, 16, 16, null)

    tile
  }

  def createTileSetFromFile(inputStream: InputStream, transparent: Boolean): Array[Object] = {
    val image = ImageIO.read(inputStream)

    val transparency = if (transparent) Transparency.BITMASK else Transparency.OPAQUE

    val tileSet = new Array[Object](5 * 18)

    var count = 0

    for (
      y <- 0 until 5;
      x <- 0 until 18
    ) {

      val tile = gc.createCompatibleImage(size, size, transparency)

      val sx1 = x * 16 + 1 + x
      val sy1 = y * 16 + 1 + y
      val sx2 = sx1 + 16
      val sy2 = sy1 + 16

      tile.getGraphics.drawImage(image, 0, 0, size, size, sx1, sy1, sx2, sy2, null)

      tileSet(count) = tile
      count += 1
    }

    tileSet
  }
}

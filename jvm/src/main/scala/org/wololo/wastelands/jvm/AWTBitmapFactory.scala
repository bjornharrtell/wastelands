package org.wololo.wastelands.jvm
import org.wololo.wastelands.vmlayer.BitmapFactory
import java.awt.GraphicsEnvironment
import javax.imageio.ImageIO
import java.awt.Transparency
import java.io.InputStream
import org.wololo.wastelands.vmlayer.BitmapTypes
import java.awt.image.BufferedImage

object AWTBitmapFactory extends BitmapFactory[BufferedImage] {
  val ge = GraphicsEnvironment.getLocalGraphicsEnvironment
  val gs = ge.getDefaultScreenDevice
  val gc = gs.getDefaultConfiguration
  
  def create(width: Int, height: Int, bitmapType: Int): BufferedImage = {
    gc.createCompatibleImage(width, height, parseBitmapType(bitmapType))
  }
  def create(inputStream: InputStream): BufferedImage = {
    ImageIO.read(inputStream)
  }
  
  private def parseBitmapType(bitmapType: Int) : Int = {
    bitmapType match {
      case BitmapTypes.Opague => Transparency.OPAQUE
      case BitmapTypes.Bitmask => Transparency.BITMASK
      case BitmapTypes.Translucent => Transparency.TRANSLUCENT
    }
  }
}
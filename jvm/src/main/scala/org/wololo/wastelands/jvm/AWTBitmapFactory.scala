package org.wololo.wastelands.jvm
import org.wololo.wastelands.vmlayer.BitmapFactory
import java.awt.GraphicsEnvironment
import javax.imageio.ImageIO
import java.awt.Transparency
import java.io.InputStream
import org.wololo.wastelands.vmlayer.BitmapTypes
import java.awt.image.BufferedImage
import java.awt.image.ColorConvertOp
import java.awt.color.ColorSpace
import java.awt.image.IndexColorModel
import java.awt.Color
import java.awt.CompositeContext
import java.awt.AlphaComposite
import java.awt.image.ColorModel
import java.io.File

object AWTBitmapFactory extends BitmapFactory[BufferedImage] {
  val ge = GraphicsEnvironment.getLocalGraphicsEnvironment
  val gs = ge.getDefaultScreenDevice
  val gc = gs.getDefaultConfiguration

  def create(width: Int, height: Int, bitmapType: Int): BufferedImage = {
    gc.createCompatibleImage(width, height, parseBitmapType(bitmapType))
  }
  
  def create(file: File): BufferedImage = {
    ImageIO.read(file)
  }
  
  def createShadow(bitmap: BufferedImage): BufferedImage = {
    val shadow = gc.createCompatibleImage(32, 32, Transparency.TRANSLUCENT)
    var g = shadow.createGraphics
    g.setColor(new Color(0, 0, 0))
    g.fillRect(0, 0, 32, 32)
    g.dispose
    applyGrayscaleMaskToAlpha(shadow, bitmap)

    shadow
  }

  def applyGrayscaleMaskToAlpha(image: BufferedImage, mask: BufferedImage) {
    val width = image.getWidth
    val height = image.getHeight

    val imagePixels = image.getRGB(0, 0, width, height, null, 0, width)
    val maskPixels = mask.getRGB(0, 0, width, height, null, 0, width)

    for (i <- 0 until imagePixels.length) {
      val color = imagePixels(i) & 0x00ffffff // mask preexisting alpha
      var alpha = maskPixels(i) & 0x6f000000 // mask alpha with upper threshold

      imagePixels(i) = color | alpha
    }

    image.setRGB(0, 0, width, height, imagePixels, 0, width)
  }

  private def parseBitmapType(bitmapType: Int): Int = {
    bitmapType match {
      case BitmapTypes.Opague => Transparency.OPAQUE
      case BitmapTypes.Bitmask => Transparency.BITMASK
      case BitmapTypes.Translucent => Transparency.TRANSLUCENT
    }
  }
}
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
import scala.collection.mutable.ArrayBuffer

object AWTBitmapFactory extends BitmapFactory {
  val ge = GraphicsEnvironment.getLocalGraphicsEnvironment
  val gs = ge.getDefaultScreenDevice
  val gc = gs.getDefaultConfiguration
  
  val bitmaps = ArrayBuffer[BufferedImage]()

  def create(width: Int, height: Int, bitmapType: Int): Int = {
    bitmaps += gc.createCompatibleImage(width, height, parseBitmapType(bitmapType))
    bitmaps.size-1
  }
  
  def create(file: File): Int = {
    bitmaps += ImageIO.read(file)
    bitmaps.size-1
  }
  
  def createShadow(id: Int): Int = {
    val bitmap = bitmaps(id)
    
    val shadow = gc.createCompatibleImage(32, 32, Transparency.TRANSLUCENT)
    val g = shadow.createGraphics
    g.setColor(new Color(0, 0, 0))
    g.fillRect(0, 0, 32, 32)
    g.dispose()
    applyGrayscaleMaskToAlpha(shadow, bitmap)

    bitmaps += shadow
    bitmaps.size-1
  }

  private def applyGrayscaleMaskToAlpha(image: BufferedImage, mask: BufferedImage) {
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
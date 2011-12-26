package org.wololo.dune3.jvm
import java.awt.image.BufferedImage
import org.wololo.dune3.vmlayer.Image

class AWTImage(bufferedImage: BufferedImage) extends Image {
 def bufferedImage() : BufferedImage = { bufferedImage }
}
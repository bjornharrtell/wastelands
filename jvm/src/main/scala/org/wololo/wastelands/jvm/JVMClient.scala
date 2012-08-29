package org.wololo.wastelands.jvm
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.awt.image.BufferedImage
import java.awt.DisplayMode
import java.awt.Frame
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment
import org.wololo.wastelands.core.Game
import org.wololo.wastelands.core.GameMapEditor
import org.wololo.wastelands.core.Coordinate
import org.wololo.wastelands.core.event.Touch
import org.wololo.wastelands.core.client.KeyCode
import org.wololo.wastelands.core.client.Client
import akka.actor._
import org.wololo.wastelands.core.client.ClientApp

object JVMClient extends JVMContext with ClientApp with WindowListener with MouseListener with MouseMotionListener with KeyListener {
  val device = GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice
  var gc = device.getDefaultConfiguration

  var mainFrame: Frame = null

  var isFullscreen = false
  var canRender = true

  def main(args: Array[String]) {
    mainFrame = newFrame
    mainFrame.setVisible(true)
    
    run()
    
    mainFrame.dispose()
    JVMSoundFactory.dispose()
  }

  def newFrame: Frame = {
    val frame = new Frame(gc)
    frame.setBounds(0, 0, screenWidth, screenHeight)
    frame.setIgnoreRepaint(true)
    frame.setResizable(false)

    frame.addWindowListener(this)
    frame.addKeyListener(this)
    frame.addMouseListener(this)
    frame.addMouseMotionListener(this)

    frame
  }

  def getBestDisplayMode(device: GraphicsDevice): DisplayMode = {
    val modes = device.getDisplayModes
    for (i <- 0 until modes.length) {
      val mode = modes(i)
      if (mode.getWidth == screenWidth
        && mode.getHeight == screenHeight) {
        return mode
      }
    }
    null
  }

  def chooseBestDisplayMode(device: GraphicsDevice) {
    val best = getBestDisplayMode(device)
    if (best != null) {
      device.setDisplayMode(best)
    }
  }

  def render(id: Int) {
    if (!canRender) return

    val bufferStrategy = mainFrame.getBufferStrategy
    if (bufferStrategy == null) {
      mainFrame.createBufferStrategy(2)
    } else {
      val graphics = bufferStrategy.getDrawGraphics
      // NOTE: I think this is the fastest drawImage for this purpose
      graphics.drawImage(AWTBitmapFactory.bitmaps(id), 0, 0, screenWidth, screenHeight, null)
      graphics.dispose()
      bufferStrategy.show()
    }
  }

  def toggleFullscreen() {
    if (isFullscreen) {
      canRender = false

      mainFrame.dispose()

      mainFrame = newFrame
      mainFrame.setVisible(true)

      canRender = true
      isFullscreen = false
    } else {
      canRender = false

      mainFrame.dispose()
      mainFrame = newFrame
      mainFrame.setUndecorated(true)

      device.setFullScreenWindow(mainFrame)
      val best = getBestDisplayMode(device)
      device.setDisplayMode(best)

      canRender = true
      isFullscreen = true
    }
  }

  def windowClosing(e: WindowEvent) {
    running = false
  }

  def windowOpened(e: WindowEvent) {}
  def windowClosed(e: WindowEvent) {}
  def windowIconified(e: WindowEvent) {}
  def windowDeiconified(e: WindowEvent) {}
  def windowActivated(e: WindowEvent) {}
  def windowDeactivated(e: WindowEvent) {}

  def keyPressed(e: KeyEvent) {
    val keyCode = e.getKeyCode
    if (keyCode == KeyEvent.VK_F11) {
      toggleFullscreen()
    } else if (keyCode == KeyEvent.VK_Q) {
      running = false
    } else {
      keyCode match {
        /*case KeyEvent.VK_1 => client.keyDown(KeyCode.KEY_1)
        case KeyEvent.VK_2 => client.keyDown(KeyCode.KEY_2)
        case KeyEvent.VK_3 => client.keyDown(KeyCode.KEY_3)
        case KeyEvent.VK_4 => client.keyDown(KeyCode.KEY_4)
        case KeyEvent.VK_5 => client.keyDown(KeyCode.KEY_5)
        case KeyEvent.VK_9 => client.keyDown(KeyCode.KEY_9)
        case KeyEvent.VK_0 => client.keyDown(KeyCode.KEY_0)*/
        case _ =>
      }
    }
  }

  def keyReleased(e: KeyEvent) {}
  def keyTyped(e: KeyEvent) {}
  def mouseDragged(e: MouseEvent) = client ! Touch((e.getX, e.getY), Touch.MOVE)
  def mouseMoved(e: MouseEvent) {}
  def mouseClicked(e: MouseEvent) {}
  def mouseEntered(e: MouseEvent) {}
  def mouseExited(e: MouseEvent) {}
  def mousePressed(e: MouseEvent) = client ! Touch((e.getX, e.getY), Touch.DOWN)
  def mouseReleased(e: MouseEvent) = client ! Touch((e.getX, e.getY), Touch.UP)
}

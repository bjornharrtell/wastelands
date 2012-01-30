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

object Client extends Runnable with WindowListener with MouseListener with MouseMotionListener with KeyListener with JVMContext {
  var game: Game = null

  var prevX = 0
  var prevY = 0

  val device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
  var gc = device.getDefaultConfiguration()

  var mainFrame: Frame = null

  var isFullscreen = false
  var canRender = true

  override def run() {
    game.run()
    mainFrame.dispose
  }

  def main(args: Array[String]) {
    createFrame
    mainFrame.setVisible(true)

    game = new Game(this)

    new Thread(this).start()
  }

  def createFrame() {
    mainFrame = new Frame(gc)
    mainFrame.setBounds(0, 0, screenWidth, screenHeight)
    mainFrame.setIgnoreRepaint(true)
    mainFrame.setResizable(false)

    mainFrame.addWindowListener(this)
    mainFrame.addKeyListener(this)
    mainFrame.addMouseListener(this)
    mainFrame.addMouseMotionListener(this)
  }

  def getBestDisplayMode(device: GraphicsDevice): DisplayMode = {
    val modes = device.getDisplayModes()
    for (i <- 0 until modes.length) {
      val mode = modes(i)
      if (mode.getWidth() == screenWidth
        && mode.getHeight() == screenHeight) {
        return mode
      }
    }
    null
  }

  def chooseBestDisplayMode(device: GraphicsDevice) = {
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

      mainFrame.dispose

      createFrame
      mainFrame.setVisible(true)

      canRender = true
      isFullscreen = false
    } else {
      canRender = false

      mainFrame.dispose
      createFrame
      mainFrame.setUndecorated(true)

      device.setFullScreenWindow(mainFrame)
      val best = getBestDisplayMode(device)
      device.setDisplayMode(best)

      canRender = true
      isFullscreen = true
    }
  }

  def windowClosing(e: WindowEvent) {
    game.running = false
  }

  def windowOpened(e: WindowEvent) {}
  def windowClosed(e: WindowEvent) {}
  def windowIconified(e: WindowEvent) {}
  def windowDeiconified(e: WindowEvent) {}
  def windowActivated(e: WindowEvent) {}
  def windowDeactivated(e: WindowEvent) {}

  def keyPressed(e: KeyEvent) {
    val keyCode = e.getKeyCode()
    if (keyCode == KeyEvent.VK_F11) {
      toggleFullscreen
    } else if (keyCode == KeyEvent.VK_Q) {
      game.running = false
    }
  }

  def keyReleased(e: KeyEvent) {}
  def keyTyped(e: KeyEvent) {}

  def mouseDragged(e: MouseEvent) {
    val x = e.getX
    val y = e.getY

    val dx = prevX - x
    val dy = prevY - y

    game.scroll(dx, dy)

    prevX = x
    prevY = y
  }

  def mouseMoved(e: MouseEvent) {
    val x = e.getX
    val y = e.getY

    prevX = x
    prevY = y
  }

  def mouseClicked(e: MouseEvent) {
    val x = e.getX
    val y = e.getY

    game.click(x, y)
  }

  def mouseEntered(e: MouseEvent) {}
  def mouseExited(e: MouseEvent) {}
  def mousePressed(e: MouseEvent) {}
  def mouseReleased(e: MouseEvent) {}
}

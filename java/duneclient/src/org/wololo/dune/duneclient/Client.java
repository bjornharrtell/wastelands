package org.wololo.dune.duneclient;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Client extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	static final int WIDTH = 16 * 2 * 16;
	static final int HEIGHT = 16 * 2 * 16;

	boolean running = false;
	int tickCount = 0;
	
	Screen screen;

	void start() {
		running = true;
		new Thread(this).start();
	}

	void init() {
		try {
			screen = new Screen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60.0;
		int frames = 0;
		int ticks = 0;
		long lastTimer1 = System.currentTimeMillis();

		init();

		while (running) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = false;
			while (unprocessed >= 1.0) {
				ticks++;
				tick();
				unprocessed -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
				System.out.println(ticks + " ticks, " + frames + " fps");
				frames = 0;
				ticks = 0;
			}
		}
	}

	void tick() {
		tickCount++;
	}

	void render() {
		BufferStrategy bufferStrategy = getBufferStrategy();
		if (bufferStrategy == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics graphics = bufferStrategy.getDrawGraphics();
		
		screen.render(graphics, WIDTH, HEIGHT);
		
		graphics.dispose();
		bufferStrategy.show();
	}

	public static void main(final String[] argv) {
		Client client = new Client();
		client.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		client.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		client.setMinimumSize(new Dimension(WIDTH, HEIGHT));

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(client);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

		client.start();
	}
}

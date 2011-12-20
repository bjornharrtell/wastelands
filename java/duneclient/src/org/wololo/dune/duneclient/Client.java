package org.wololo.dune.duneclient;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Client extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	static final int WIDTH = 16*2*16;
	static final int HEIGHT = 16*2*16;
	
	boolean running = true;
	
	BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	void start() {
		new Thread(this).start();
	}
	
	void runStep() {
		tick();
		render();
	}
	
	void tick() {
	}
	
	void render() {
		BufferStrategy bufferStrategy = getBufferStrategy();
		if (bufferStrategy==null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = image.getGraphics();
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
		graphics.dispose();
		
		graphics = bufferStrategy.getDrawGraphics();
		graphics.drawImage(image, 0,0, getWidth(), getHeight(), null);
		graphics.dispose();
		bufferStrategy.show();
	}

	@Override
	public void run() {
		while(running) {
			runStep();
		}
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

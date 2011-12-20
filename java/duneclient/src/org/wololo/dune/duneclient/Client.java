package org.wololo.dune.duneclient;

import java.awt.Frame;

public class Client extends Frame {
	public static void main(final String[] argv) {
		new Client();
	}
	
	long justNow;
	long now;
	
	boolean running = true;
	
	Client() {
		while (running) {
			now = System.nanoTime();
		}
	}
	
	void tick() {
		
	}
	
	void render() {
		
	}
}

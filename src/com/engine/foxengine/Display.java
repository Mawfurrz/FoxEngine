package com.engine.foxengine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
//import com.engine.foxengine.graphics.Render;
import com.engine.foxengine.graphics.Screen;
import com.engine.foxengine.gui.Launcher;
import com.engine.foxengine.input.Controller;
import com.engine.foxengine.input.InputHandler;

public class Display extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	//public static final int WIDTH = 1280;
	//public static final int HEIGHT = 720;
	// WIDTH and HEIGHT need place holder values or
	// Exception in thread "main" java.lang.IllegalArgumentException: Width (0) and height (0) cannot be <= 0
	// Is thrown
	public static int WIDTH = 800;
	public static int HEIGHT = 600;
	public static final String TITLE = "FoxEngine";
	
	
	private Thread thread;
	private Screen screen;
	private Game game;
	private BufferedImage img;
	private boolean running = false;
	private int[] pixels;
	private InputHandler input;
	private int fps;
	public static int selection = 0;
	
	static Launcher launcher;
	
	public static int mouseSpeed;
	
	private int oldX = 0;
	private int newX = 0;
	
	public Display() {
		//render = new Render(WIDTH, HEIGHT);
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		screen = new Screen(getGameWidth(), getGameHeight());
		game = new Game();
		img = new BufferedImage(getGameWidth(), getGameHeight(), BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		
		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
	}
	
	public static Launcher getLauncherInstance() {
		if (launcher == null) {
			launcher = new Launcher(0);
		}
		return launcher;
		
	}
	
	public static int getGameWidth() {
		/*if (selection == 0) {
			WIDTH = 640;
		}
		if (selection == 1 || selection == -1) {
			WIDTH = 800;
		}
		if (selection == 2) {
			WIDTH = 1024;
		}
		if (selection == 3) {
			WIDTH = 1280;
		}*/
		
		return WIDTH;
	}
	
	public static int getGameHeight() {
		/*if (selection == 0) {
			HEIGHT = 480;
		}
		if (selection == 1 || selection == -1) {
			HEIGHT = 600;
		}
		if (selection == 2) {
			HEIGHT = 768;
		}
		if (selection == 3) {
			HEIGHT = 720;
		}*/
		
		return HEIGHT;
	}
	
	
	public synchronized void start() {
		if (running) {
			return;
		}
		running = true;
		thread = new Thread(this, "game");
		thread.start();
		
		System.out.println("start method called!");
	}
	
	public synchronized void stop() {
		if (!running) {
			return;
		}
		running = false;
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception Message: " + e);
			System.exit(0);
		}
		
		System.out.println("stop method called!");
	}
	
	private void tick() {
		game.tick(input.key);
		
		newX = InputHandler.MouseX;
		
		if (newX > oldX) {
			//System.out.println("Going Right");
			Controller.turnRight = true;
		}
		if (newX < oldX) {
			//System.out.println("Going Left");
			Controller.turnLeft = true;
		}
		if (newX == oldX) {
			//System.out.println("Not Moving");
			Controller.turnLeft = false;
			Controller.turnRight = false;
		}
		mouseSpeed = Math.abs(newX - oldX);
		oldX = newX;
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.render(game);
		
		for (int i = 0; i < getGameWidth() * getGameHeight(); i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, getGameWidth(), getGameHeight(), null);
		g.setFont(new Font("Cascadia Code", 2, 22));
		g.setColor(Color.WHITE);
		g.drawString(fps + " FPS", 20, 30);
		g.dispose();
		bs.show();
	}
	
	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1/60.0;
		int tickCount = 0;
		boolean ticked = false;
		requestFocus();
		
		while (running) {
			//tick();
			//render();
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;
			//launcher.updateFrame();
			
			while (unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0) {
					//System.out.println(frames + " FPS");
					previousTime += 1000;
					fps = frames;
					frames = 0;
				}
			}
			
			if (ticked) {
				render();
				frames++;
			}
			
			//System.out.println("X: " + InputHandler.MouseX + " Y: " + InputHandler.MouseY);
		}
	}
	
	public static void main(String[] args) {
		//Display display = new Display();
		getLauncherInstance(); // launcher instance
	}
}

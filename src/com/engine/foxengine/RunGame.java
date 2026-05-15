package com.engine.foxengine;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class RunGame {
	
	public RunGame() {
		BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankcursor = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0,0), "blank");
		Display game = new Display();
		JFrame frame = new JFrame();
		frame.add(game);
		//frame.pack();
		frame.setSize(Display.getGameWidth(), Display.getGameHeight());
		frame.getContentPane().setCursor(blankcursor);
		frame.setTitle(Display.TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		
		game.start();
		stopMenuThread();
	}
	
	
	private void stopMenuThread() {
		Display.getLauncherInstance().stopMenu();
	}
}

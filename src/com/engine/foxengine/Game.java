package com.engine.foxengine;

import java.awt.event.KeyEvent;

import com.engine.foxengine.input.Controller;
import com.engine.foxengine.level.Level;

public class Game {
	
	public int time;
	public Controller controls;
	public Level level;
	
	public Game() {
		controls = new Controller();
		level = new Level(80, 80);
	}
	
	public void tick(boolean[] key) {
		time++;
		boolean forward 	= key[KeyEvent.VK_W];
		boolean backward 	= key[KeyEvent.VK_S];
		boolean left 		= key[KeyEvent.VK_A];
		boolean right 		= key[KeyEvent.VK_D];
		
		boolean rleft 		= key[KeyEvent.VK_LEFT];
		boolean rright 		= key[KeyEvent.VK_RIGHT];
		boolean jump 		= key[KeyEvent.VK_SPACE];
		boolean crouch 		= key[KeyEvent.VK_CONTROL];
		boolean sprint 		= key[KeyEvent.VK_SHIFT];
		//boolean turnLeft 		= key[KeyEvent.VK_LEFT];
		//boolean turnRight 	= key[KeyEvent.VK_RIGHT]; 
		
		
		controls.tick(forward, backward, left, right, rleft, rright, jump, crouch, sprint);
	}
}

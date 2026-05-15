package com.engine.foxengine.graphics;

// import java.util.Random;
import com.engine.foxengine.Game;

public class Screen extends Render{
	
	// object testing the Render Class
	//private Render test;
	private Render3D render3d;
	
	public Screen(int width, int height) {
		super(width, height);
		//Random random = new Random();
		//test = new Render(256, 256);
		render3d = new Render3D(width, height);
		//for (int i = 0; i < 256*256; i++) {
			//test.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
		//}
	}
	
	public void render(Game game) {
		 
		for (int i = 0; i<width*height; i++) {
			pixels[i] = 0;
		}
		
		render3d.floor(game);
		// xLeft, xRight, zDistanceLeft, zDistanceRight, yHeight
		/*render3d.renderWalls(0, 0.5, 1, 1, 0);
		render3d.renderWalls(0, 0, 1.5, 1, 0);
		render3d.renderWalls(0.5, 0.5, 1, 1.5, 0);
		render3d.renderWalls(0.5, 0, 1.5, 1.5, 0);*/
		//render3d.renderWalls(0, 0, 1, 1.5, 0);
		render3d.renderDistanceLimiter();
		//render3d.walls();
		draw(render3d, 0, 0);
	}
}

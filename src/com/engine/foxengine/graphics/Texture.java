package com.engine.foxengine.graphics;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Texture {
	
	// 8x8
	public static Render floor = loadBitmap("/textures/floor.png");
	public static Render wall = loadBitmap("/textures/wall.png");
	public static Render target = loadBitmap("/textures/target.png");
	
	
	// 32x32
	public static Render grass32 = loadBitmap("/textures/grass32.png");
	public static Render wall32 = loadBitmap("/textures/wall32.png");
	public static Render sky32 = loadBitmap("/textures/sky32.png");
	public static Render target32 = loadBitmap("/textures/target32.png");
	
	public static Render loadBitmap(String fileName) {
		try {
			BufferedImage image = ImageIO.read(Texture.class.getResource(fileName));
			int width = image.getWidth();
			int height = image.getHeight();
			Render result = new Render(width, height);
			image.getRGB(0, 0, width, height, result.pixels, 0, width);
			return result;
		} catch (Exception e) {
			System.out.println("Textures failed to load!");
			throw new RuntimeException(e);
		}
	}
}

package com.engine.foxengine.graphics;

import java.util.Random;

import com.engine.foxengine.Display;
import com.engine.foxengine.Game;
import com.engine.foxengine.input.Controller;
import com.engine.foxengine.level.Block;
import com.engine.foxengine.level.Level;

public class Render3D extends Render{
	
	public double[] zBuffer;
	public double[] zBufferWall;
	private double renderDistance = 2000.0;
	private double forwardMove, rightMove, vertical, cosine, sine, walkingAnim;
	Random random = new Random(100);
	
	int c = 0; // ceilingValue

	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[width*height];
		zBufferWall = new double[width];
	}
	
	//double time = 0;
	
	public void floor(Game game) {
		
		for (int x = 0; x < width; x++) {
			zBufferWall[x] = 0;
		}
		
		double floorPosition = 8.0;
		double ceilingPosition = 8.0;
		forwardMove = game.controls.z;
		rightMove = game.controls.x;
		vertical = game.controls.y;
		walkingAnim = 0;
		// Math.sin(game.time / 10.00) * 2;
				
		// game.time / 100.0
		double rotation = game.controls.rotation;
		cosine = Math.cos(rotation);
		sine = Math.sin(rotation);
		
		for (int y = 0; y < height; y++) {
			double ceiling = (y + -height / 2.0) / height;
			
			// can control floor
			// z = (floor modifier) / ceiling
			double z = (floorPosition + vertical) / ceiling;
			c = 0;
			
			if (Controller.walking) {
				walkingAnim = Math.sin(game.time / 6.0) * 1;
				z = (floorPosition + vertical + walkingAnim) / ceiling;
			}
			
			if (Controller.crouching && Controller.walking) {
				walkingAnim = Math.sin(game.time / 6.0) * 0.25;
				z = (floorPosition + vertical + walkingAnim) / ceiling;
			}
			
			if (Controller.running && Controller.walking) {
				walkingAnim = Math.sin(game.time / 6.0) * 1.25;
				z = (floorPosition + vertical + walkingAnim) / ceiling;
			}
			
			if (ceiling < 0) {
				// can control height of the ceiling
				// z = (height modifier) / -ceiling;
				z = (ceilingPosition - vertical) / -ceiling;
				c = 1;
				if (Controller.walking) {
					z = (ceilingPosition - vertical - walkingAnim) / -ceiling;
				}
			}
		
			
			//time += 0.0005;
			
			for (int x = 0; x < width; x++) {
				double xDepth = (x - width / 2.0) / height;
				xDepth *= z;
				// converting depth to int and uses bit-wise to show visible pixels
				double xx = xDepth  * cosine + z * sine;
				double yy = z * cosine - xDepth * sine;
				// left/right movement
				// game.time
				int xPix = (int) ((xx + rightMove) * 4); // multiply by resolution adjustment * 4, etc.
				// up/down movement
				int yPix = (int) ((yy + forwardMove) * 4); // multiply by resolution adjustment * 4, etc.
				zBuffer[x+y*width] = z;
				//pixels[x+y*width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;
				if (c == 0) {
					pixels[x+y*width] = Texture.grass32.pixels[(xPix & 30) + (yPix & 30) * 32];
				} //else {
					//pixels[x+y*width] = Texture.sky32.pixels[(xPix & 30) + (yPix & 30) * 32];
				//}
				
				if (z > 500) {
					pixels[x + y * width] = 0;
				}
			}
		}
		
		Level level = game.level;
		int size = 20;
		for (int xBlock = -size; xBlock <= size; xBlock++) {
			for (int zBlock = -size; zBlock <= size; zBlock++) {
				Block block = level.create(xBlock, zBlock);
				Block east = level.create(xBlock + 1, zBlock);
				Block south = level.create(xBlock, zBlock + 1);
				
				if (block.solid) {
					if (!east.solid) {
						renderWalls(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0);
					}
					if (!south.solid) {
						renderWalls(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0);
					}
				}
				else {
					if (east.solid) {
						renderWalls(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0);
					}
					if (south.solid) {
						renderWalls(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0);
					}
				}
			}
		}
		
		for (int xBlock = -size; xBlock <= size; xBlock++) {
			for (int zBlock = -size; zBlock <= size; zBlock++) {
				Block block = level.create(xBlock, zBlock);
				Block east = level.create(xBlock + 1, zBlock);
				Block south = level.create(xBlock, zBlock + 1);
				
				if (block.solid) {
					if (!east.solid) {
						renderWalls(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0.5);
					}
					if (!south.solid) {
						renderWalls(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0.5);
					}
				}
				else {
					if (east.solid) {
						renderWalls(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0.5);
					}
					if (south.solid) {
						renderWalls(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0.5);
					}
				}
			}
		}
		
		for (int xBlock = -size; xBlock <= size; xBlock++) {
			for (int zBlock = -size; zBlock <= size; zBlock++) {
				Block block = level.create(xBlock, zBlock);
				
				for (int s = 0; s < block.sprites.size(); s++) {
					Sprite sprite = block.sprites.get(s);
					renderSprite(xBlock+sprite.x, sprite.y, zBlock + sprite.z , 0.5);
				}
			}
		}
	}
	
	public void renderSprite(double x, double y, double z, double hOffset) {
		double upCorrect = -0.125;
		double rightCorrect = 0.0625;
		double forwardCorrect = 0.0625;
		double walkingCorrect = 0.0625;
		
		
		double xc = ((x / 2) - (rightMove  * rightCorrect)) * 2 + hOffset;
		double yc = ((y / 2) - (vertical  * upCorrect)) + (walkingAnim * walkingCorrect) * 2 + hOffset;
		double zc = ((z / 2) - (forwardMove * forwardCorrect)) * 2;
		
		double rotX = xc * cosine - zc * sine;
		double rotY = yc;
		double rotZ = zc * cosine + xc * sine;
		
		double xCenter = Display.WIDTH / 2;
		double yCenter = Display.HEIGHT / 2;
		
		double xPixel = rotX / rotZ * height + xCenter;
		double yPixel = rotY / rotZ * height + yCenter;
		
		double xPixelL = xPixel - height / 2 / rotZ;
		double xPixelR = xPixel + height / 2 / rotZ;
		
		double yPixelL = yPixel - height / 2 / rotZ;
		double yPixelR = yPixel + height / 2 / rotZ;
		
		int xpl = (int) xPixelL;
		int xpr = (int) xPixelR;
		int ypl = (int) yPixelL;
		int ypr = (int) yPixelR;
		
		if (xpl < 0) {
			xpl = 0;
		}
		
		if (xpr > width) {
			xpr = width;
		}
		
		if (ypl < 0) {
			ypl = 0;
		}
		
		if (ypr > height) {
			ypr = height;
		}
		
		rotZ *= 8;
		
		for (int yp = ypl; yp < ypr; yp++) {
			double pixelRotY = (yp - yPixelR) / (yPixelL - yPixelR);
			int yTexture = (int) ((pixelRotY * 8) * 4); // multiply by resolution adjustment * 4, etc.
			
			for (int xp = xpl; xp < xpr; xp++) {
				double pixelRotX = (xp - xPixelR) / (xPixelL - xPixelR);
				int xTexture = (int) ((pixelRotX * 8) * 4); // multiply by resolution adjustment * 4, etc.
				
				if (zBuffer[xp + yp * width] > rotZ) {
					int color = Texture.target32.pixels[(xTexture & 30) + (yTexture & 30) * 32];
					if (color != 0xffff00ff) {
						pixels[xp + yp * width] = color;
						zBuffer[xp + yp * width] = rotZ;
					}
				}
			}
		}
	}
	
	public void renderWalls(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {
		
		double upCorrect = 0.0625;
		double rightCorrect = 0.0625;
		double forwardCorrect = 0.0625;
		double walkingCorrect = -0.0625;
		
		// c stands for calculate
		double xcLeft = ((xLeft / 2) - (rightMove  * rightCorrect)) * 2;
		double zcLeft = ((zDistanceLeft / 2) - (forwardMove * forwardCorrect)) * 2;
		
		double rotationLeftSideX = xcLeft * cosine - zcLeft * sine;
		// TL = Top Left corner of block
		// BL = Bottom left corner of block
		double yCornerTL = ((-yHeight) - (-vertical * upCorrect + (walkingAnim * walkingCorrect))) * 2;
		double yCornerBL = ((+0.5 - yHeight) - (-vertical * upCorrect + (walkingAnim * walkingCorrect))) * 2;
		double rotationLeftSideZ = zcLeft * cosine + xcLeft * sine;
		
		
		double xcRight = ((xRight / 2) - (rightMove  * rightCorrect)) * 2;
		double zcRight = ((zDistanceRight / 2) - (forwardMove * forwardCorrect)) * 2;
		
		double rotationRightSideX = xcRight * cosine - zcRight * sine;
		double yCornerTR = ((-yHeight) - (-vertical * upCorrect + (walkingAnim * walkingCorrect))) * 2;
		double yCornerBR = ((+0.5 - yHeight) - (-vertical * upCorrect + (walkingAnim * walkingCorrect))) * 2;
		double rotationRightSideZ = zcRight * cosine + xcRight * sine;
		
		double texture30 = 0;
		double texture40 = 8;
		// Original value was 0.5
		double clip = 0.5;
		
		if (rotationLeftSideZ < clip && rotationRightSideZ < clip) {
			return;
		}
		
		if (rotationLeftSideZ < clip) {
			double clip0 = (clip - rotationLeftSideZ) / (rotationRightSideZ - rotationLeftSideZ);
			
			rotationLeftSideZ = rotationLeftSideZ + (rotationRightSideZ - rotationLeftSideZ) * clip0;
			rotationLeftSideX = rotationLeftSideX + (rotationRightSideX - rotationLeftSideX) * clip0;
			texture30 = texture30 + (texture40 - texture30) * clip0;
		}
		
		if (rotationRightSideZ < clip) {
			double clip0 = (clip - rotationLeftSideZ) / (rotationRightSideZ - rotationLeftSideZ);
			
			rotationRightSideZ = rotationLeftSideZ + (rotationRightSideZ - rotationLeftSideZ) * clip0;
			rotationRightSideX = rotationLeftSideX + (rotationRightSideX - rotationLeftSideX) * clip0;
			texture40 = texture30 + (texture40 - texture30) * clip0;
		}
		
		double xPixelLeft 	= (rotationLeftSideX / rotationLeftSideZ * height + width / 2);
		double xPixelRight 	= (rotationRightSideX / rotationRightSideZ * height + width / 2);
		
		if (xPixelLeft >= xPixelRight) {
			return;
		}
		
		int xPixelLeftInt = (int) (xPixelLeft);
		int xPixelRightInt = (int) (xPixelRight);
		
		if (xPixelLeftInt < 0) {
			xPixelLeftInt = 0;
		}
		
		if (xPixelRightInt > width) {
			xPixelRightInt = width;
		}
		
		double yPixelLeftTop 		= yCornerTL / rotationLeftSideZ * height + height / 2.0;
		double yPixelLeftBottom 	= yCornerBL / rotationLeftSideZ * height + height / 2.0;
		double yPixelRightTop 		= yCornerTR / rotationRightSideZ * height + height / 2.0;
		double yPixelRightBottom 	= yCornerBR / rotationRightSideZ * height + height / 2.0;
		
		double texture1 = 1 / rotationLeftSideZ;
		double texture2 = 1 / rotationRightSideZ;
		double texture3 = texture30 / rotationLeftSideZ;
		double texture4 = texture40 / rotationRightSideZ - texture3;
		
		for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
			double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);
			double zWall = (texture1 + (texture2 - texture1) * pixelRotation);
			
			if (zBufferWall[x] > zWall) {
				continue;
			}
			
			zBufferWall[x] = zWall;
			
			int xTexture = (int) (((texture3 + texture4 * pixelRotation) / zWall) * 4); // multiply by resolution adjustment * 4, etc.
			
			
			double yPixelTop 	= yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
			double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;
			
			int yPixelTopInt 	= (int) (yPixelTop);
			int yPixelBottomInt = (int) (yPixelBottom);
			
			if (yPixelTopInt < 0) {
				yPixelTopInt = 0;
			}
			
			if (yPixelBottomInt > height) {
				yPixelBottomInt = height;
			}
			
			for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
				
				double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);
				int yTexture = (int) ((8 * pixelRotationY) * 4); // multiply by resolution adjustment * 4, etc.
				
				try {
					
					//pixels[x + y * width] = xTexture * 128 + yTexture * 128;//0x3074AB;;
					pixels[x+y*width] = Texture.wall32.pixels[(xTexture & 30) + (yTexture & 30) * 32];
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
					continue;
				}
				// Last Multiplication changes brightness of walls, lower value means walls brighter
				// father away, visa versa.
				zBuffer[x + y * width] = 1 / (texture1 + (texture2 - texture1) * pixelRotation) * 8;
			}
		}
	}
	
	/*
	public void walls() {
		for (int i = 0; i < 10000; i++) {
			// x horizontal, y vertical, z depth 
			// x minus moves wall to the left, x plus moves wall to right
			double xx = random.nextDouble();
			double yy = random.nextDouble();
			double zz = 1.5;
			
			int xPixel = (int) (xx / zz * height / 2 + width / 2);
			int yPixel = (int) (yy / zz * height / 2 + height / 2);
			
			// this ensures that we don't render any pixels off of the screen.
			// if the x coord is greater to 0 or equal to zero, render the pixel on the screen,
			// otherwise do not render, same thing for y.
			if (xPixel >= 0 && yPixel >= 0 && xPixel < width && yPixel < height) {
				pixels[xPixel + yPixel * width] = 0xfffff;
			}
		}
	}*/
	
	public void renderDistanceLimiter() {
		for (int i = 0; i < width*height; i++) {
			int color = pixels[i];
			int brightness = (int) (renderDistance / (zBuffer[i]));
			
			if (brightness < 0) {
				brightness = 0;
			}
			
			if (brightness > 255) {
				brightness = 255;
			}
			
			int r = (color >> 16) & 0xff;
			int g = (color >> 8) & 0xff;
			int b = (color) & 0xff;
			
			r = r*brightness / 255;
			g = g*brightness / 255;
			b = b*brightness / 255;
			
			pixels[i] = r << 16 | g << 8 | b;
		}
	}
}

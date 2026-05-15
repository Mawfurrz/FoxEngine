package com.engine.foxengine.level;

import java.util.ArrayList;
import java.util.List;

import com.engine.foxengine.graphics.Sprite;

public class Block {
	
	public boolean solid = false;
	public static Block solidWall = new SolidBlock();
	public List<Sprite> sprites = new ArrayList<Sprite>();
	
	public void addSprite(Sprite sprite) {
		sprites.add(sprite);
	}
}

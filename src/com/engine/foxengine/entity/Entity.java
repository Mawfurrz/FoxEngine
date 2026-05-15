package com.engine.foxengine.entity;

public class Entity {
	
	protected double x, z;
	protected boolean removed = false;
	
	protected Entity() {
		
	}
	
	public void remove() {
		removed = true;
	}
}

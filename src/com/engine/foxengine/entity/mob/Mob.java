package com.engine.foxengine.entity.mob;

import com.engine.foxengine.entity.Entity;

public class Mob extends Entity {
	
	public void move(int xa, int za, double rot) {
		if (xa != 0 && za != 0) {
			move(xa, 0, rot);
			move(0, za, rot);
			return;
		}
		
		double nx = xa * Math.cos(rot) + za * Math.sin(rot);
		double nz = za * Math.cos(rot) - xa * Math.sin(rot);
		x += nx;
		z += nz;
	}
}

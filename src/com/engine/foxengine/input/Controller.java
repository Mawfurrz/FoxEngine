package com.engine.foxengine.input;

public class Controller {
	
	public double x, z, y, rotation, xa, za, ya, rotationa;
	public static boolean turnLeft = false;
	public static boolean turnRight = false;
	public static boolean walking = false;
	public static boolean crouching = false;
	public static boolean running = false;
	//Mob mob = new Mob();
	
	public void tick(boolean forward, boolean backward, boolean left, boolean right, boolean rleft, boolean rright, boolean jump, boolean crouch, boolean sprint) {
		double rotationSpeed = 0.025;
		double walkSpeed = 0.5;
		double jumpHeight = 0.5;
		double crouchDepth = 0.3; // higher the number lower the crouch
		double xMove = 0.0;
		double zMove = 0.0;
		//int xa = 0;
		//int za = 0;
		
		if (forward) {
			//za++;
			zMove++;
			walking = true;
		}
		
		if (backward) {
			//za--;
			zMove--;
			walking = true;
		}
		
		if (left) {
			//xa--;
			xMove--;
			walking = true;
		}
		
		if (right) {
			//xa++;
			xMove++;
			walking = true;
		}
		
		if (rleft) {
			rotationa -= rotationSpeed;
		}
		
		if (rright) {
			rotationa += rotationSpeed;
		}
		
		//mob.move(xa, za, rotation);
		
		if (jump) {
			y += jumpHeight;
			sprint = false;
		}
		
		if (crouch) {
			y -= crouchDepth;
			sprint = false;
			crouching = true;
			walkSpeed = 0.25;
		}
 		
		/*if (turnLeft) {
			rotationa -= rotationSpeed;
		}
		
		if (turnRight) {
			rotationa += rotationSpeed;
		}*/
		
		// sprint && !jump && !crouch
		if (sprint) {
			walkSpeed = 0.75;
			walking = true;
			running = true;
		}
		
		if (!forward && !backward && !left && !right) {
			walking = false;
		}
		
		if (!crouch) {
			crouching = false;
		}
		
		if (!sprint) {
			running = false;
		}

		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;
		
		x += xa;
		y *= 0.9;
		z += za;
		xa *= 0.1;
		za *= 0.1;
		rotation += rotationa;
		rotationa *= 0.5;
	}
}

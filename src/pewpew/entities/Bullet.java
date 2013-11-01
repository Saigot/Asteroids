/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.entities;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.openal.Audio;

/**
 * 
 * @author michael
 */
public abstract class Bullet implements Entity {
	public float x;
	public float y;

	public float xv;
	public float yv;
	public float speed;
	public float angle;

	public static int cost;
	public static int CoolDown;

	public boolean cullable = false;

	public Polygon shape;
	public int score;
	public static Audio FireSound;
	public static double dmgDealMult = 1;
	public String type;

	public Bullet(float X, float Y, float Angle) {
		x = X;
		y = Y;
		angle = Angle;
	}

	public int getScore() {
		return score;
	}

	public abstract void special();

	@Override
	public Polygon getBounds() {
		return shape;
	}

	@Override
	public String getSuperType() {
		return "Bullet";
	}
}

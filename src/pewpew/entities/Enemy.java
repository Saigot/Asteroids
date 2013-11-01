/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Polygon;

/**
 * 
 * @author michael
 */
abstract public class Enemy implements Entity {

	public int IntialParalysis;
	public float size;
	public short tick = 0;
	public float x;
	public float y;
	public float xv;
	public float yv;
	public float xa;
	public float ya;
	public int health;
	public int MaxHealth;
	public float maxSpeed;
	public Polygon shape;
	public Image form;
	public boolean cullable = false;

	public float GetX() {
		return x;
	}

	public float GetY() {
		return y;
	}

	public abstract void SetKillCounts(boolean KC);

	public abstract boolean GetKillCounts();

	public abstract boolean isKilled();

	public abstract void SetSpawnProb(double SP);

	public abstract double GetSpawnProb();

	@Override
	public abstract void Death(byte conditions);

	@Override
	public abstract void render(GameContainer gc, Graphics g);

	public int GetHealth() {
		return health;
	}

	@Override
	public Polygon getBounds() {
		return shape;
	}

	@Override
	public String GetSuperType() {
		return "Enemy";
	}
}

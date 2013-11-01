/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.entities;

import org.newdawn.slick.geom.Polygon;

/**
 * 
 * @author michael
 */
abstract public class PowerUps implements Entity {
	public float x;
	public float y;

	public float xv;
	public float yv;

	public float xa;
	public float ya;

	public float size;

	public int EffectTime;
	public int EffectTimeleft;
	public int timeout;
	public int TimeRemaining;

	public Polygon shape;
	public boolean cullable = false;
	public boolean Collided;

	@Override
	public String GetType() {
		return "PowerUps";
	}

	public abstract void SetSpawnProb(Double Prob);

	public abstract double GetSpawnProb();
}

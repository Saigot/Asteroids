/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

/**
 * 
 * @author michael
 */
public interface Entity {
	static final int FORM_WIDTH = 1000;
	static final int FORM_HEIGHT = 700;
	static final float FRICTION = 0.05f;

	/**
	 * 
	 * @param conditions
	 *            : 0 = normal 1 = cull 2+ = other death functions
	 */
	public abstract void death(byte conditions);

	public abstract void render(GameContainer gc, Graphics g);

	// checks whether parent or it's children has collided with e, if so returns
	// the child or parent
	// that collided else null
	public abstract void collides(Entity... en);

	public abstract Polygon getBounds();

	public float doDamage();

	public void takeDamage(float Damage);

	public void move(Entity e);

	public boolean cull();

	public String getType();

	public String getSuperType();

	public Entity[] getAllChildren();

}

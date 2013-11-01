/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.entities.powerups;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

import pewpew.entities.Entity;
import pewpew.entities.Player;
import pewpew.entities.PowerUps;

/**
 * 
 * @author michael randomly chooses one of the powerups in existence and uses
 *         all it's effects image is an animation flashing through all available
 *         sprites
 */

public class RandomUp extends PowerUps {

	public Image RandomUp;
	public PowerUps ActivePowerUp;
	public static double SpawnProbibility = 0.0;

	public RandomUp() throws SlickException {
		timeout = (int) (Math.random() * 500) + 500;
		TimeRemaining = timeout;
		x = (int) (Math.random() * FORM_WIDTH);
		y = (int) (Math.random() * FORM_HEIGHT);
		size = (float) (Math.random() * 0.5 + 0.5);

		shape = new Polygon(new float[] { x, y, x + 50 * size, y,
				x + 50 * size, y + 50 * size, x, y + 50 * size });
		RandomUp = new Image("Res/PowerUps/RandomUp.png");
	}

	@Override
	public void Death(byte conditions) {
		cullable = true;
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		if (!Collided) {
			RandomUp.draw(x, y, size);
			// g.draw(shape);
		}
	}

	@Override
	public void Collides(Entity... en) {
		for (Entity e : en) {
			if (shape.intersects(e.getBounds()) && !Collided) {
				if (e.GetType().equals("Player")) {
					Player p = (Player) e;
					ActivePowerUp = GetCurrentPowerUp();
					ActivePowerUp.Move(e);
					ActivePowerUp.Collides(p);
					Collided = true;
				}
				return;
			}
		}
		return;
	}

	public PowerUps GetCurrentPowerUp() {
		double rand = Math.random();
		PowerUps p = null;
		try {
			if (rand > 0.5) {
				p = new ShrinkUp(x, y, size * 50);
			} else if (rand > 0.2) {
				p = (new HealthUp(x, y, size * 50));
			} else {
				p = (new InfiniteShot(x, y, size));
			}
		} catch (SlickException ex) {
			Logger.getLogger(RandomUp.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return p;
	}

	@Override
	public Polygon getBounds() {
		return shape;
	}

	@Override
	public float DoDamage() {
		return 0;
	}

	@Override
	public void TakeDamage(float Damage) {
	}

	@Override
	public void Move(Entity e) {
		TimeRemaining--;
		if (TimeRemaining <= 0 && !Collided) {
			Death((byte) 0);
		}

		if (Collided) {
			ActivePowerUp.Move(e);
		}
	}

	@Override
	public boolean Cull() {
		if (ActivePowerUp != null) {
			if (ActivePowerUp.Cull()) {
				cullable = true;
				return true;
			} else {
				return false;
			}
		}
		return cullable;

	}

	@Override
	public String GetType() {
		return "RandomUp";
	}

	@Override
	public String GetSuperType() {
		return super.GetType();
	}

	@Override
	public Entity[] GetAllChildren() {
		return null;
	}

	@Override
	public void SetSpawnProb(Double Prob) {
		SpawnProbibility = Prob;
	}

	@Override
	public double GetSpawnProb() {
		return SpawnProbibility;
	}
}

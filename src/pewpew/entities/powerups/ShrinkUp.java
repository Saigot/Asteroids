/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.entities.powerups;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import pewpew.entities.Entity;
import pewpew.entities.Player;
import pewpew.entities.PowerUps;
import pewpew.entities.guns.BulletNormal;

/**
 * 
 * @author michael
 */
public class ShrinkUp extends PowerUps {
	Image healthUp;
	float InitialSize;
	float bonus;
	boolean collided = false;
	Player affected;
	Audio sound;
	public static double SpawnProbibility = 0.0;

	public ShrinkUp() throws SlickException {
		this((float) (Math.random() * FORM_WIDTH),
				(float) (Math.random() * FORM_HEIGHT));
	}

	public ShrinkUp(float X, float Y) throws SlickException {
		this(X, Y, (float) (Math.random() * 0.5 + 0.5));
	}

	public ShrinkUp(float X, float Y, float SIZE) throws SlickException {
		timeout = (int) (Math.random() * 500) + 500;
		TimeRemaining = timeout;
		x = X;
		y = Y;
		size = SIZE;
		bonus = (float) (Math.random() * (1.50) + 0.25);
		healthUp = new Image("Res/PowerUps/ShrinkUp.png");
		shape = new Polygon(new float[] { x, y, x + 50 * size, y,
				x + 50 * size, y + 50 * size, x, y + 50 * size });
		EffectTime = (int) (Math.random() * 1200) + 600;
		try {
			sound = AudioLoader.getAudio("OGG", ResourceLoader
					.getResourceAsStream("Res/Sounds/ShrinkUpOn.ogg"));
		} catch (IOException ex) {
			Logger.getLogger(BulletNormal.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	@Override
	public void Death(byte conditions) {
		try {
			sound = AudioLoader.getAudio("OGG", ResourceLoader
					.getResourceAsStream("Res/Sounds/ShrinkUpOff.ogg"));
		} catch (IOException ex) {
			Logger.getLogger(BulletNormal.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		if (collided) {
			affected.SetScale(InitialSize / bonus);
			sound.playAsSoundEffect(1.0f, 0.5f, false);
		}
		cullable = true;
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		if (!collided) {
			healthUp.draw(x, y, size);
		}
		// g.draw(shape);
	}

	@Override
	public void Collides(Entity... en) {
		if (collided) {
			return;
		}
		for (Entity e : en) {
			if (shape.intersects(e.getBounds())) {
				if (e.GetType().equals("Player")) {
					Player p = (Player) e;
					p.SetScale(bonus);
					affected = p;
					InitialSize = p.GetScale();
					affected.SetScale(bonus);
					collided = true;
					EffectTimeleft = EffectTime;
					sound.playAsSoundEffect(1.0f, 0.5f, false);
				}
				return;
			}
		}
		return;
	}

	@Override
	public Polygon getBounds() {
		throw new UnsupportedOperationException("Not supported yet.");
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
		if (collided) {
			EffectTimeleft--;
			if (EffectTimeleft == 0) {
				Death((byte) 0);
			}
		} else {
			TimeRemaining--;
			if (TimeRemaining == 0) {
				Death((byte) 0);
			}
		}
	}

	@Override
	public boolean Cull() {
		return cullable;
	}

	@Override
	public String GetType() {
		return "HealthUp";
	}

	@Override
	public String GetSuperType() {
		return super.GetType();
	}

	@Override
	public Entity[] GetAllChildren() {
		throw new UnsupportedOperationException("Not supported yet.");
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

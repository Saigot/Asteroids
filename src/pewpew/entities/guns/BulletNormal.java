/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.entities.guns;

import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import pewpew.entities.Bullet;
import pewpew.entities.Entity;

/**
 * 
 * @author michael
 */
public class BulletNormal extends Bullet {
	// <editor-fold defaultstate="collapsed" desc="Variables">
	public float Damage = 15f;
	public Color c = new Color(Color.red);
	public int range;
	public static final int SuggestedCooldown = 10;
	public static final int SuggestedCost = 5;
	public static final Audio NORMAL_SOUND;
	public static final Image NORMAL_BARREL;
	public int culltick = 0;
	public int culltickend = 5;
	static {
		Image i;
		try {
			i = new Image("Res/Ship/StandardBarrel.png");
		} catch (SlickException ex) {
			i = null;
		}
		NORMAL_BARREL = i;
	}
	static {
		Audio a = null;
		try {
			a = AudioLoader.getAudio("OGG", ResourceLoader
					.getResourceAsStream("Res/Sounds/NormalBullet.ogg"));
		} catch (IOException ex) {
		}
		NORMAL_SOUND = a;
	}

	// </editor-fold>

	public BulletNormal(float X, float Y, float Angle, float Additionalspeed,
			int R) {
		super(X, Y, Angle);
		range = R;
		speed = 5f + Additionalspeed;
		xv = (float) (speed * Math.cos(Angle));
		yv = (float) (speed * Math.sin(Angle));
		shape = new Polygon(new float[] { X, Y - 2, X + 10, Y - 1, X + 10,
				Y + 2, X, Y + 2 });
		shape = (Polygon) shape.transform(Transform
				.createRotateTransform(Angle));
		shape.setCenterX(x);
		shape.setCenterY(y);
	}

	@Override
	public void move(Entity e) {
		if (cullable) {
			culltick++;
			return;
		}

		x += xv;
		y += yv;
		range -= Math.sqrt(xv * xv + yv * yv);
		if (range < 0) {
			cullable = true;
		}
		shape.setCenterX(x);
		shape.setCenterY(y);

		// Wrap Screen
		if (x > FORM_WIDTH) {
			x = 0;
		} else if (x < 0) {
			x = FORM_WIDTH;
		}

		if (y > FORM_HEIGHT) {
			y = 0;
		} else if (y < 0) {
			y = FORM_HEIGHT;
		}
	}

	@Override
	public void Special() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void death(byte conditions) {
		cullable = true;
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setColor(c);
		if (cullable) {
			g.fillOval(x - 5, y + 5, (float) (Damage * dmgDealMult * 1.4f),
					(float) (Damage * dmgDealMult * 1.4));
		} else {
			g.fill(getBounds());
		}
	}

	@Override
	public void collides(Entity... en) {
		if (cullable) {
			return;
		}
		for (Entity e : en) {
			if (e == null || e.getBounds() == null || e == this || e.cull())
				continue;
			Polygon p = e.getBounds();
			if (shape.intersects(e.getBounds())
					|| shape.contains(e.getBounds())) {
				takeDamage(e.doDamage());
				e.takeDamage(doDamage());
				continue;
			}
		}
		return;
	}

	@Override
	public float doDamage() {
		if (cullable && culltick != 0) {
			return 0;
		}
		return (int) (Damage * dmgDealMult);
	}

	@Override
	public void takeDamage(float Damage) {
		if (!cullable || culltick == 0) {
			score += doDamage();
			death((byte) 0);
		}
	}

	@Override
	public boolean cull() {
		return cullable && culltick > culltickend;
	}

	@Override
	public String getType() {
		return "Normal";
	}

	@Override
	public Entity[] getAllChildren() {
		return new Entity[] { this };
	}

}

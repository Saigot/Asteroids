/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew;

import java.util.ArrayList;
import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

import pewpew.entities.Enemy;
import pewpew.entities.Entity;
import pewpew.entities.Player;

/**
 * 
 * @author michael
 */
public class Asteroid extends Enemy {

    Asteroid[] a;
    float TOP_SPEED = 5;
    int CollisionCoolDown = 3;
    int Collsiontick = 0;
    Color C = Color.white; // or red
    static boolean KillCounts = false;
    static double SpawnProbablility = 0;

    // parents
    public Asteroid(float X, float Y, Player p) {
        Asteroid.SpawnProbablility = 1.0;
        IntialParalysis = 180;
        Collsiontick = CollisionCoolDown;
        size = (float) Math.random() * 125 + 25;
        health = (int) size;
        // debug fixed size
        // size = 100;
        // choose random number is X or Y is exactly -1
        if (X == -1) {
            X = (float) Math.random() * Entity.FORM_WIDTH;
        }
        if (Y == -1) {
            Y = (float) Math.random() * Entity.FORM_HEIGHT;
        }

        if (p != null) {
            while (Math.sqrt(((X - x) * (X - x)) + ((Y - y) * (Y - y))) < (200 * p.getScale()) + (size)) {
                X = (float) Math.random() * Entity.FORM_WIDTH;
                Y = (float) Math.random() * Entity.FORM_HEIGHT;
            }
        }
        int points = (int) (Math.random() * 27) + 3;
        float poly[] = new float[points * 2];
        float angle = (float) (2 * Math.PI / (points * 2));

        for (int i = 0; i <= points * 2 - 1; i += 2) {
            float pX = (float) (size / 2 * Math.sin(angle * i + (Math.random() * angle)) + Math.random() * 20) + Y;
            float pY = (float) (size / 2 * Math.cos(angle * i + (Math.random() * angle)) + Math.random() * 20) + X;
            poly[i] = pX;
            poly[i + 1] = pY;
        }
        shape = new Polygon(poly);
        x = X;
        y = Y;
        xv = 0;
        yv = 0;
    }

    public Asteroid(float X, float Y, float Size, Player p, int points) {
        health = (int) Size;
        size = Size;
        if (X == -1) {
            X = (float) Math.random() * Entity.FORM_WIDTH;
        }
        if (Y == -1) {
            Y = (float) Math.random() * Entity.FORM_HEIGHT;
        }
        if (p != null) {
            while (Math.sqrt(((X - x) * (X - x)) + ((Y - y) * (Y - y))) < (200 * p.getScale()) + (size)) {
                X = (float) Math.random() * Entity.FORM_WIDTH;
                Y = (float) Math.random() * Entity.FORM_HEIGHT;
            }
        }
        float poly[] = new float[points * 2];
        float angle = (float) (2 * Math.PI / (points * 2));

        for (int i = 0; i <= points * 2 - 1; i += 2) {
            // SIZE/5 FOR SMOOTH
            float pX = (float) (size / 2 * Math.sin(angle * i + (Math.random() * angle)) + Math.random() * (20)) + Y;
            float pY = (float) (size / 2 * Math.cos(angle * i + (Math.random() * angle)) + Math.random() * (20)) + X;
            poly[i] = pX;
            poly[i + 1] = pY;
        }
        shape = new Polygon(poly);
        x = X;
        y = Y;
        shape.setCenterX(x);
        shape.setCenterY(y);
        xv = 0;
        yv = 0;
    }

    // childs
    public Asteroid(float X, float Y, float Size) {
        this(X, Y, Size, new Player(), (int) (Math.random() * 27) + 3);
        // (int)(Math.random()*(10*(size/40))) + 3; FOR SMOOTH
    }

    @Override
    public void Collides(Entity... en) {
        if (en == null) {
            return;
        }
        if (Collsiontick > 0) {
            return;
        } else {
            Collsiontick = CollisionCoolDown;
        }
        for (Entity e : en) {
            if (e == null || e.getBounds() == null || e == this || e.Cull()) {
                continue;
            }
            Polygon p = e.getBounds();
            if (shape.intersects(e.getBounds()) || shape.contains(e.getBounds())) {
                if (e.GetType().equals("Asteroid")) {
                    Asteroid as = (Asteroid) e;
                    // TODO: momentum?
                    as.xv = as.xv * -1;
                    as.yv = as.yv * -1;
                    continue;
                }
                TakeDamage(e.DoDamage());
                e.TakeDamage(DoDamage());
                continue;
            }
            if (a == null) {
                continue;
            }
        }

        return;

    }

    @Override
    public void Death(byte conditions) {
        cullable = true;
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(C);
        if (IntialParalysis > 0) {
            Color Cn = new Color(C.b, C.g, C.r);
            g.setColor(Cn);
        }
        if (health > 0) {
            g.draw(getBounds());
        } else {
            if (a != null) {
                for (int i = 0; i <= a.length - 1; i++) {
                    if (a[i] != null) {
                        a[i].render(gc, g);
                    }
                }
            }
        }
    }

    @Override
    public void Move(Entity e) {
        // do Child collision and movement then return
        if (a != null) {
            for (int i = 0; i <= a.length - 1; i++) {
                if (a[i] != null) {
                    a[i].Move(e);
                }
            }
            for (int i = 0; i <= a.length - 1; i++) {
                for (int j = i + 1; j <= a.length - 1; j++) {
                    if (a[i] != null && a[j] != null) {
                        a[i].Collides(a[j].GetAllChildren());
                    }
                }
            }
            return;
        }
        if (Collsiontick > 0) {
            Collsiontick--;
        }
        tick++;
        if (xv == 0 && yv == 0) {
            float speed = (float) (Math.random() * TOP_SPEED) + (TOP_SPEED * 0.1f);
            float theta = (float) (Math.random() * 2 * Math.PI);
            xv = (float) (speed * Math.cos(theta));
            yv = (float) (speed * Math.sin(theta));
        }
        x += xv;
        y += yv;
        //
        // xv = Util.DoFrictionX(xv, yv, FRICTION);
        // yv = Util.DoFrictionY(xv, yv, FRICTION);

        // Wrap Screen
        if (x > Entity.FORM_WIDTH) {
            x = 0;
        } else if (x < 0) {
            x = Entity.FORM_WIDTH;
        }

        if (y > Entity.FORM_HEIGHT) {
            y = 0;
        } else if (y < 0) {
            y = Entity.FORM_HEIGHT;
        }
        shape.setCenterX(x);
        shape.setCenterY(y);
    }

    @Override
    public float DoDamage() {
        return size / 4;
    }

    @Override
    public void TakeDamage(float Damage) {
        health -= Damage;
        if (health < 0 && a == null) {
            int Asteroids = (int) (Math.random() * 5) + 2;
            a = new Asteroid[Asteroids];
            float newSizes = size / Asteroids;

            if (newSizes >= 5) {
                float speed = (float) Math.sqrt(xv * xv + yv * yv);
                for (int i = 0; i <= Asteroids - 1; i++) {
                    double angle = ((i - 2.5) / 2.5) * Math.PI * 2;
                    float dis = newSizes + (newSizes * 0.1f);
                    float disX = (float) (dis * Math.cos(angle));
                    float disY = (float) (dis * Math.sin(angle));
                    a[i] = new Asteroid(x + disX, y + disY, newSizes);
                    // set speed manually
                    a[i].xv = (float) (speed * Math.cos(angle));
                    a[i].yv = (float) (speed * Math.sin(angle));
                }
            } else {
                Death((byte) 0);
            }
        }
    }

    @Override
    public boolean Cull() {
        if (cullable) {
            return true;
        }
        if (health < 0) {
            boolean cull = true;
            for (int i = 0; i <= a.length - 1; i++) {
                if (!a[i].Cull()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String GetType() {
        return "Asteroid";
    }

    @Override
    public Entity[] GetAllChildren() {
        ArrayList<Entity> b = new ArrayList<>();
        Entity e[];
        if (a == null) {
            e = new Entity[1];
            e[0] = this;
            return e;
        } else {
            for (int i = 0; i <= a.length - 1; i++) {
                if (a[i] != null) {
                    if (!a[i].Cull()) {
                        b.addAll(Arrays.asList(a[i].GetAllChildren()));
                    }
                }
            }
            return b.toArray(new Entity[b.size()]);
        }
    }

    @Override
    public boolean isKilled() {
        return Cull();
    }

    @Override
    public void SetKillCounts(boolean KC) {
        Asteroid.KillCounts = KC;
    }

    @Override
    public boolean GetKillCounts() {
        return Asteroid.KillCounts;
    }

    @Override
    public void SetSpawnProb(double SP) {
        Asteroid.SpawnProbablility = SP;
    }

    @Override
    public double GetSpawnProb() {
        return Asteroid.SpawnProbablility;
    }

    @Override
    public Polygon getBounds() {
        if (health > 0) {
            return shape;
        } else {
            return new Polygon();
        }
    }
}

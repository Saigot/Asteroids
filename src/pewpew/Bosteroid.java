/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import pewpew.entities.Bullet;
import pewpew.entities.Entity;
import pewpew.entities.Player;

/**
 * 
 * @author michael
 */
public class Bosteroid extends Asteroid {
    static int SIZE;
    int stage;
    Player p;

    public Bosteroid(Player P) {
        super(Entity.FORM_WIDTH / 2, 0, 500, P, 50);
        p = P;
        health = 10000;
        TOP_SPEED = 0.5f;
        C = org.newdawn.slick.Color.red;
        Bosteroid.SIZE = 600;
        stage = 0;
        shape.setCenterX(x);
        shape.setCenterY(y);
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        // g.draw(getBounds());
        float centerx = shape.getMinX() + shape.getWidth() / 2;
        float centery = shape.getMinY() + shape.getHeight() / 2;
        g.setColor(C);
        g.drawString(Integer.toString(health), x - 50, y - 10);
        g.draw(shape);

        // Shape small;
        // for(int i = 0; i <= SIZE/6; i++){
        // small = shape.transform(Transform.createScaleTransform((1 -
        // (i*0.01f)),(1 - (i*0.01f))));
        // small.setCenterX(shape.getX());
        // small.setCenterY(shape.getY());
        // g.draw(small);
        // }
        g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        g.fillOval(x - Bosteroid.SIZE * 0.045f, y - Bosteroid.SIZE * 0.045f, Bosteroid.SIZE * 0.09f, Bosteroid.SIZE * 0.09f);
    }

    @Override
    public String getType() {
        return "Bosteroid";
    }

    @Override
    public void move(Entity e) {
        // do Child collision and movement then return

        if (Collsiontick > 0) {
            Collsiontick--;
        }
        tick++;
        float deltax = x - p.x;
        float deltay = y - p.y;
        float speed = TOP_SPEED;
        float theta = (float) (Math.atan(deltay / deltax) + Math.PI);
        xv = (float) ((speed * Math.cos(theta)));
        yv = (float) ((speed * Math.sin(theta)));

        if (deltax < 0) {
            xv = -xv;
            yv = -yv;
        }
        x += xv;
        y += yv;

        //
        // xv = Util.DoFrictionX(xv, yv, FRICTION);
        // yv = Util.DoFrictionY(xv, yv, FRICTION);

        shape = (Polygon) shape.transform(Transform.createTranslateTransform(xv, yv));
    }

    @Override
    public void collides(Entity... en) {

        if (en == null || getBounds() == null) {
            return;
        }

        for (Entity e : en) {
            if (e == null) {
                continue;
            }
            if (en.length > 2) {
                // System.out.println();
            }

            Polygon p = e.getBounds();
            if (e == null || p == null || e == this || e.cull() || getBounds() == null) {
                continue;
            }
            e.collides(this);
            if (getBounds().intersects(p) || getBounds().contains(p)) {
                if (e.getType().equals("Asteroid")) {
                    Asteroid as = (Asteroid) e;
                    // TODO: momentum?
                    as.xv = as.xv * -1;
                    as.yv = as.yv * -1;
                    return;
                } else if (e.getSuperType().equals("Bullet")) {
                    Bullet b = (Bullet) e;
                    Asteroid as = new Asteroid(b.x, b.y, b.doDamage() * 1.4f, null, 5);
                    Shape temp[] = shape.subtract(as.getBounds());
                    if (temp.length == 1) {
                        shape = (Polygon) GetLargestShape(temp);
                    }
                }
                takeDamage(e.doDamage());
                e.takeDamage(doDamage());
                return;
            }
        }

        return;

    }

    public int GetClosestPoint(float x, float y) {
        float[] shapept = shape.getPoints();
        double closest = Entity.FORM_WIDTH;
        int best = 0;
        for (int i = 0; i <= shapept.length - 2; i += 2) {
            double dis = Math.sqrt(Math.pow(shapept[i], 2) + Math.pow(shapept[i + 1], 2));
            if (dis < closest) {
                closest = dis;
                best = i;
            }
        }
        return best;
    }

    public Shape GetLargestShape(Shape... a) {
        Shape result = null;

        for (Shape s : a) {
            if (s == null) {
                continue;
            }
            if (s.contains(x, y)) {
                return s;
            }
            if (result == null || s.getBoundingCircleRadius() > result.getBoundingCircleRadius()) {
                result = s;
            }
        }

        return result;
    }

    @Override
    public void takeDamage(float dmg) {
        return;
    }

    @Override
    public Polygon getBounds() {
        return shape;
    }

}

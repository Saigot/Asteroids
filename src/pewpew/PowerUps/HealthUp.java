/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.PowerUps;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import pewpew.Entity;
import pewpew.Player;

/**
 *
 * @author michael
 */
public class HealthUp extends PowerUps {
    //<editor-fold defaultstate="collapsed" desc="Variables">
    static final Image HEALTH_IMAGE;
    int bonus;
    public static double SpawnProbibility = 0.0;
    static{
        Image i = null;
        try {
            i = new Image("Res/PowerUps/health.png");
        } catch (SlickException ex) {
        }
        HEALTH_IMAGE = i;
    }
    //</editor-fold>
    
    public HealthUp(){
        this((float) (Math.random() * FORM_WIDTH),(float) (Math.random() * FORM_HEIGHT),
                (float) (Math.random() * 0.5 + 0.5));
    }
    public HealthUp(float X, float Y) {
        this(X,Y,(float) (Math.random() * 0.5 + 0.5));
    }
    public HealthUp(float X, float Y, float SIZE) {
        timeout = (int) (Math.random() * 500) + 500;
        TimeRemaining = timeout;
        x = X;
        y = Y;
        size = SIZE;
        bonus = (int) (Math.random() * 25 + 50);
        shape = new Polygon(new float[]{
                    x, y, x + 50 * size, y, x + 50 * size, y + 50 * size, x, y + 50 * size
                });
    }
    
    @Override
    public void Death(byte conditions) {
        cullable = true;
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        HEALTH_IMAGE.draw(x, y, size);
     //   g.draw(shape);
    }

    @Override
    public Entity Collides(Entity... en) {
        for (Entity e : en) {
            if (shape.intersects(e.getBounds())) {
                if (e.GetType().equals("Player")) {
                    Player p = (Player) e;
                    if(p.health + bonus > p.MAX_HEALTH){
                        p.health = p.MAX_HEALTH;
                    } else {
                        p.health += bonus;
                    }
                    Death((byte) 0);
                }
                return this;
            }
        }
        return null;
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
        TimeRemaining--;
        if (TimeRemaining <= 0) {
            Death((byte) 0);
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
    public Entity[] GetAllChildren() {
        return null;
    }

    @Override
    public String GetSuperType() {
        return super.GetType();
    }
    
    public void SetSpawnProb(Double Prob){
        SpawnProbibility = Prob;
    }
    public double GetSpawnProb(){
        return SpawnProbibility;
    }
}

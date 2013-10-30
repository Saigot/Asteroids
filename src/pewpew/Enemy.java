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
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Polygon;

/**
 *
 * @author michael
 */
abstract public class Enemy implements Entity {

    int IntialParalysis;
    float size;
    short tick = 0;
    float x;
    float y;
    float xv;
    float yv;
    float xa;
    float ya;
    int health;
    int MaxHealth;
    float maxSpeed;
    Polygon shape;
    Image form;
    boolean cullable = false;
    
    public float GetX(){
        return x;
    }
    public float GetY(){
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

    public int GetHealth(){
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


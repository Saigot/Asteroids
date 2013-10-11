/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew;

import java.net.URL;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

/**
 *
 * @author michael
 */
abstract public class PowerUps implements Entity{
    float x;
    float y;
    
    float xv;
    float yv;
    
    float xa;
    float ya;
    
    float size;
    
    int EffectTime;
    int EffectTimeleft;
    int timeout;
    int TimeRemaining;
    
    
    Polygon shape;
    boolean cullable = false;
    boolean Collided;
    @Override
    public String GetType() {
        return "PowerUps";
    }
    
    public abstract void SetSpawnProb(Double Prob);
    public abstract double GetSpawnProb();
}



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.Guns;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import pewpew.Entity;

/**
 *
 * @author michael
 */
public abstract class Bullet implements Entity{
    float x;
    float y;
    
    float xv;
    float yv;
    float speed;
    float angle;
    
    public static int cost;
    public static int CoolDown;
    
    boolean cullable = false;
    
    Polygon shape;
    int score;
    public static Audio FireSound;
    
    Bullet(float X, float Y, float Angle){
       x = X;
       y = Y;
       angle = Angle;
    }
    
    public int GetScore(){
        return score;
    }
    public abstract void Special();
     @Override
    public Polygon getBounds() {
        return shape;
    }
     @Override
    public String GetSuperType() {
        return "Bullet";
    }
}


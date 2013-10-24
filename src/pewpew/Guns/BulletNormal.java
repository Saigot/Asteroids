/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.Guns;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.*;
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
public class BulletNormal extends Bullet{
    //<editor-fold defaultstate="collapsed" desc="Variables">
    float Damage = 15f;
    Color c = new Color(Color.red);
    int range;
    public static final int SuggestedCooldown = 10;
    public static final int SuggestedCost = 5;
    public static final Audio NORMAL_SOUND;
    public static final Image NORMAL_BARREL;
    static{
        Image i;
        try {
            i = new Image("Res/Ship/StandardBarrel.png");
        } catch (SlickException ex) {
            i = null;
        }
        NORMAL_BARREL = i;
    }
    static{
        Audio a = null;
        try {
            a = AudioLoader.getAudio("OGG",
                    ResourceLoader.getResourceAsStream("Res/Sounds/NormalBullet.ogg"));
        } catch (IOException ex) {
        }
        NORMAL_SOUND = a;
    }
    //</editor-fold>
    
    public BulletNormal(float X, float Y, float Angle, float Additionalspeed, int R){
        super(X,Y, Angle);
        range = R;
        speed = 5f + Additionalspeed;
        xv = (float)(speed*Math.cos(Angle));
        yv = (float)(speed*Math.sin(Angle));
        shape = new Polygon(new float[]{
            X,Y-2,
            X+10,Y-1,
            X+10,Y+2,
            X,Y+2
        });
        shape = (Polygon)shape.transform(Transform.createRotateTransform(Angle));
    }
    
    @Override
    public void Move(Entity e) {
        x += xv;
        y += yv;
        range-= Math.sqrt(xv*xv + yv*yv);
        if(range < 0){
            cullable = true;
        }
        shape.setCenterX(x);
        shape.setCenterY(y);
        
        //Wrap Screen
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
    public void Death(byte conditions) {
        cullable = true;
    }
    
    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(c);
        g.fill(shape);
    }

     @Override
    public Entity Collides(Entity... en) {
         for (Entity e : en) {
             if(e == this || e.Cull()) continue;
             Polygon p = e.getBounds();
             if (shape.intersects(e.getBounds())) {
                 TakeDamage(e.DoDamage());
                 e.TakeDamage(DoDamage());
                 return this;
             }
         }
        return null;
    }

    @Override
    public float DoDamage() {
        return (int)(Damage * dmgDealMult);
    }

    @Override
    public void TakeDamage(float Damage) {
        score += DoDamage();
        Death((byte)0);
    }

    @Override
    public boolean Cull() {
        return cullable;
    }

    @Override
    public String GetType() {
        return "Normal";
    }

    @Override
    public Entity[] GetAllChildren() {
        return new Entity[]{this};
    }

    
}

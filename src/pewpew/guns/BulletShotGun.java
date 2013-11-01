/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.guns;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import pewpew.Entity;

/**
 *
 * @author michael
 */
public class BulletShotGun extends Bullet {

    //<editor-fold defaultstate="collapsed" desc="Variables">
    BulletNormal b[];
    public static int SuggestedCooldown = 40;
    public static final int SuggestedCost = 50;
    
    public static final Audio SHOTGUN_SOUND;
    public static final Image SHOTGUN_BARREL;
    static{
        Image i = null;
        try {
            i = new Image("Res/Ship/ShotGunBarrel.png");
        } catch (SlickException ex) {
        }
        SHOTGUN_BARREL = i;
    }
    static{
        Audio a = null;
        try {
            a = AudioLoader.getAudio("OGG",
                    ResourceLoader.getResourceAsStream("Res/Sounds/ShotGun.ogg"));
        } catch (IOException ex) {
        }
        SHOTGUN_SOUND = a;
    }
    //</editor-fold>
    public BulletShotGun(float X, float Y, float Angle) {
        super(X, Y, Angle);
        /**
         * ShotGun uses an array of Norm bullets of variable speed and angle
         * with reduced damage to simulate a shotgun, very random
         */
        
        float bullets = (float) (Math.random() * 18) + 2;
        b = new BulletNormal[(int) bullets];
        double MaxAngleError = (Math.random() * Math.PI *5/12) + Math.PI / 12;
        for (int i = 0; i <= bullets - 1; i++) {
            float speedIncrease = (float) (Math.random() * 11) - 4;
            float AngleError = (float) ((Math.random() * (MaxAngleError)) - (MaxAngleError / 2));
            b[i] = new BulletNormal(X, Y, Angle - AngleError, speedIncrease,Entity.FORM_WIDTH);
            //b[i].Damage += (float) (Math.random() * 10) + 10;
            b[i].c = new Color(
                    (int) (((speedIncrease + 4) / 15) * 255),
                    0, (int) (255 - ((speedIncrease + 4) / 15) * 255));
//with number of bullets as green            
//            b[i].c = new Color(
//                    (int)(((speedIncrease+4) / 15)*255),
//                    (int)(((bullets+2)/17)*255),(int)(255 - ((speedIncrease+4) / 15)*255));

        }
    }

    @Override
    public void Special() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void Death(byte conditions) {
        for(Bullet B : b){
            score += B.score;
        }
        cullable = true;
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        for (Bullet B : b) {
            if (B == null) {
                continue;
            }
            if (B.Cull()) {
                continue;
            }
            B.render(gc, g);
        }
    }

    @Override
    public void Collides(Entity... en) {
        for (Bullet B : b) {
            if (B == null || B.getBounds() == null || B == null || B.Cull()) {
                continue;
            }
            for (Entity e : en) {
                if(e == B) continue;
                Polygon p = e.getBounds();
                if (B.shape.intersects(e.getBounds())) {
                    B.TakeDamage(e.DoDamage());
                    e.TakeDamage(B.DoDamage());
                    return;
                }
            }
        }
        return;
    }

    @Override
    public float DoDamage() {
        return (int)(10 * dmgDealMult);
    }

    @Override
    public void TakeDamage(float Damage) {
        score += DoDamage();
    }

    @Override
    public void Move(Entity e) {
        for (Bullet B : b) {
            if (B == null) {
                continue;
            }
            if (B.Cull()) {
                continue;
            }
            B.Move(e);
        }
    }

    @Override
    public boolean Cull() {
        if (cullable) {
            return true;
        }
        boolean c = true;
        for (Bullet B : b) {
            if (B == null) {
                continue;
            }
            if (!B.Cull()) {
                c = false;
            }
        }
        cullable = c;
        return c;
    }

    @Override
    public String GetType() {
        return "ShotGun";
    }

    @Override
    public Entity[] GetAllChildren() {
        return b;
    }
}

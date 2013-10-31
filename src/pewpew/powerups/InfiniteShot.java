/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.powerups;

import pewpew.guns.Bullet;
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
import pewpew.Entity;
import pewpew.Player;

/**
 *
 * @author michael
 */
public class InfiniteShot extends PowerUps{
    //<editor-fold defaultstate="collapsed" desc="Variables">
    static final Image INFINTY_IMAGE;
    static final Audio INFINITY_SOUND;
    static {
        Image i;
        try {
            i = new Image("Res/PowerUps/InfiniteAmmo.png");
        } catch (SlickException e) {
            i = null;
        }
        INFINTY_IMAGE = i;
        Audio a;
        try {
            a = AudioLoader.getAudio("OGG",
                    ResourceLoader.getResourceAsStream("Res/Sounds/InfiniteAmmo.ogg"));
        } catch (IOException ex) {
            a = null;
        }
        INFINITY_SOUND = a;
    }
    Player affected;
    public static double SpawnProbibility = 0.0;
    //</editor-fold>
    
    public InfiniteShot() throws SlickException{
        this((float) (Math.random() * FORM_WIDTH),
                (float) (Math.random() * FORM_HEIGHT),(float) (Math.random() * 0.5 + 0.5));
    }
    public InfiniteShot(float X, float Y) throws SlickException{
        this(X,Y,(float) (Math.random() * 0.5 + 0.5));
    }
    public InfiniteShot(float X, float Y, float SIZE) throws SlickException{
        EffectTime = 600;
        timeout = (int) (Math.random() * 500) + 500;
        TimeRemaining = timeout;
        x = X;
        y = Y;
        size = SIZE;
        shape = new Polygon(new float[]{
                    x, y, x + 50 * size, y, x + 50 * size, y + 50 * size, x, y + 50 * size
                });
    }
    @Override
    public void Death(byte conditions) {
        cullable = true;
        if (Collided) {
            affected.ChangeBulletType(affected.GetAmmoType());
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        if(!Collided){
            INFINTY_IMAGE.draw(x, y, size);
        }
    }

    @Override
    public Entity Collides(Entity... en) {
        for (Entity e : en) {
            if (shape.intersects(e.getBounds()) && !Collided) {
                if (e.GetType().equals("Player")) {
                    Player p = (Player) e;
                    Collided = true;
                    affected = p;
                    EffectTimeleft = EffectTime;
                    InfiniteShot.INFINITY_SOUND.playAsSoundEffect(1.0f, 1.0f, false);
                }
                return this;
            }
        }
        return null;
    }

    @Override
    public Polygon getBounds() {
        return shape;
    }

    @Override
    public float DoDamage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void TakeDamage(float Damage) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void Move(Entity e) {
        if (Collided) {
            byte b = affected.GetAmmoType();
            switch(b){
                case 0:
                case 1:
                case 2:
                    Bullet.CoolDown = 1;
                    Bullet.cost = 1;
                    break;
                case 3:
                    Bullet.CoolDown = 10;
                    Bullet.cost = 1;
                    break; 
                case 4:
                    Bullet.CoolDown = 0;
                    Bullet.cost = 1;
                    break;
            }
            
            EffectTimeleft--;
            if(EffectTimeleft < 0){
                Death((byte)0);
            }else{
                Bullet.FireSound = null;
            }
        }else{
            TimeRemaining--;
            if(TimeRemaining == 0){
                Death((byte)0);
            }
        }
    }

    @Override
    public boolean Cull() {
        return cullable;
    }

    @Override
    public String GetType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Entity[] GetAllChildren() {
        throw new UnsupportedOperationException("Not supported yet.");
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

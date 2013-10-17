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
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import pewpew.Entity;

/**
 *
 * @author michael
 */
public class BulletBomb extends Bullet{
    //<editor-fold defaultstate="collapsed" desc="Variables">
    float Radius;
    float INITIAL_SIZE = 10f;
    float FuseRemaining;
    boolean Exploded = false;
    boolean Shrinking = false;
    float MaxRadius;
    public static boolean previousStickState;
    public static int SuggestedCost = 150;
    public static int SuggestedCooldown = 120;
    float growthrate = 1f;
    float shrinkrate = 5f;
    Color c = Color.cyan;
    public static final Image BOMB_BARREL;
    static{
        Image i = null;
        try {
            i = new Image("Res/Ship/BombBarrel.png");
        } catch (SlickException ex) {
        }
        BOMB_BARREL = i;
    }
    public static final Audio BOMB_SOUND;
    static{
        Audio a = null;
        try {
            a = AudioLoader.getAudio("OGG",
                    ResourceLoader.getResourceAsStream("Res/Sounds/BombDrop.ogg"));
        } catch (IOException ex) {
        }
        BOMB_SOUND = a;
    }
    
    public static final Audio BOOM;
    static{
        Audio a = null;
        try {
            a = AudioLoader.getAudio("OGG",
                    ResourceLoader.getResourceAsStream("Res/Sounds/Boom.ogg"));
        } catch (IOException ex) {
        }
        BOOM = a;
    }
    //</editor-fold>
    
    public BulletBomb(float X, float Y, float Timer){
        this(X,Y,(float)(((Timer+Math.PI/2)/(Math.PI*2))*240f) + 60f
                ,(float)(Math.random() * 250)+250f);
    }
    
    public BulletBomb(float X, float Y, float Timer, float radius){
        super(X,Y,Timer);
        MaxRadius = radius;
        FuseRemaining = Timer;
        growthrate = Math.abs(((1-((Timer-60)/240f))*5) + 1f);
        shrinkrate = Math.abs(((1-((Timer-60)/240f))*5) + 5f);
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
        if(!Exploded){
            g.drawOval(x, y, 5f,5f);
        }else{
            g.draw(shape);
        }
    }

    @Override
    public Entity Collides(Entity... en) {
        if(!Exploded){
            return null;
        }
        for (Entity e : en) {
             if(e == this || e.Cull()) continue;
             Polygon p = e.getBounds();
             if (shape.intersects(e.getBounds())) {
                 TakeDamage(e.DoDamage());
                 e.TakeDamage(DoDamage());
                 return e;
             }
         }
       return null;
    }

    @Override
    public float DoDamage() {
        return (int)(10f * dmgDealMult);
    }

    @Override
    public void TakeDamage(float Damage) {
        score += DoDamage();
    }

    @Override
    public void Move(Entity e) {
        if (!Exploded) {
            FuseRemaining--;
            if(FuseRemaining < 0){
                Exploded = true;
            }
        } 
        if (Exploded && !Shrinking) {
            Radius+=growthrate;
            BOOM.playAsSoundEffect(0.2f + (float)(Radius * 0.001),
                    0.1f+ (float)(Radius * 0.005), false);
            if(Radius > MaxRadius){
                Shrinking = true;
            }
        }else if(Exploded){
            Radius-=shrinkrate;
        }
        int points = (int) (Math.random() * 27) + 3;
        float poly[] = new float[points * 2];
        float ANGLE = (float) (2 * Math.PI / (points));

        for (int i = 0; i <= points * 2 - 1; i += 2) {
            float pX = (float) (Radius / 2 * Math.sin(ANGLE * i + (Math.random() * ANGLE))
                    + Math.random() * 20) + x;
            float pY = (float) (Radius / 2 * Math.cos(ANGLE * i + (Math.random() * ANGLE))
                    + Math.random() * 20) + y;
            poly[i] = pX;
            poly[i + 1] = pY;
        }
        shape = new Polygon(poly);
        if (Radius < 0 && Shrinking) {
            Death((byte) 0);
        }
    }

    @Override
    public boolean Cull() {
       return cullable;
    }

    @Override
    public String GetType() {
        return "BulletBomb";
    }

    @Override
    public Entity[] GetAllChildren() {
        Entity[] e = {this};
        return e;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.guns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
public class BulletBounce extends Bullet{
    //<editor-fold defaultstate="collapsed" desc="Variables">
    ArrayList<BulletBounce> b = new ArrayList<>();
    Color c=new Color(Color.green);;
    int recursion;
    int Damageless;
    float damage = 30f;
    public static final int SuggestedCooldown = 15;
    public static final int SuggestedCost = 30;
    boolean dead = false;
    
    public static final Image BOUNCE_BARREL;
    static{
        Image i = null;
        try {
            i = new Image("Res/Ship/BounceBarrel.png");
        } catch (SlickException ex) {
        }
        BOUNCE_BARREL = i;
    }
    
    public static final Audio BOUNCE_SOUND;
    static{
        Audio a = null;
        try {
            a = AudioLoader.getAudio("OGG",
                        ResourceLoader.getResourceAsStream("Res/Sounds/ShotGun.ogg"));
        } catch (IOException ex) {
        }
        BOUNCE_SOUND = a;
    }
    //</editor-fold>
    public BulletBounce(float X, float Y, float Angle, float Additionalspeed, int Recursion){
        super(X,Y,Angle);
        c = new Color(Color.green);
        if (Recursion == 0) {
            Damageless = 0;
        } else {
            Damageless = 10;
        }
        speed = 7f + Additionalspeed;
        xv = (float)(speed*Math.cos(Angle));
        yv = (float)(speed*Math.sin(Angle));
        shape = new Polygon(new float[]{
            X,Y-2,
            X+10,Y-2,
            X+10,Y+2,
            X,Y+2
        });
        shape = (Polygon)shape.transform(Transform.createRotateTransform(Angle));
        recursion = Recursion;
//        int r = (int)(Math.random()*255);
//        int y = (int)(Math.random()*255);
//        int b = (int)(Math.random()*255);
//        c = new Color((r+y)/2,y,b);
        
    }
    @Override
    public void Special() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void Death(byte conditions) {
        dead = true;
        cullable = true;
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(c);
        if (!dead) {
            g.fill(shape);
        }
        for(BulletBounce e : b){
            e.render(gc, g);
        }
    }

    @Override
    public Entity Collides(Entity... en) {
        for (BulletBounce e : b) {
            e.Collides(en);
        }
        if(dead){
            return null;
        }
        for (Entity e : en) {
            if (e == this || e.Cull()) {
                continue;
            }
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
        float d= (int)( (damage / ((3*recursion+1f)/2)) * dmgDealMult);
        if(d < 1){
            d = 1;
        }
        return d;
    }

    @Override
    public void TakeDamage(float Damage) {
        if(Damageless > 0){
            return;
        }
        score += DoDamage()/4f;
        //spawn new guys randomly in the 180 degree space behind the bullet
        float minAngle = (float)(angle + Math.PI);
        float maxAngle = (float)(angle - Math.PI);
        
        int newBullets = (int)(Math.random()*3)+2;
        for(int i = 0; i <= newBullets; i++){
            b.add(new BulletBounce(x, y, (float)(Math.random()*(maxAngle-minAngle)+minAngle),
                    speed+1, recursion+1));
            b.get(b.size()-1).Move(this);
        }
        Death((byte)0);
    }

    @Override
    public void Move(Entity e) {
        if (!dead) {
            x += xv;
            y += yv;
            shape.setCenterX(x);
            shape.setCenterY(y);
        }
        Damageless--;
        //cull if neccesary
        if(x >= FORM_WIDTH || x <= 0 || y >= FORM_HEIGHT || y <= 0){
            Death((byte)0);
        }
        for(BulletBounce en : b){
            en.Move(e);
        }
        //cull
        for(int i = 0; i <= b.size()-1; i++){
            if(b.get(i).Cull()){
                score += b.get(i).score;
                b.remove(i);
            }
        }
    }

    @Override
    public boolean Cull() {
        boolean CullMe = cullable;
        for(BulletBounce en : b){
            if(!en.Cull()){
               CullMe = false; 
            }
        }
        return CullMe;
    }

    @Override
    public String GetType() {
        return "Bonunce";
    }

    @Override
    public Entity[] GetAllChildren() {
        ArrayList<Entity> t = new ArrayList<>();
        for(BulletBounce en : b){
            t.addAll(Arrays.asList(en.GetAllChildren()));
        }
        t.add(this);
        return t.toArray(new Entity[t.size()]);
    }
    
}

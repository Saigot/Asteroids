  /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.entities;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import pewpew.Util;

/**
 * 
 * @author michael
 */
public class Bosteroid extends Asteroid {
    static int SIZE;
    static boolean spawn = false;
    int stage;
    Player player;
    int shotTick = 0;
    int shotTime = 300; //5  sec initial
    float startRad;
    
    final float FRICTION = 0.03f;
    ArrayList<Asteroid> shots = new ArrayList<>();

    public Bosteroid(Player P) {
        super(Entity.FORM_WIDTH / 2, 0, 500, P, 50);
        CollisionCoolDown = 200; //Only used for collision with player
        Collsiontick = 0; 
        player = P;
        health = 10000;
        TOP_SPEED = 5f;
        C = Color.red;
        Bosteroid.SIZE = 600;
        stage = 0;
        shape.setCenterX(x);
        shape.setCenterY(y);
        startRad = MaxRadius();
        
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        // g.draw(getBounds());
        float centerx = shape.getMinX() + (shape.getWidth() / 2);
        float centery = shape.getMinY() + (shape.getHeight() / 2);
        g.setColor(C);
        //g.drawString(Integer.toString(health), x - 50, y - 10);
        g.fill(shape);
        //float radius = 2*MaxRadius();
        //g.drawOval((x-radius/2),(y-radius/2), radius, radius);

//         Shape small;
//         for (int i = 0; i <= SIZE / 6; i++) {
//            small = shape.transform(Transform.createScaleTransform((1
//                    - (i * 0.01f)), (1 - (i * 0.01f))));
//            small.setCenterX(centerx);
//            small.setCenterY(centery);
//            g.draw(small);
//        }
        g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        g.fillOval(x - Bosteroid.SIZE * 0.045f, y - Bosteroid.SIZE * 0.045f
                , Bosteroid.SIZE * 0.09f, Bosteroid.SIZE * 0.09f);
    }

    @Override
    public String getType() {
        return "Bosteroid";
    }
    
    @Override
    public double GetSpawnProb() {
        if(spawn){
            return 1;
        }
        return 0;
    }
    
    @Override
    public void SetSpawnProb(double SP){
       //always spawns or never spawns
        if(SP > 0.5){
            spawn = true;
        }else{
            spawn = false;
        }
    }
            
    @Override
    public void move(Entity e) {
        // do Child collision and movement then return

        if (Collsiontick > 0) {
            Collsiontick--;
        }
        tick++;
        float deltax = x - player.x;
        float deltay = y - player.y;
        float speed = getSpeed();
        if(Math.abs(speed) < 0.1 && Collsiontick/50<=1){
            float theta = (float) (Math.atan(deltay / deltax) + Math.PI);
            xv = (float) ((TOP_SPEED * Math.cos(theta)));
            yv = (float) ((TOP_SPEED * Math.sin(theta)));
            if (deltax < 0) {
                xv = -xv;
                yv = -yv;
            }
        }
        
        xv = Util.doFrictionX(xv, yv, FRICTION);
        yv = Util.doFrictionY(xv, yv, FRICTION);
         
         
        x += xv;
        y += yv;

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
                }else if (e.getType().equals("Player")) {
                    if (Collsiontick <= 0) {
                        xv = -xv;
                        yv = -yv;
                        player.Bounce(xv, yv);
                        Collsiontick = CollisionCoolDown;
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

    public float getSpeed(){
        return (float) Math.sqrt(xv*xv + yv*yv);
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
        if(MaxRadius() < 0.2*startRad){
            stage++;
        }
        
        if(shape.getPointCount() > 100){
            SmoothShape();
        }
        
    }
    
    @Override
    public float doDamage(){
        return 10;
    }
    @Override
    public Polygon getBounds() {
        return shape;
    }
    
    public float MaxRadius(){
        float[] pts = shape.getPoints();
        float maxradius = 0;
        float centerx = shape.getMinX() + (shape.getWidth() / 2);
        float centery = shape.getMinY() + (shape.getHeight() / 2);
        for(int i = 0; i < pts.length-2; i+=2){
            float rad = (centerx-pts[i])*(centerx-pts[i]) + (centery-pts[i+1])*(centery-pts[i+1]);
            if(rad>maxradius){
                maxradius = rad;
            }
        }
        
        return (float) Math.sqrt(maxradius);
    }
    
    public void SmoothShape(){
        int THRESHOLD = 20;
        float[] pts = shape.getPoints();
        boolean[] keep = new boolean[shape.getPointCount()];
        int count = 0;
        
        for(int i = 0; i <= pts.length-2; i+=2){
            float distance;
            keep[i/2] = true;
            if(i+3 <= pts.length-1){
                distance = ((pts[i]-pts[i+2])*(pts[i]-pts[i+2]) 
                        + (pts[i+1]-pts[i+3])*(pts[i+1]-pts[i+3]));
                if(distance < THRESHOLD){
                    keep[i/2] = false;
                }
            }
            
            if(i-2 >= 0){
                distance = ((pts[i]-pts[i-2])*(pts[i]-pts[i-2]) 
                        + (pts[i+1]-pts[i-1])*(pts[i+1]-pts[i-1]));
                if(distance < THRESHOLD){
                   keep[i/2] = false;
                }
            }
            if(keep[i/2]){
                count++;
            }
        }
        float newPts[] = new float[count*2];
        int point = 0;
        for(int i = 0; i <= pts.length-2; i+=2){
            if(keep[i/2]){
                newPts[point] = pts[i];
                newPts[point+1] = pts[i+1];
                point+=2;
            }
        }
        
        shape = new Polygon(newPts);
    }

}

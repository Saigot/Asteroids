/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.entities;

import java.util.ArrayList;
import java.util.Arrays;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Transform;

import pewpew.Util;
import pewpew.entities.guns.BulletBomb;
import pewpew.entities.guns.BulletBounce;
import pewpew.entities.guns.BulletMissle;
import pewpew.entities.guns.BulletNormal;
import pewpew.entities.guns.BulletShotGun;

/**
 *
 * @author michael
 */
public class Player implements Entity {

    // <editor-fold defaultstate="collapsed" desc="Variables">
    public float x = Entity.FORM_WIDTH / 2;
    public float y = Entity.FORM_HEIGHT / 2;
    public float xv;
    public float yv;
    public float xa;
    public float ya;
    public float friction = 0.02f;
    public int damageCoolDown = 30;
    public int damagetick = 0;
    public int MAX_HEALTH = 100;
    public int health = MAX_HEALTH;
    public float MaxSpeed = 10;
    public float HEIGHT = 50;
    public float WIDTH = 50;
    public Polygon shape;
    private float scale = 1f;
    public Image Ship;
    public Image Flame;
    public Image ReverseFlame;
    public Image Barrel = null;
    public ArrayList<Bullet> b = new ArrayList<>();
    public float BulletAngle = (float) -Math.PI / 2; // RADIANS
    private byte ammoType = 0;
    public int shotRate;
    public int shotCoolDown = 0;
    private boolean fixedGun = true;
    private float rotation = 0;
    public boolean isReverse = false;
    public boolean isForward = false;
    public int tick = 0;
    public int score = 100;
    public static final byte GUN_TYPES = 5;
    public static double dmgRecieveMult = 1;
    public static double priceMult = 1;

    // </editor-fold>
    public Player(Boolean main) {
        shape = new Polygon(
                new float[]{x + 25, y, x, y + 50, x + 50, y + 50});
        try {
            Ship = new Image(("Res/Ship/Ship.png"));
            Flame = new Image("Res/Ship/ShipFlame.png");
            ReverseFlame = new Image("Res/Ship/ShipReverse.png");
        } catch (Exception ex) {
            Ship = null;
            return;
        }

        setScale(1);
        if(main){
        changeBulletType(ammoType);}
        
        x -= Ship.getWidth() / 2;
        y -= Ship.getHeight() / 2;

    }

    // <editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public float getBulletAngle() {
        return BulletAngle;
    }

    public void setFixedGun(boolean FixedGun) {
        if (ammoType != 3) {
            fixedGun = FixedGun;
        }
    }

    public boolean getFixedGun() {
        return fixedGun;
    }

    public void toggleFixedGun() {
        if (ammoType != 3) {
            fixedGun = !fixedGun;
        }
    }

    public float getRotation() {
        return rotation;
    } // IN DEGREES

    public float getSpeed() {
        return (float) Math.sqrt(xv * xv + yv * yv);
    }

    public void setScale(float i) {
        scale = i;
        float shapeScale = 1 / (shape.getWidth() / (50 * i));
        shape = (Polygon) shape.transform(Transform.createScaleTransform(
                shapeScale, shapeScale));
    }

    public float getScale() {
        return scale;
    }

    @Override
    public Polygon getBounds() {
        return shape;
    }

    public float getRotatedFirePointX() {
        float yi = getFirePointY();

        float dy = y - yi;

        float theta = (float) Math.toRadians(getRotation() - 90);

        return (float) -(dy * Math.cos(theta)) + (x + (WIDTH * scale / 2));
    }

    public float getRotatedFirePointY() {
        float yi = getFirePointY();

        float dy = y - yi;

        float theta = (float) Math.toRadians(getRotation() - 90);

        return (float) -(dy * Math.sin(theta)) + (y + (HEIGHT * scale / 2));
    }

    private float getFirePointX() {
        float a;
        a = x + (WIDTH * scale / 2);
        return a;
    }

    private float getFirePointY() {
        float a;
        a = y + (HEIGHT * scale / 2) - (15 * scale);
        return a;
    }

    public byte getAmmoType() {
        return ammoType;
    }

    // </editor-fold>
    public void rotateGun(float rad) {
        if (ammoType == 3) {
            if (BulletAngle + rad > Math.PI * 3 / 2) {
                BulletAngle = (float) Math.PI * 3 / 2;
            }
            // if(BulletAngle + rad < Math.PI*3/2 && BulletAngle > Math.PI*3/2
            // ){
            // BulletAngle = 0;
            // }
        }
        BulletAngle += rad;
    }

    public void changeBulletType(byte type) {
        if (ammoType == 3) {
            fixedGun = BulletBomb.previousStickState;
        }
        ammoType = type;
        switch (type) {
            case 0: // normal bullet
                Bullet.CoolDown = BulletNormal.SuggestedCooldown;
                Bullet.cost = BulletNormal.SuggestedCost;
                Bullet.FireSound = BulletNormal.NORMAL_SOUND;
                Barrel = BulletNormal.NORMAL_BARREL;
                break;
            case 1: // shotgun
                Bullet.CoolDown = BulletShotGun.SuggestedCooldown;
                Bullet.cost = BulletShotGun.SuggestedCost;
                Bullet.FireSound = BulletShotGun.SHOTGUN_SOUND;
                Barrel = BulletShotGun.SHOTGUN_BARREL;
                break;
            case 2: // bounce
                Bullet.CoolDown = BulletBounce.SuggestedCooldown;
                Bullet.cost = BulletBounce.SuggestedCost;
                Bullet.FireSound = BulletBounce.BOUNCE_SOUND;
                Barrel = BulletBounce.BOUNCE_BARREL;
                break;
            case 3: // bomb
                Bullet.CoolDown = BulletBomb.SuggestedCooldown;
                Bullet.cost = BulletBomb.SuggestedCost;
                Bullet.FireSound = BulletBomb.BOMB_SOUND;
                BulletBomb.previousStickState = fixedGun;
                Barrel = BulletBomb.BOMB_BARREL;
                fixedGun = true;
                break;
            case 4:
                Bullet.CoolDown = BulletMissle.SuggestedCooldown;
                Bullet.cost = BulletMissle.SuggestedCost;
                Bullet.FireSound = BulletBomb.BOMB_SOUND;
                BulletBomb.previousStickState = fixedGun;
                Barrel = BulletMissle.MISSLE_BARREL;
                fixedGun = true;
                break;
        }

    }

    public void fireBullet() {
        if (shotCoolDown > 0 || score <= 0) {
            return;
        }
        Bullet bu = null;
        if (score - Bullet.cost < 0) {
            return;
        }
        switch (ammoType) {
            case 0: // Normal
                bu = new BulletNormal(getRotatedFirePointX(),
                        getRotatedFirePointY(), BulletAngle, getSpeed(),
                        Entity.FORM_WIDTH);
                break;
            case 1: // shotgun
                bu = new BulletShotGun(getRotatedFirePointX(),
                        getRotatedFirePointY(), BulletAngle);
                break;
            case 2: // bouncer
                bu = new BulletBounce(getRotatedFirePointX(),
                        getRotatedFirePointY(), BulletAngle, getSpeed(), 0);
                break;
            case 3: // bomb
                bu = new BulletBomb(getRotatedFirePointX(), getRotatedFirePointY(),
                        (float) (BulletAngle - Math.toRadians(getRotation())));
                break;
            case 4: // missle
                bu = new BulletMissle(getRotatedFirePointX(),
                        getRotatedFirePointY(), BulletAngle);
                break;
        }

        if (bu != null) {
            b.add(bu);
        }
        shotRate = Bullet.CoolDown;
        // shotRate = 120; //TEMPORARY
        score -= (Bullet.cost * priceMult);
        shotCoolDown = shotRate;
        if (Bullet.FireSound != null) {
            Bullet.FireSound.playAsSoundEffect(1.0f, 0.5f, false);
        }
    }

    @Override
    public void move(Entity e) {
        if (health < 0) {
            return;
        }
        // things done every tick
        // score+=1;
        if (damagetick > 0) {
            damagetick -= 1;
        }
        tick++;

        // friction
        xv = Util.doFrictionX(xv, yv, friction);
        yv = Util.doFrictionY(xv, yv, friction);

        // move ship
        x += xv;
        y += yv;

        // Wrap Screen
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

        // move bounds
        MoveBounds();

        // move and cull bullets
        for (int i = 0; i <= b.size() - 1; i++) {
            b.get(i).move(this);
        }
        for (int i = 0; i <= b.size() - 1; i++) {
            if (b.get(i).cull()) {
                score += b.get(i).getScore();
                b.remove(i);
            }
        }

        // do Cooldown
        if (shotCoolDown > 0) {
            shotCoolDown--;
        }
        isForward = false;
        isReverse = false;
    }

    public void MoveBounds() {
        shape.setCenterX(x + 25 * scale);
        shape.setCenterY(y + 25 * scale);
    }

    public void Forward(float up, float dn, int delta, Entity e) {
        isForward = true;
        xa = up;
        ya = dn;
        // apply acceleration if below speed limit
        float speed = (float) Math.sqrt((xv * xv) + (yv * yv));
        if (speed < MaxSpeed || (xv < 0 && xa > 0) || (xv > 0 && xa < 0)) {
            xv += xa;
        }
        if (speed < MaxSpeed || (yv > 0 && ya < 0) || (yv < 0 && ya > 0)) {
            yv += ya;
        }
    }

    public void Forward(float up, float dn, int delta) {
        Forward(up, dn, delta, this);
    }

    public void Reverse(float up, float dn, int delta, Entity e) {
        isReverse = true;
        xa = -up;
        ya = -dn;
        // apply acceleration if below speed limit
        float speed = (float) Math.sqrt((xv * xv) + (yv * yv));
        if (speed < MaxSpeed / 4 || (xv < 0 && xa > 0) || (xv > 0 && xa < 0)) {
            xv += xa;

        }
        if (speed < MaxSpeed / 4 || (yv > 0 && ya < 0) || (yv < 0 && ya > 0)) {
            yv += ya;
        }
    }

    public void Reverse(float up, float dn, int delta) {
        Reverse(up, dn, delta, this);
    }

    public void Rotate(float rad) {
        if (Ship != null) {
            Ship.rotate((float) Math.toDegrees(rad));
        }
        Flame.rotate((float) Math.toDegrees(rad));
        ReverseFlame.rotate((float) Math.toDegrees(rad));
        // change centre of rotation
        shape = (Polygon) shape.transform(Transform.createRotateTransform(rad,
                x, y));

        rotation += Math.toDegrees(rad);
        while (rotation > 360 || rotation < 0) {
            if (rotation > 360) {
                rotation -= 360;
            } else if (rotation < 0) {
                rotation += 360;
            }
        }
        // rotate bullets if gun is fixed
        if (fixedGun) {
            BulletAngle += rad;
        }
        while (BulletAngle > Math.PI * 2 || BulletAngle < 0) {
            if (BulletAngle > Math.PI * 2) {
                BulletAngle -= Math.PI * 2;
            } else if (BulletAngle < 0) {
                BulletAngle += Math.PI * 2;
            }
        }
    } // IN RADIANS

    @Override
    public void render(GameContainer gc, Graphics g) {
        if (health < 0) {
            return;
        }
        if (Ship != null) {
            Ship.draw(x, y, scale);
        } else {
            g.setColor(Color.green);
            g.draw(shape);
        }
        g.setColor(Color.red);
        // Draw Debug
        // g.draw(shape); //bounds
        // g.drawOval(GetRotatedFirePointX(), GetRotatedFirePointY(), 5, 5);
        // //firepoint
        // g.drawOval(x, y, 5, 5);//true location

        // gun barrel
        if (Barrel != null) {
            Barrel.rotate((float) Math.toDegrees(BulletAngle) + 90
                    - Barrel.getRotation());
            Barrel.draw(getRotatedFirePointX()
                    - (Barrel.getWidth() * scale / 2), getRotatedFirePointY()
                    - (Barrel.getHeight() * scale / 2), scale);
        }
        // exhuast flame if a != 0
        if (isForward) {// (xa != 0 || ya != 0) {
            Flame.draw(x, y, scale);
        }
        if (isReverse) {// (xa != 0 || ya != 0) {
            ReverseFlame.draw(x, y, scale);
        }
        // bullets
        for (int i = 0; i <= b.size() - 1; i++) {
            b.get(i).render(gc, g);
        }
    }

    @Override
    public void death(byte conditions) {
        // temp debug invincibility
        System.out.println("DEAD: " + score);
    }

    @Override
    public float doDamage() {
        score += 10;
        return (int) (float)(50 * Bullet.dmgDealMult);
    }

    @Override
    public void takeDamage(float Damage) {
        // no damage if cooldown hasn't expired else damage and rest counter
        if (damagetick > 0) {
            return;
        }
        health -= (Damage * dmgRecieveMult);
        damagetick = damageCoolDown;
        if (health < 0) {
            death((byte) 0);
        }
    }

    @Override
    public void collides(Entity... en) {
        if (health < 0) {
            return;
        }
        for (Entity e : en) {
            if (e == null || e.getBounds() == null || e == this || e.cull()) {
                continue;
            }
            if (shape.intersects(e.getBounds())) {
                takeDamage(e.doDamage());
                e.takeDamage(doDamage());
                return;
            }
            // for (int i = 0; i <= b.size() - 1; i++) {
            //
            // if (b.get(i).Collides(e) != null) {
            // b.get(i).TakeDamage(e.DoDamage());
            // e.TakeDamage(b.get(i).DoDamage());
            // return b.get(i);
            // }
            // }
        }
        return;
    }

    @Override
    public boolean cull() {
        return false;
    }

    @Override
    public String getType() {
        return "Player";
    }

    @Override
    public String getSuperType() {
        return "Player";
    }

    @Override
    public Entity[] getAllChildren() {
        if (health < 0) {
            return null;
        }
        ArrayList<Entity> e = new ArrayList<>();
        e.add(this);
        for (int i = 0; i <= b.size() - 1; i++) {
            e.addAll(Arrays.asList(b.get(i).getAllChildren()));
        }
        return e.toArray(new Entity[e.size()]);
        // Entity[] e = new Entity[b.size()+1];
        //
        // for(int i = 0; i <= b.size()-1;){
        // e[i] = b.get(i);
        // }
        // e[b.size()+1] = this;
        // return e;
    }
}

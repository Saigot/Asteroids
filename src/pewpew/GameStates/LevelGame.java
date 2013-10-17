/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.GameStates;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import pewpew.Asteroid;
import pewpew.Entity;
import pewpew.InfiniteShot;
import pewpew.Player;

/**
 *
 * @author michael
 */
public class LevelGame extends StandardGame{
    int level = 0;
    
    boolean pointLimit = false;
    boolean PointsReq = false;
    int targetPts;
    
    boolean EnemyLimit = false;
    boolean EnemyReq = false;
    int EnemiesKilled = 0;
    int targetEnemies;
    
    ArrayList<String> EnemiesAccepted = new ArrayList<>();
    
    boolean LimitedTime = false;
    boolean TimeReq = false;
    int timeLimit;
    
    int secondsElasped = 0;
    
    public LevelGame(int startLevel){
        level = startLevel;
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        super.init(gc,sbg);
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
        for (int i = 0; i <= e.size() - 1; i++) {
            if(EnemiesAccepted.contains(e.get(i).GetType())){
                pewpew.Enemy en = (pewpew.Enemy) e.get(i);
                if(en.isKilled()){
                    EnemiesKilled++;
                }
             }
        }
        super.update(gc, sbg, delta);
    }
    
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException{
        super.render(gc, sbg, g);
        if(LevelConditionsMet()){
            g.drawString("Level Clear!", Entity.FORM_WIDTH/2 - 60, Entity.FORM_HEIGHT/2);
            g.drawString("press c to continue",Entity.FORM_WIDTH/2 -95, Entity.FORM_HEIGHT/2 + 20);
            //return;
        }
    } 
    
    @Override
    public void GetMotion(Input in, GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        super.GetMotion(in, gc, sbg, delta);
        if(in.isKeyPressed(Input.KEY_C) && LevelConditionsMet()){
            levelUp();
        }
    }
    
    public void levelUp() throws SlickException {
        level++;
        LevelReader lvl = new LevelReader();
        try {
            lvl.read(level, this);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LevelGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LevelGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean LevelConditionsMet(){
        if((PointsReq || (p.score < targetPts))){
            return true;
        }
        return false;
    }
    
    
}

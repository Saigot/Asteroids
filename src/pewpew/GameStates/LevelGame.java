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
    
    //TODO: make this an enum
    byte gameCondition = 1;
    //1 = not over
    //2 = Won
    //3 = loss
    
    boolean PtsSecondary = false;
    boolean PtsPrimary = false;
    int PtsLimit = 50200;
    
    boolean EnemySecondary = false;
    boolean EnemyPrimary = false;
    int EnemiesKilled = 0;
    int EnemyLimit;
    
    
    boolean TimeSecondary = false;
    boolean TimePrimary = false;
    int TimeLimit;
    int tick;
    
    
    public LevelGame(int startLevel){
        level = startLevel-1;
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        levelUp();
        super.init(gc,sbg);
    }
    
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
        tick++;
        for (int i = 0; i <= e.size() - 1; i++) {
            if (e.get(i).GetSuperType().equals("Enemy")) {
                pewpew.Enemy en = (pewpew.Enemy) e.get(i);
                if (en.isKilled() && en.GetKillCounts()) {
                    EnemiesKilled++;
                }
            }
        }
        super.update(gc, sbg, delta);
        UpdateLevelConditions();
    }
    
    
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
            throws SlickException{
        super.render(gc, sbg, g);
        if(gameCondition == 2){
            g.drawString("Level Clear!", Entity.FORM_WIDTH/2 - 60, Entity.FORM_HEIGHT/2);
            g.drawString("press c to continue",Entity.FORM_WIDTH/2 -95, Entity.FORM_HEIGHT/2 + 20);
            //return;
        }
    } 
    
    @Override
    public void GetMotion(Input in, GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        super.GetMotion(in, gc, sbg, delta);
        if(in.isKeyPressed(Input.KEY_C) && gameCondition == 2){
            levelUp();
        }
    }
    
    public void levelUp() throws SlickException {
        level++;
        gameCondition = 1;
        LevelReader lvl = new LevelReader();
        try {
            lvl.read(level, this);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LevelGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LevelGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean TimeMet(){
        return tick>TimeLimit;
    }
    
    public boolean EnemiesMet(){
      return EnemiesKilled>EnemyLimit;   
    }
    
    public boolean PointsMet(){
        return p.score >PtsLimit;
    }
    
    public boolean RequiredMet(){
        return (TimeMet()||!TimePrimary)
                && (EnemiesMet()||!EnemyPrimary)
                && (PointsMet()||!PtsPrimary) && 
                !(!EnemyPrimary && !EnemyPrimary && !PtsPrimary);
    }
    
    public boolean AnySecondaryMet(){
        return (TimeMet() && TimeSecondary)
               ||(PointsMet() && PtsSecondary)
               ||(EnemiesMet() && EnemySecondary);
    }
    
    public void UpdateLevelConditions(){
        if(TimeMet() && TimePrimary){
            if(RequiredMet()){
                gameCondition = 2;
            }
            gameCondition = 3;
            return; 
        }
        else if(RequiredMet()){
            gameCondition = 2;
        }else if(p.health < 0){
            gameCondition = 3;
        }else if(AnySecondaryMet()){
            gameCondition = 2;   
        }else{
            gameCondition = 1;
        }
    }
    
    @Override
    public boolean GameOver(){
        if(gameCondition == 3){
            p.health = -1;
            return true;
        }else return false;
    }
}

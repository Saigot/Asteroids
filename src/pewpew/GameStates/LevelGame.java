/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.GameStates;

import java.util.ArrayList;
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
    boolean Cont = false;
    
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
    
    public void AllowAllEnemies(){
        EnemiesAccepted.add("Asteroid");
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        super.init(gc,sbg);
        AllowAllEnemies();
        initLevel();
        Cont = false;
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
    
    private void initLevel(){
        switch(level){
            case 0:
                MaxEnemies = 1;
                MaxPowerups = 0;
                targetEnemies = 1;
                break;
            case 1:
                EnemyCoolDown = 20;
                MaxEnemies = 2;
                MaxPowerups = 0;
                targetEnemies = 6;
        }
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
    
    public void levelUp() {
        level++;
        Cont = true;
    }
    
    public boolean LevelConditionsMet(){
        switch(level){
            case 0:
                return EnemiesKilled >= targetEnemies;
            case 1:
        }
        return false;
    }
    
    @Override
    public boolean CanContinue(){
        return Cont;
    }
}

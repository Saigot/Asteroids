/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.GameStates;

import java.awt.FontMetrics;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.*;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;
import pewpew.Player;

/**
 *
 * @author michael
 */
public class MainGame extends StateBasedGame {
    boolean mute = true;
    
    StandardGame ARCADE;
    LevelGame LEVELS;
    PauseMenu PAUSED;
    StandardGame currentState;
    int gameType;
    
    /**
     * 
     * @param MainGame
     * contains defaults and logic common to every game mode; overridden
     * 
     */
    public MainGame(String Title, int startState) {
        super(Title);
        gameType = startState;
    }
    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        currentState.update(gc, this, delta);
    }

    public void GetUniversalOptions(Input in, GameContainer gc) throws SlickException{
        //listens for things that work always even in pause
        if (in.isKeyPressed(Input.KEY_ESCAPE)) {
           if(currentState == PAUSED){
               switch(gameType){
                   case 1:
                       currentState = ARCADE;
                       break;
                   case 2:
                       currentState = LEVELS;
                       break;
               }
           }else{
               currentState = PAUSED;
           }
            
        }
        //toggle music
        if(in.isKeyPressed(Input.KEY_M)){
            mute = !mute;
        }
        
    }
    
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        currentState.render(gc, this, g);
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        ARCADE = new StandardGame();
        LEVELS = new LevelGame(0);
        ARCADE.init(gc, this);
        LEVELS.init(gc, this);
        switch (gameType) {
            case 1:
                PAUSED = new PauseMenu(ARCADE);
                currentState = ARCADE;
                break;
            case 2:
                PAUSED = new PauseMenu(LEVELS);
                currentState = LEVELS;
                break;
        }
        PAUSED.init(gc, this);

    }
    
}

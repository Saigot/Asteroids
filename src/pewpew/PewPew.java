/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew;

import org.newdawn.slick.SlickException;

import pewpew.gamestates.StartScreen;

/**
 * 
 * @author michael
 */
public class PewPew {

    /**
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) throws SlickException {
        /*
         * TODO:add more powerups (negative and positive)add more gunsadd more
         * enemiesadd start screen (urggggg)finish pause menu create generic
         * on-screen message system level system with bonuses, unlocks etc
         * bosses: Giant Asteroid HighScores.txt, Options.txt options menu
         * resizers don't stack perfectly
         */
        StartScreen s = new StartScreen();
        s.build();
    }
}

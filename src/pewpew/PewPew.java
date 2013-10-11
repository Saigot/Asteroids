/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew;

import pewpew.GameStates.StartScreen;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 *
 * @author michael
 */
public class PewPew {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SlickException {
        /*TODO:
         *add more powerups (negative and positive)
         *add more guns
         *add more enemies
         *add start screen (urggggg)
         *finish pause menu 
         * create generic on-screen message system
         * level system with bonuses, unlocks etc
         * bosses: Giant Asteroid
         * HighScores.txt, Options.txt
         * options menu
         * resizers don't stack perfectly
         */
        StartScreen s = new StartScreen();
        s.build();
    }
}

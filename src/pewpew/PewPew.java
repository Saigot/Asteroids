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
        StartScreen s = new StartScreen();
        s.build();
    }
}

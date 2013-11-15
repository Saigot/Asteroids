/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.gamestates;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import pewpew.entities.Entity;
import pewpew.entities.Player;

/**
 * 
 * @author michael
 */
public class PauseMenu extends StandardGame {

	int StarArray[];
	Player p = null;

	PauseMenu() {
		StarArray = new int[(int) ((Math.random() * 50) + 1) * 3];
		for (int i = 0; i <= StarArray.length - 3; i += 3) {
			StarArray[i] = (int) (Math.random() * Entity.FORM_WIDTH); // x
			StarArray[i + 1] = (int) (Math.random() * Entity.FORM_HEIGHT); // radius
			StarArray[i + 2] = (int) (Math.random() * 10) + 1; // radius
		}
	}

	PauseMenu(int stararray[]) {
		StarArray = stararray;
		if (stararray == null) {
			StarArray = new int[] { -10, -10, -10 };
		}
	}

	PauseMenu(StandardGame b) {
		p = b.p;
		StarArray = b.StarArray;
	}

	@Override
	public int getID() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		// stars
		for (int i = 0; i <= StarArray.length - 3; i += 3) {
			g.setColor(new Color((255 + (int) (-Math.random() * 100)),
					(255 + (int) (-Math.random() * 100)), 255));
			int rand;
			rand = (int) (Math.random() * 2) - 3;

			g.fillOval(StarArray[i] + rand / 2, StarArray[i + 1] + rand / 2,
					StarArray[i + 2] + rand, StarArray[i + 2] + rand);
		}
		g.setColor(Color.darkGray);
		g.fillRoundRect((float) (gc.getWidth() * 0.15),
				(float) (gc.getHeight() * 0.15),
				(float) (gc.getWidth() * 0.75),
				(float) (gc.getHeight() * 0.75), 50);
		g.setColor(Color.white);

		// measure string width
		String[] instr = {
				"Instructions",
				"Use the arrow keys to move",
				"Use the A and D to rotate the gun left and right",
				"Press S to toggle locking the gun in relation to the ship or the screen",
				"Press W to fire the gun",
				"Press the numbers 1 to 5 to change weapons, different weapons will be unlocked",
				"Firing a weapon uses up points, with no more points you cannot shoot",
				"you can also cycle your weapons with Q and E keys" };
		for (int i = 0; i <= instr.length - 1; i++) {
			g.drawString(instr[i], gc.getWidth() / 2 - (instr[i].length() * 4),
					(gc.getHeight() * 0.15f) + ((25) * i));
		}

		// score
		if (p != null) {
			g.setColor(Color.white);
			NumberFormat formatter = new DecimalFormat("00000000");
			String scr = formatter.format(p.score);
			g.drawString(scr, gc.getWidth() - 100, gc.getHeight() - 50);
		}

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int i)
			throws SlickException {
		MainGame g = (MainGame) sbg;
		Input in = gc.getInput();
		if (in.isKeyDown(Input.KEY_R)) {
			sbg.initStatesList(gc);
		}
		g.GetUniversalOptions(in, gc);
	}
}

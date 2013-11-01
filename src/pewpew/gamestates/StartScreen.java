/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.gamestates;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 * 
 * @author michael
 */
public class StartScreen implements ActionListener {
	JFrame StartMenu = new JFrame();

	public void build() {
		StartMenu.setVisible(true);
		StartMenu.setSize(1000, 700);
		StartMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton Arcade = new JButton("Arcade");
		JButton LevelMode = new JButton("Level Mode");
		JButton Options = new JButton("Options");
		JButton Story = new JButton("The Story So Far...");

		StartMenu.setLayout(new GridLayout(2, 1));

		StartMenu.add(Arcade);
		StartMenu.add(LevelMode);
		StartMenu.add(Options);
		StartMenu.add(Story);

		Arcade.addActionListener(this);
		LevelMode.addActionListener(this);
		Options.addActionListener(this);

		StartMenu.validate();
	}

	public void InitiateArcadeMode() throws SlickException {
		AppGameContainer app = new AppGameContainer(new MainGame("PewPew", 1));
		// if you change this change the corresponding values in Entity
		app.setUpdateOnlyWhenVisible(true);
		app.setVerbose(false);
		app.setDisplayMode(1000, 700, false);
		app.start();
	}

	public void InitiateLevelMode() throws SlickException {
		AppGameContainer app = new AppGameContainer(new MainGame("PewPew", 2));
		// if you change this change the corresponding values in Entity
		app.setUpdateOnlyWhenVisible(true);
		app.setVerbose(false);
		app.setDisplayMode(1000, 700, false);
		app.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String c = e.getActionCommand();
		switch (c) {
		case "Arcade":
			try {
				StartMenu.dispose();
				InitiateArcadeMode();
			} catch (SlickException ex) {
				Logger.getLogger(StartScreen.class.getName()).log(Level.SEVERE,
						null, ex);
			}
			break;
		case "Level Mode":
			try {
				StartMenu.dispose();
				InitiateLevelMode();
			} catch (SlickException ex) {
				Logger.getLogger(StartScreen.class.getName()).log(Level.SEVERE,
						null, ex);
			}
			break;
		}
	}

}

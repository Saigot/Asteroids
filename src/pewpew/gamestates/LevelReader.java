/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.gamestates;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import org.newdawn.slick.SlickException;

import pewpew.entities.Asteroid;
import pewpew.entities.Bosteroid;
import pewpew.entities.Bullet;
import pewpew.entities.Player;
import pewpew.entities.powerups.HealthUp;
import pewpew.entities.powerups.InfiniteShot;
import pewpew.entities.powerups.RandomUp;
import pewpew.entities.powerups.ShrinkUp;

/**
 *
 * @author michael
 */
public class LevelReader {

    static final int LEVELCAP = 10;

    public void read(int level, LevelGame g) throws IOException, SlickException {
        String line;
        String fileName;
        level = 10;
        if (level > LEVELCAP) {
            fileName = "Victory.lvl";
        } else {
            DecimalFormat myFormatter = new DecimalFormat("0000");
            fileName = myFormatter.format(level) + ".lvl";
        }

        // File file = new File("Res/Lvls/" +fileName);
        InputStream in = getClass().getResourceAsStream("/Res/Lvls/" + fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String[] firstLine = br.readLine().split(",");
        int[] StartingCond = new int[firstLine.length];
        for (int i = 0; i <= firstLine.length - 1; i++) {
            StartingCond[i] = Integer.parseInt(firstLine[i]);
        }
        g.MaxPowerups = StartingCond[0];
        g.PowerUpCoolDown = StartingCond[1];
        g.EnemyCoolDown = StartingCond[2];
        g.MaxEnemies = StartingCond[3];

        line = br.readLine();
        String[] linelist = line.split(",");
        g.PtsPrimary = Boolean.parseBoolean(linelist[0]);
        g.PtsSecondary = Boolean.parseBoolean(linelist[1]);
        if (g.PtsPrimary || g.PtsSecondary) {
            g.PtsLimit = Integer.parseInt(linelist[2]);
        }

        line = br.readLine();
        linelist = line.split(",");
        g.TimePrimary = Boolean.parseBoolean(linelist[0]);
        g.TimeSecondary = Boolean.parseBoolean(linelist[1]);
        if (g.TimePrimary || g.TimeSecondary) {
            g.TimeLimit = Integer.parseInt(linelist[2]);
        }

        line = br.readLine();
        linelist = line.split(",");
        g.EnemyPrimary = Boolean.parseBoolean(linelist[0]);
        g.EnemySecondary = Boolean.parseBoolean(linelist[1]);
        if (g.EnemyPrimary || g.EnemySecondary) {
            g.EnemyLimit = Integer.parseInt(linelist[2]);
        }

        line = br.readLine();
        String[] enemies = line.split(",");
        for (int i = 0; i <= enemies.length - 3; i += 3) {
            switch (enemies[i]) {
                case "Asteroid":
                    Asteroid a = new Asteroid(i, i, null);
                    a.SetKillCounts(Boolean.parseBoolean(enemies[i + 1]));
                    a.SetSpawnProb(Double.parseDouble(enemies[i + 2]));
                    break;
                case "Bosteroid":
                    Bosteroid b = new Bosteroid(null);
                    b.SetKillCounts(Boolean.parseBoolean(enemies[i + 1]));
                    b.SetSpawnProb(Double.parseDouble(enemies[i + 2]));
                    break;
            }
        }
        line = br.readLine();
        String[] Powerups = line.split(",");
        for (int i = 0; i <= Powerups.length - 2; i += 2) {
            switch (Powerups[i]) {
                case "HealthUp":
                    HealthUp a = new HealthUp();
                    a.SetSpawnProb(Double.parseDouble(Powerups[i + 1]));
                    break;
                case "RandomUp":
                    RandomUp r = new RandomUp();
                    r.SetSpawnProb(Double.parseDouble(Powerups[i + 1]));
                    break;
                case "InfiniteShot":
                    InfiniteShot is = new InfiniteShot();
                    is.SetSpawnProb(Double.parseDouble(Powerups[i + 1]));
                    break;
                case "ShrinkUp":
                    ShrinkUp s = new ShrinkUp();
                    s.SetSpawnProb(Double.parseDouble(Powerups[i + 1]));
                    break;
            }
        }
        line = br.readLine();
        String[] Guns = line.split(",");
        for (int i = 0; i <= Guns.length - 1; i++) {
            StandardGame.GunsAllowed[i] = Boolean.parseBoolean(Guns[i]);
        }
        line = br.readLine();
        Bullet.dmgDealMult = Double.parseDouble(line);
        line = br.readLine();
        Player.priceMult = Double.parseDouble(line);
        line = br.readLine();
        Player.dmgRecieveMult = Double.parseDouble(line);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.GameStates;

import java.io.*;
import java.text.DecimalFormat;
import org.newdawn.slick.SlickException;
import pewpew.*;
import pewpew.Guns.Bullet;

/**
 *
 * @author michael
 */
public class LevelReader {
    public void read(int level, LevelGame g) throws FileNotFoundException, IOException, SlickException {
        String line;
        DecimalFormat myFormatter = new DecimalFormat("0000");
        String fileName = myFormatter.format(level) + ".lvl";
        //File file = new File("Res/Lvls/" +fileName);
        InputStream in = getClass().getResourceAsStream("/Res/Lvls/" +fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String[] firstLine = br.readLine().split(",");
        int[] StartingCond = new int[firstLine.length];
        for(int i = 0; i <= firstLine.length-1; i++){
            StartingCond[i] = Integer.parseInt(firstLine[i]);
        }
        g.MaxPowerups = StartingCond[0];
        g.PowerUpCoolDown = StartingCond[1];
        g.EnemyCoolDown = StartingCond[2];
        g.MaxEnemies = StartingCond[3];
        
        line =br.readLine();
        String[] linelist = line.split(",");
        g.PtsPrimary = Boolean.parseBoolean(linelist[0]);
        g.PtsSecondary = Boolean.parseBoolean(linelist[1]);
        if(g.PtsPrimary || g.PtsSecondary){
            g.PtsLimit = Integer.parseInt(linelist[2]);
        }
        
        line =br.readLine();
        linelist = line.split(",");
        g.TimePrimary = Boolean.parseBoolean(linelist[0]);
        g.TimeSecondary = Boolean.parseBoolean(linelist[1]);
        if(g.TimePrimary || g.TimeSecondary){
            g.TimeLimit = Integer.parseInt(linelist[2]);
        }
        
        line =br.readLine();
        linelist = line.split(",");
        g.EnemyPrimary = Boolean.parseBoolean(linelist[0]);
        g.EnemySecondary = Boolean.parseBoolean(linelist[1]);
        if(g.EnemyPrimary || g.EnemySecondary){
            g.EnemyLimit = Integer.parseInt(linelist[2]);
        }
  
        line = br.readLine();
        String[] enemies= line.split(",");
        for (int i = 0; i <= enemies.length-3; i+=3){
            switch(enemies[0]){
                case "Asteroid":
                    Asteroid a = new Asteroid(i, i, null);
                    a.SetKillCounts(Boolean.parseBoolean(enemies[1]));
                    a.SetSpawnProb(Double.parseDouble(enemies[2]));
                    break;
            }
        }
        line = br.readLine();
        String[] Powerups= line.split(",");
        for (int i = 0; i <= enemies.length-1; i+=3){
            switch(enemies[0]){
                case "HealthUp":
                    HealthUp a = new HealthUp();
                    a.SetSpawnProb(Double.parseDouble(enemies[2]));
                    break;
                case "RandomUp":
                    RandomUp r = new RandomUp();
                    r.SetSpawnProb(Double.parseDouble(enemies[2]));
                    break;
                case "InfiniteShot":
                    InfiniteShot is = new InfiniteShot();
                    is.SetSpawnProb(Double.parseDouble(enemies[2]));
                    break;
                case "ShrinkUp":
                    ShrinkUp s = new ShrinkUp();
                    s.SetSpawnProb(Double.parseDouble(enemies[2]));
                    break;
            }
        }
        line = br.readLine();
        String[] Guns= line.split(",");
        for (int i = 0; i <= Guns.length-1; i++){
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

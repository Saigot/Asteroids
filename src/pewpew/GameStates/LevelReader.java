/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pewpew.GameStates;

import com.sun.corba.se.impl.naming.cosnaming.InterOperableNamingImpl;
import java.io.*;
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
        String fileName = String.format("0000", Integer.toString(level));
        File file = new File("./" + fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
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
        if(line.equals("false")){
            g.pointLimit = false;
        }else{
            g.pointLimit = true;
            g.targetPts= Integer.parseInt(line.split(",")[1]);
        }
        line =br.readLine();
        if(line.equals("false")){
            g.LimitedTime = false;
        }else{
            g.LimitedTime = true;
            g.timeLimit= Integer.parseInt(line.split(",")[1]);
        }
        line =br.readLine();
        if(line.equals("false")){
            g.LimitedTime = false;
        }else{
            g.LimitedTime = true;
            g.timeLimit= Integer.parseInt(line.split(",")[1]);
        }
        line =br.readLine();
        if(line.equals("false")){
            g.EnemyLimit = false;
        }else{
            g.EnemyLimit = true;
            g.MaxEnemies= Integer.parseInt(line.split(",")[1]);
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
        for (int i = 0; i <= enemies.length-1; i+=3){
            StandardGame.GunsAllowed[i] = Boolean.parseBoolean(Guns[i]);
        }
        line = br.readLine();
        Player.dmgRecieveMult = Double.parseDouble(line);
        line = br.readLine();
        Player.priceMult = Double.parseDouble(line);
        line = br.readLine();
        Bullet.dmgDealMult = Double.parseDouble(line);
    }
}

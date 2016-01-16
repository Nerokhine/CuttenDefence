/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * TowerTurret Class
 */
package cuttendefence;
//imports
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class TowerTurret extends AbstractTower {
    //musics player variables

    protected AudioInputStream audioInputStream;
    protected Clip clip;
    protected FloatControl gainControl;
    //shotSpeed counter
    protected int shotSpeedCounter = 0;

    public TowerTurret(int range, int shotSpeed, double damage, int xPos, int yPos, int price, AbstractMap map) {//constructor
        super(range, shotSpeed, damage, xPos, yPos, price, map, 0);//calls constructor in abstract tower class
        //sets up sounds to be played
        try {

            audioInputStream = AudioSystem.getAudioInputStream(new File("Sounds\\gun.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);



        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public void shoot() {
        //this part is very similar to the laser class shoot method
        shotSpeedCounter += 1;

        if (shotSpeedCounter >= shotSpeed) {

            shotSpeedCounter = 0;
            double greatestD = 0;
            int p = -1;
            for (int x = 0; x < AbstractEnemy.getNumEnemies(); x++) {
                if (map.getEnemy(x).getDistance() > greatestD && Math.abs(xPos - map.getEnemy(x).getxPos()) < range && Math.abs(yPos - map.getEnemy(x).getyPos()) < range
                         && map.getEnemy(x).getEnemyID() != 2 && map.getEnemy(x).getEnemyID() != 5 && map.getEnemy(x).getEnemyID() != 8) {
                    greatestD = map.getEnemy(x).getDistance();
                    p = x;
                }
            }
            if (p != -1) {
                //same as the music player in the nuke class
                try {
                    clip.close();
                    audioInputStream = AudioSystem.getAudioInputStream(new File("Sounds\\gun.wav").getAbsoluteFile());
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-25.0f); // Reduce volume by 10 decibels.
                    clip.start();


                } catch (Exception ex) {
                    System.out.println("Error with playing sound.");
                    ex.printStackTrace();
                }

                try {
                    if (map.getEnemy(p).getxPos() > xPos) {
                        rotation = Math.atan(((double) yPos - (double) map.getEnemy(p).getyPos()) / ((double) xPos - (double) map.getEnemy(p).getxPos())) - (Math.PI * 3 / 2);
                    } else {
                        rotation = Math.atan(((double) yPos - (double) map.getEnemy(p).getyPos()) / ((double) xPos - (double) map.getEnemy(p).getxPos())) - Math.PI / 2;
                    }
                } catch (ArithmeticException e) {
                }

                map.getEnemy(p).setHealth(map.getEnemy(p).getHealth() - damage, p);
                //similarities to other towers end here (pretty much the whole class is similar to other classes)
            }
        }

    }
}

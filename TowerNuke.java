/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * TowerNuke Class
 */
package cuttendefence;
//imports
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class TowerNuke extends AbstractTower {
    //variables to do with playing sound

    protected AudioInputStream audioInputStream;
    protected Clip clip;
    protected FloatControl gainControl;
    //range of the blast radius of the nuke bomb
    protected int bombRange = 200;
    protected int shotSpeedCounter = 0;
    //position of the cursor (nuke is manually fired wherever you want on the map)
    protected int xPos2 = 0, yPos2 = 0;

    public TowerNuke(int range, int shotSpeed, double damage, int xPos, int yPos, int price, AbstractMap map) {//constructor
        super(range, shotSpeed, damage, xPos, yPos, price, map, 4);//calls constructor in abstract tower class
        //sets up the bomb sound to be played
        try {

            audioInputStream = AudioSystem.getAudioInputStream(new File("Sounds\\bigbomb.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);



        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    public boolean shoot(boolean shoot, int xPos2, int yPos2) {
        //position of the mouse is passed and stored
        this.xPos2 = xPos2;
        this.yPos2 = yPos2;
        //very similar to TowerBlade.java except that the mouse position is used during the within range calculation
        if (shotSpeedCounter >= shotSpeed && shoot) {//only fires once every 10 seconds
            shotSpeedCounter = 0;

            ArrayList<Integer> nums = new ArrayList();
            for (int x = 0; x < map.getEnemyNum(); x++) {

                if (Math.abs(xPos2 - map.getEnemy(x).getxPos()) < bombRange && Math.abs(yPos2 - map.getEnemy(x).getyPos()) < bombRange) {
                    nums.add(x);
                }
            }
            for (int x = 0; x < nums.size(); x++) {
                try {
                    map.getEnemy(nums.get(x)).setHealth(map.getEnemy(nums.get(x)).getHealth() - damage, nums.get(x));


                } catch (IndexOutOfBoundsException e) {
                }
            }
            //similarities end here
            //bomb sound is played upon fire
            try {
                clip.close();
                audioInputStream = AudioSystem.getAudioInputStream(new File("Sounds\\bigbomb.wav").getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                //make the bomb sound quieter
                gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-25.0f); // Reduce volume by 10 decibels.
                clip.start();


            } catch (Exception ex) {//if anything goes wrong, catch the error
                System.out.println("Error with playing sound.");
                ex.printStackTrace();
            }
            return true;//if the nuke fires, return true

        }
        return false;//if the nuke doesn't fire, return false
    }

    public void incrementCounter() {//this is called instead of the shoot method when towers are shooting
        shotSpeedCounter += 1;
    }

    public int getShotSpeedCounterModified() {//returns how much time has passed in seconds since the last fire of the nuke 
        //(if 10 seconds, it stays 10)
        int x = shotSpeedCounter / 60;
        if (x > 10) {
            x = 10;
        }
        return x;
    }
}

/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * TowerLaser Class
 */
package cuttendefence;
//imports
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class TowerLaser extends AbstractTower {

    protected int shotSpeedCounter = 0;

    public TowerLaser(int range, int shotSpeed, double damage, int xPos, int yPos, int price, AbstractMap map) {//constructor
        super(range, shotSpeed, damage, xPos, yPos, price, map, 1);//calls constructor in abstract tower class

    }

    public void shoot(Graphics2D g) {
        //blade towers damage anything in their range
        shotSpeedCounter += 1;

        if (shotSpeedCounter >= shotSpeed) {//if it is time for the tower to shoot

            shotSpeedCounter = 0;//reset counter

            double greatestD = 0;//will hold the distance of the enemy furthest along the track
            int p = -1;//index of the enemy futherst along the track
            for (int x = 0; x < AbstractEnemy.getNumEnemies(); x++) {//loops through all enemies on the track
                if (map.getEnemy(x).getDistance() > greatestD && Math.abs(xPos - map.getEnemy(x).getxPos()) < range && Math.abs(yPos - map.getEnemy(x).getyPos()) < range
                        && map.getEnemy(x).getEnemyID() != 2 && map.getEnemy(x).getEnemyID() != 5 && map.getEnemy(x).getEnemyID() != 8) {
                    //if an enemy has a greater distance than the greatest enemy checked before it and 
                    //the enemy is not a flying enemy and the enemy is within range of the tower, store the distance and the index of the enemy
                    greatestD = map.getEnemy(x).getDistance();
                    p = x;
                }
            }


            if (p != -1) {//if an enemy was found at all within range of the tower
                try {//prevent dividing by 0
                    //rotates the tower towards the enemy that it is firing by using complex formulas with radians and stores it in a rotation variable
                    if (map.getEnemy(p).getxPos() > xPos) {
                        rotation = Math.atan(((double) yPos - (double) map.getEnemy(p).getyPos()) / ((double) xPos - (double) map.getEnemy(p).getxPos())) - (Math.PI * 3 / 2);
                    } else {
                        rotation = Math.atan(((double) yPos - (double) map.getEnemy(p).getyPos()) / ((double) xPos - (double) map.getEnemy(p).getxPos())) - Math.PI / 2;
                    }

                } catch (ArithmeticException e) {
                }
                //draws the laser
                drawLaser(g, map.getEnemy(p).getxPos(), map.getEnemy(p).getyPos());
                //damages the targeted enemy
                map.getEnemy(p).setHealth(map.getEnemy(p).getHealth() - damage, p);

            }

        }
    }

    public void drawLaser(Graphics2D g, int xPosE, int yPosE) {
        //draws a laser from the tower to the enemy it is damaging
        g.setColor(Color.RED);
        g.drawLine(xPos, yPos, xPosE, yPosE);


    }
}

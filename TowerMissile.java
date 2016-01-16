/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * TowerMissile Class
 */
package cuttendefence;

import java.util.ArrayList;

public class TowerMissile extends AbstractTower {

    protected double shotSpeedCounter = 0;

    public TowerMissile(int range, int shotSpeed, double damage, int xPos, int yPos, int price, AbstractMap map) {//constructor
        super(range, shotSpeed, damage, xPos, yPos, price, map, 3);//calls constructor in abstract tower class
    }

    public void shoot() { //missile towers have homing shots
        //everything here is similar to the TowerLaser.java
        shotSpeedCounter += 1;

        if (shotSpeedCounter >= shotSpeed) {
            shotSpeedCounter = 0;
            double greatestD = 0;
            int p = -1;
            for (int x = 0; x < AbstractEnemy.getNumEnemies(); x++) {
                if (map.getEnemy(x).getDistance() > greatestD && Math.abs(xPos - map.getEnemy(x).getxPos()) < range && Math.abs(yPos - map.getEnemy(x).getyPos()) < range) {
                    greatestD = map.getEnemy(x).getDistance();
                    p = x;
                }
            }


            if (p != -1) {
                try {
                    if (map.getEnemy(p).getxPos() > xPos) {
                        rotation = Math.atan(((double) yPos - (double) map.getEnemy(p).getyPos()) / ((double) xPos - (double) map.getEnemy(p).getxPos())) - (Math.PI * 3 / 2);
                    } else {
                        rotation = Math.atan(((double) yPos - (double) map.getEnemy(p).getyPos()) / ((double) xPos - (double) map.getEnemy(p).getxPos())) - Math.PI / 2;
                    }
                } catch (ArithmeticException e) {
                }
                //similarities end here
                //instead of damaging an enemy , the missile tower creaqtes a missile object at the position of the tower and with the same rotation
                Missile missile = new Missile(xPos, yPos, 20, damage, map, rotation);
            }


        }
    }
}

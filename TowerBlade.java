/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * TowerBlade Class
 */
package cuttendefence;
//imports
import java.util.ArrayList;

public class TowerBlade extends AbstractTower {

    protected int shotSpeedCounter = 0;//holds shotspeed counter

    public TowerBlade(int range, int shotSpeed, double damage, int xPos, int yPos, int price, AbstractMap map) {//constructor

        super(range, shotSpeed, damage, xPos, yPos, price, map, 2);//calls constructor in abstract tower class
    }

    public void shoot() { //shoot method
        //blade towers damage anything in their range
        shotSpeedCounter += 1;//counter
        rotation = rotation += .3;//rotation interval for blades (constantly spinning)

        if (shotSpeedCounter >= shotSpeed) {//if it is time for the tower to shoot
            shotSpeedCounter = 0;//reset counter

            ArrayList<Integer> nums = new ArrayList();//array that holds enemy indexes within fire range
            for (int y = 0; y < AbstractEnemy.getNumEnemies(); y++) {//loops through all enemies on the map
                if (Math.abs(xPos - map.getEnemy(y).getxPos()) < range && Math.abs(yPos - map.getEnemy(y).getyPos()) < range
                        && map.getEnemy(y).getEnemyID() != 2 && map.getEnemy(y).getEnemyID() != 5 && map.getEnemy(y).getEnemyID() != 8) {
                    //if an enemy falls within the range of the tower and is not a flying enemy, add that enemy to the array
                    nums.add(y);
                }
            }


            //damages all enemies contained in the array
            for (int x = 0; x < nums.size(); x++) {
                try {
                    map.getEnemy(nums.get(x)).setHealth(map.getEnemy(nums.get(x)).getHealth() - damage, x);
                } catch (IndexOutOfBoundsException e) {
                }

            }

        }



    }
}

/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * Missile Class
 */
package cuttendefence;

import java.util.ArrayList;

public class Missile {
    //similar kinds variables as the AbstractTower.java class. range in this case is the blast radius of the missile

    protected int xPos, yPos, speed, speedX, speedY, range = 120;
    protected double damage, rotation;
    protected AbstractMap map;
    protected double greatestD = 0;
    //holds index of the enemy with the greatest distance
    protected int p = -1;

    public Missile(int xPos, int yPos, int speed, double damage, AbstractMap map, double rotation) {//constructor
        this.xPos = xPos;
        this.yPos = yPos;
        this.speed = speed;
        this.damage = damage;
        this.map = map;
        this.rotation = rotation;
        //missile locks on to the enemy that is the futherst distance along the map
        for (int x = 0; x < AbstractEnemy.getNumEnemies(); x++) {
            if (map.getEnemy(x).getDistance() > greatestD) {
                greatestD = map.getEnemy(x).getDistance();
                p = x;
            }
        }
        //the missile sets its direction of path towards the spot that the enemy it is locked on to last was
        //the missile does not change directions so it is prone to miss
        //speedX and speedY add up to 20 (This gives it a direction with constant speed)
        if (xPos < map.getEnemy(p).getxPos()) {
            speedX = (int) ((double) (Math.abs(xPos - map.getEnemy(p).getxPos()) / (double) ((Math.abs(xPos - map.getEnemy(p).getxPos())) + Math.abs(yPos - map.getEnemy(p).getyPos()))) * speed);
        } else {
            speedX = (int) ((double) (Math.abs(xPos - map.getEnemy(p).getxPos()) / (double) ((Math.abs(xPos - map.getEnemy(p).getxPos())) + Math.abs(yPos - map.getEnemy(p).getyPos()))) * speed) * -1;
        }
        if (yPos < map.getEnemy(p).getyPos()) {
            speedY = (int) ((double) (Math.abs(yPos - map.getEnemy(p).getyPos()) / (double) ((Math.abs(xPos - map.getEnemy(p).getxPos())) + Math.abs(yPos - map.getEnemy(p).getyPos()))) * speed);
        } else {
            speedY = (int) ((double) (Math.abs(yPos - map.getEnemy(p).getyPos()) / (double) ((Math.abs(xPos - map.getEnemy(p).getxPos())) + Math.abs(yPos - map.getEnemy(p).getyPos()))) * speed) * -1;
        }
        //add this created missile to the map class
        map.addMissiles(this);

    }

    public boolean move() {



        if (p != -1) {

            //missile moves based on calculated speedX and speedY values above
            xPos += speedX;
            yPos += speedY;
            for (int c = 0; c < AbstractEnemy.getNumEnemies(); c++) {
                if ((Math.abs((xPos - map.getEnemy(c).getxPos())) < map.getEnemy(p).getSize()) && (Math.abs((yPos - map.getEnemy(c).getyPos())) < map.getEnemy(p).getSize())) {
                    ArrayList<Integer> nums = new ArrayList();
                    for (int x = 0; x < AbstractEnemy.getNumEnemies(); x++) {
                        //if the missile comes in contact with an enemy, destroy the missile and add all the enemies within it's blast radius to an array
                        if (Math.abs(xPos - map.getEnemy(x).getxPos()) < range && Math.abs(yPos - map.getEnemy(x).getyPos()) < range) {
                            nums.add(x);
                        }
                    }
                    for (int x = 0; x < nums.size(); x++) {
                        try {
                            //deal damage to all enemies within the missiles blast radius
                            map.getEnemy(nums.get(x)).setHealth(map.getEnemy(nums.get(x)).getHealth() - damage, nums.get(x));

                        } catch (IndexOutOfBoundsException e) {
                        }
                    }
                    return true;//returns true if the missile hits an enemy


                }
            }

        }
        if (xPos > 1100 || xPos < -100 || yPos > 750 || yPos < -100) {
            return true;//return true if the missile is not on the map anymore (this is to remove the missile from memory on the map)
        }
        return false;//return false if the missile did not hit an enemy or left the map
    }
    //getters and setters for variables

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
    //to string

    @Override
    public String toString() {
        return "Missile{" + "xPos=" + xPos + ", yPos=" + yPos + ", speed=" + speed + ", speedX=" + speedX + ", speedY=" + speedY + ", range=" + range + ", damage=" + damage + ", rotation=" + rotation + ", map=" + map + ", greatestD=" + greatestD + ", p=" + p + '}';
    }
}

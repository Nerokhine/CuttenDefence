/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * Enemy3 Class
 */
package cuttendefence;

public class Enemy3 extends AbstractEnemy {

    public Enemy3(int health, int speed, int xPos, int yPos, int money, int direction, AbstractMap map) {//basic fly enemy constructor
        super(health, speed, xPos, yPos, direction, money, map, 2);//calls constructor in the abstract enemy class
    }
}

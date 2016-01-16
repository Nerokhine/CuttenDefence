/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * Enemy4 Class
 */
package cuttendefence;

public class Enemy4 extends AbstractEnemy {

    public Enemy4(int health, int speed, int xPos, int yPos, int direction, int money, AbstractMap map) {//medium slow enemy constructor
        super(health, speed, xPos, yPos, direction, money, map, 3);//calls constructor in the abstract enemy class
    }
}

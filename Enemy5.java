/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * Enemy5 Class
 */
package cuttendefence;

public class Enemy5 extends AbstractEnemy {

    public Enemy5(int health, int speed, int xPos, int yPos, int direction, int money, AbstractMap map) {//medium fast enemy constructor
        super(health, speed, xPos, yPos, direction, money, map, 4);//calls constructor in the abstract enemy class
    }
}

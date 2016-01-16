/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * Enemy6 Class
 */
package cuttendefence;

public class Enemy6 extends AbstractEnemy {

    public Enemy6(int health, int speed, int xPos, int yPos, int direction, int money, AbstractMap map) {//medium fly enemy constructor
        super(health, speed, xPos, yPos, direction, money, map, 5);//calls constructor in the abstract enemy class
    }
}

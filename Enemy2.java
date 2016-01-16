/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * Enemy2 Class
 */
package cuttendefence;

public class Enemy2 extends AbstractEnemy {

    public Enemy2(int health, int speed, int xPos, int yPos, int money, int direction, AbstractMap map) {//basic fast enemy constructor
        super(health, speed, xPos, yPos, direction, money, map, 1);//calls constructor in the abstract enemy class
    }
}

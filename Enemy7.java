/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * Enemy7 Class
 */
package cuttendefence;

public class Enemy7 extends AbstractEnemy {

    public Enemy7(int health, int speed, int xPos, int yPos, int direction, int money, AbstractMap map) {//hard slow enemy constructor
        super(health, speed, xPos, yPos, direction, money, map, 6);//calls constructor in the abstract enemy class
    }
}

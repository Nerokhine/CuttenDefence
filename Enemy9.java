/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * Enemy9 Class
 */
package cuttendefence;

public class Enemy9 extends AbstractEnemy {

    public Enemy9(int health, int speed, int xPos, int yPos, int direction, int money, AbstractMap map) {//hard fly enemy constructor
        super(health, speed, xPos, yPos, direction, money, map, 8);//calls constructor in the abstract enemy class
    }
}

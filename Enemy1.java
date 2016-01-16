/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * Enemy1 Class
 */
package cuttendefence;

public class Enemy1 extends AbstractEnemy {

    public Enemy1(int health, int speed, int xPos, int yPos, int direction, int money, AbstractMap map) {//basic slow enemy constructor
        super(health, speed, xPos, yPos, direction, money, map, 0);//calls constructor in the abstract enemy class
    }
}

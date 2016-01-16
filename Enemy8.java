/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * Enemy8 Class
 */
package cuttendefence;

public class Enemy8 extends AbstractEnemy {

    public Enemy8(int health, int speed, int xPos, int yPos, int direction, int money, AbstractMap map) {//hard fast enemy constructor
        super(health, speed, xPos, yPos, direction, money, map, 7);//calls constructor in the abstract enemy class
    }
}

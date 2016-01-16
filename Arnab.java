/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * Arnab Class
 */
package cuttendefence;

public class Arnab extends AbstractEnemy {

    public Arnab(int health, int speed, int xPos, int yPos, int direction, int money, AbstractMap map) {//constructs the all and mighty "Arnab Deity" boss
        super(health, speed, xPos, yPos, direction, money, map, 9);//calls constructor in the abstract enemy class
    }
}

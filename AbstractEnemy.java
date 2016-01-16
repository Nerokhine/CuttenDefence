/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * AbstractEnemy Class
 */
package cuttendefence;

abstract public class AbstractEnemy {
    //static variable that holds the number of enemies on the map

    protected static int numEnemies = 0;
    //holds the orientation of the enemy (enemies only turn 90 degrees at a time)
    protected double rotation = 0;
    //holds the original health of the enemy and its current health (original health used for health bar calculations)
    protected double health, originalHealth;
    //speed determines how fast the enemy moves along the track
    //xPos and yPos are the coordinates of the enemy
    //distance is the distance the enemy has travelled along the track (used for targeting calculations)
    //size can be used for correclty placing images
    //money holds the amount of money you get for killing the enemy
    protected int speed, xPos, yPos, distance, size = 20, money;
    //direction tells the enemy which direction to move towards (this is influenced by keys along the map)
    protected int direction;
    //map object
    protected AbstractMap map;
    //allows the program to distinguish between different kinds of enemies (identifies what kind of enemy this is)
    protected int enemyID;

    public AbstractEnemy(double health, int speed, int xPos, int yPos, int direction, int money, AbstractMap map, int enemyID) {//constructor
        this.health = health;
        originalHealth = health;
        this.speed = speed;
        this.xPos = xPos;
        this.yPos = yPos;
        this.direction = direction;
        this.money = money;
        this.map = map;
        numEnemies += 1;
        this.enemyID = enemyID;

    }

    public void move() {//move method
        //checks if an enemy is within range of a key on the map
        //this allows enemies to move along a path as keys are placed along corners of the map
        for (int x = 0; x < map.getN(); x++) {
            if (xPos > map.getKeyX(x) - map.getKeyRange() && xPos < map.getKeyX(x) + map.getKeyRange()) {
                if (yPos > map.getKeyY(x) - map.getKeyRange() && yPos < map.getKeyY(x) + map.getKeyRange()) {
                    //if the enemy is within range of a key on the map, change its direction based on the value the key holds
                    direction = map.getKeyVal(x);
                }
            }
        }
        //the enemy moves and rotates based on the direction it is going (1 is up, 2 is right, 3 is down, 4 is left)
        if (direction == 3) {
            yPos += speed;
            rotation = -Math.PI;
        } else if (direction == 2) {
            xPos += speed;
            rotation = Math.PI / 2;
        } else if (direction == 1) {
            yPos -= speed;
            rotation = 0;
        } else if (direction == 4) {
            xPos -= speed;
            rotation = -Math.PI / 2;
        }
        //the enemies distance increases
        distance += 1;


    }

    //getters and setters for the variables
    public double getRotation() {
        return rotation;
    }

    public int getDirection() {
        return direction;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health, int x) {
        this.health = health;

    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public static int getNumEnemies() {
        return numEnemies;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public static void setNumEnemies(int numEnemies) {
        AbstractEnemy.numEnemies = numEnemies;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getOriginalHealth() {
        return originalHealth;
    }

    public int getEnemyID() {
        return enemyID;
    }
    //to string

    @Override
    public String toString() {
        return "AbstractEnemy{" + "health=" + health + ", speed=" + speed + ", xPos=" + xPos + ", yPos=" + yPos + '}';
    }
}

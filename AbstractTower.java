/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * AbstractTower Class
 */
package cuttendefence;

abstract public class AbstractTower {
    //dimensions are used for calculations based on image placement

    protected static double dimensions = 25;
    //determines how much damage a tower will do per hit to an enemy (or groups of enemies)
    protected double damage;
    //range is the radius that determines if a tower can hit an enemy (tower range is a square and range is half the side length of the square)
    //shotSpeed is a variable that determines how often a tower can fire (shotSpeed of 60 means it can fire once every second)
    //xPos and yPos are coordinates of the tower
    //towerID helps identify what type of tower the object is
    protected int range, shotSpeed, price, xPos, yPos, towerID;
    //holds the current map the tower is on
    protected AbstractMap map;
    //determines which direction the tower is facing
    protected double rotation = 0;

    public AbstractTower(int range, int shotSpeed, double damage, int xPos, int yPos, int price, AbstractMap map, int towerID) {//tower constructor
        this.range = range;
        this.shotSpeed = shotSpeed;
        this.damage = damage;
        this.xPos = xPos;
        this.yPos = yPos;
        this.price = price;
        this.map = map;
        this.towerID = towerID;
    }
    //getters and setters for the tower variables

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getShotSpeed() {
        return shotSpeed;
    }

    public void setShotSpeed(int shotSpeed) {
        this.shotSpeed = shotSpeed;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
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

    public static double getDimensions() {
        return dimensions;
    }

    public int getTowerID() {
        return towerID;
    }

    public double getRotation() {
        return rotation;
    }
    //to string

    @Override
    public String toString() {
        return "AbstractTower{" + "damage=" + damage + ", range=" + range + ", shotSpeed=" + shotSpeed + ", price=" + price + ", xPos=" + xPos + ", yPos=" + yPos + ", towerID=" + towerID + ", map=" + map + ", rotation=" + rotation + '}';
    }
}

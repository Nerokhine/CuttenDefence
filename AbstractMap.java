/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * AbstractMap Class (Most used object class and hold enemy and tower objects)
 */
package cuttendefence;

//imports
import java.util.ArrayList;

abstract public class AbstractMap {

    protected int n;//number of turns in the map
    protected int c; //number of paths in the map
    protected int dimensions = 25;//dimensions of a tower on the map
    //arrays holding the x and y positions of keys on the map
    protected int[] keyX;
    protected int[] keyY;
    //the range an enemy has to be within for the key to have an effect on said enemy
    protected int keyRange;
    //money holds your money, lives holds your lives and currentWave tells you which wave you are on
    protected int money;
    protected int lives;
    protected int currentWave = 1;
    //The most important variables
    //These hold the objects on the map and allow for various functions within the main method just by calling the map class
    protected ArrayList<AbstractEnemy> enemies = new ArrayList();
    protected ArrayList<AbstractTower> towers = new ArrayList();
    protected ArrayList<Missile> missiles = new ArrayList();
    protected int[] keyVal = new int[n]; //value of direction that the enemy goes in, 1 is up, 2 is right, 3 is down, 4 is left
    //holds how many waves there are in total on the map (not really used)
    protected int waves;
    //holds an array of coordinates that map out the path of the map with rectangles
    protected int[] pathX1 = new int[c];
    protected int[] pathX2 = new int[c];
    protected int[] pathY1 = new int[c];
    protected int[] pathY2 = new int[c];

    public AbstractMap(int n, int c, int currentWave, int lives, int money, int keyRange) {//constructor
        this.n = n;
        this.c = c;
        this.currentWave = currentWave;
        this.lives = lives;
        this.money = money;
        //initializes the arrays for the map keys and paths but does not define them (that happens with complex getters and setters)
        keyX = new int[n];
        keyY = new int[n];
        keyVal = new int[n];
        this.keyRange = keyRange;
        pathX1 = new int[c];
        pathX2 = new int[c];
        pathY1 = new int[c];
        pathY2 = new int[c];
    }

    //one of the most important methods
    //places a tower based on the position of your mouse and what tower you have selected (determined by which radio button is selected)
    public void placeTower(int xPos, int yPos, int towerSelection) {
        //boolean that determines if you can place a tower in the desired locations (initially yes)
        boolean yes = true;
        //if your cursor is on the path, you cannot place a tower
        for (int x = 0; x < c; x++) {
            if (xPos + dimensions > getPathX1(x) && xPos - dimensions < getPathX2(x)) {
                if (yPos + dimensions > getPathY1(x) && yPos - dimensions < getPathY2(x)) {
                    yes = false;
                }
            }
        }
        //if your cursor is on or too close to another tower, you cannot place a tower
        for (int x = 0; x < getTowerNum(); x++) {
            if (xPos > getTower(x).getxPos() - (dimensions + 15) && xPos < getTower(x).getxPos() + (dimensions + 15)) {
                if (yPos > getTower(x).getyPos() - (dimensions + 15) && yPos < getTower(x).getyPos() + (dimensions + 15)) {
                    yes = false;
                }
            }
        }
        //if your cursor is not on the map, you cannot place a tower
        if (xPos > 970 || yPos < 130) {
            yes = false;
        }
        //if you can place a tower, place a tower
        if (yes) {
            //initialize
            AbstractTower tower = null;
            //******** very important section. This sections determines the stats of all towers and creates objects of them on the map (stats can be modified here) ***********
            if (towerSelection == 0) {
                if (money >= 15) {
                    tower = new TowerTurret(250, 60, 20, xPos, yPos, 15, this);
                    money -= 15;
                }
            } else if (towerSelection == 1) {
                if (money >= 30) {
                    tower = new TowerLaser(150, 1, 1.5, xPos, yPos, 30, this);
                    money -= 30;
                }
            } else if (towerSelection == 2) {
                if (money >= 25) {
                    tower = new TowerBlade(100, 60, 10, xPos, yPos, 25, this);
                    money -= 25;
                }
            } else if (towerSelection == 3) {
                if (money >= 150) {
                    tower = new TowerMissile(1000, 90, 70, xPos, yPos, 150, this);//range shot speed damage price
                    money -= 150;
                }
            } else {
                if (money >= 200) {
                    tower = new TowerNuke(1000, 600, 350, xPos, yPos, 200, this);
                    money -= 200;
                }
            }
            if (tower != null) {
                //adds the created tower to the map
                addTower(tower);
            }
        }
    }

    //another very important method
    //this method spawns an enemy onto the map based on the desired location, direction of travel and enemy type
    public void spawnEnemy(int enemySelection, int xPos, int yPos, int direction) {
        //initialize
        AbstractEnemy enemy1 = null;
        //********* This section is also really important. This sections defines the stats of all enemies spawned on the map and creates these enemies. 
        //(Stats of enemies can be modifed here) ********
        if (enemySelection == 0) {
            enemy1 = new Enemy1(60, 1, xPos, yPos, direction, 2, this);
        } else if (enemySelection == 1) {
            enemy1 = new Enemy2(60, 5, xPos, yPos, direction, 2, this);
        } else if (enemySelection == 2) {
            enemy1 = new Enemy3(50, 3, xPos, yPos, direction, 2, this);
        } else if (enemySelection == 3) {
            enemy1 = new Enemy4(320, 2, xPos, yPos, direction, 5, this);
        } else if (enemySelection == 4) {
            enemy1 = new Enemy5(320, 5, xPos, yPos, direction, 5, this);
        } else if (enemySelection == 5) {
            enemy1 = new Enemy6(300, 3, xPos, yPos, direction, 5, this);
        } else if (enemySelection == 6) {
            enemy1 = new Enemy7(1500, 2, xPos, yPos, direction, 7, this);
        } else if (enemySelection == 7) {
            enemy1 = new Enemy8(600, 5, xPos, yPos, direction, 7, this);
        } else if (enemySelection == 8) {
            enemy1 = new Enemy9(700, 3, xPos, yPos, direction, 7, this);
        } else {
            enemy1 = new Arnab(40000, 1, xPos, yPos, direction, 1000, this);
        }
        //adds the created enemy to the map array;
        addEnemy(enemy1);
    }
    //complex setters that accept multiple paramaters in order to define one key and one path on the map

    public void setKey(int x, int keyX, int keyY, int keyVal) {
        this.keyX[x] = keyX;
        this.keyY[x] = keyY;
        this.keyVal[x] = keyVal;
    }

    public void setPath(int x, int pathX1, int pathX2, int pathY1, int pathY2) {
        this.pathX1[x] = pathX1;
        this.pathX2[x] = pathX2;
        this.pathY1[x] = pathY1;
        this.pathY2[x] = pathY2;
    }
    //simple getters and setters (even objects are passed)

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public void setCurrentWave(int currentWave) {
        this.currentWave = currentWave;
    }

    public Missile getMissiles(int t) {
        return missiles.get(t);
    }

    public void addMissiles(Missile missile) {
        missiles.add(missile);
    }

    public void removeMissiles(int t) {
        missiles.remove(t);
    }

    public int getMissileNum() {
        return missiles.size();
    }

    public void setWaves(int waves) {
        this.waves = waves;
    }

    public void setKeyRange(int keyRange) {
        this.keyRange = keyRange;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public void setC(int c) {
        this.c = c;
    }

    public int getC() {
        return c;
    }

    public int getPathX1(int x) {
        return pathX1[x];
    }

    public int getPathX2(int x) {
        return pathX2[x];
    }

    public int getPathY1(int x) {
        return pathY1[x];
    }

    public int getPathY2(int x) {
        return pathY2[x];
    }

    public int getKeyX(int x) {
        return keyX[x];
    }

    public int getKeyY(int x) {
        return keyY[x];
    }

    public int getKeyVal(int x) {
        return keyVal[x];
    }

    public int getWaves() {
        return waves;
    }

    public int getKeyRange() {
        return keyRange;
    }

    public AbstractEnemy getEnemy(int t) {
        return enemies.get(t);
    }

    public void addEnemy(AbstractEnemy enemy) {
        enemies.add(enemy);
    }

    public void removeEnemy(int t) {
        enemies.remove(t);
    }

    public int getEnemyNum() {
        return enemies.size();
    }

    public AbstractTower getTower(int t) {
        return towers.get(t);
    }

    public void addTower(AbstractTower tower) {
        towers.add(tower);
    }

    public void removeTower(int t) {
        towers.remove(t);
    }

    public int getTowerNum() {
        return towers.size();
    }

    public int getDimensions() {
        return dimensions;
    }
}

/*
 * Nikita Erokhine, Riley Freeborn, Arnab Dey
 * Cutten Defence
 * Main Class (Holds main method)
 * Cutten Defence is a tower defense game.
 * You must fend of waves of enemies by placing towers in strategic locations around the map
 * Enemies move along a path and try to harm Mr. Cutten
 * You win when you have defeated "Arnab Deity" (wave 30) and have not lost over 50 lives (let through 50 enemies)
 * WARNING: THIS GAME LAGS (that doesn't mean the game has crashed, just be patient when it happens)
 */
package cuttendefence;

//imports
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;

public class CuttenDefence extends JFrame {
    //variables for playing music

    static AudioInputStream audioInputStream;
    static Clip clip;
    static FloatControl gainControl;
    //x and y are cooridinates to hold your mouse position
    int x = 100, y = 100, size = 2;
    //holds the name you entered when you win
    String name = "";
    //variables explained later (they are too specific in their function)
    boolean first = false;
    boolean nukeSuccess = false;
    boolean shoot = false;
    int selectedTower = - 1;
    int nukePicDuration = 0;
    int missilePicDuration = 0;
    boolean missileSuccess = false;
    int musicLoopCount = 0;
    boolean waveEnd = false;
    boolean firstTime = true;
    static boolean changeMenu = true;
    boolean playMusic = true;
    //if you are currently selecting a nuke
    boolean selectedNuke = false;
    //important for wave functioning (holds how many enemies have spawned in the current wave)
    int enemyCounter = 0;
    //holds which tower you are ready to place
    int towerSelection = 0; //0 - Gun, 1 - Laser, 2 - Blade, 3 - Missile, 4 - Nuke
    //variable for cycling between menus (acts like a boolean)
    static int menuSelection = -1;//static because it is still present when you restart the game and make a new game object
    //if you are trying to show the range on a tower
    boolean showRange = false;
    //holds your score at the end of the game
    int yourScore = 0;
    //if you have won the game or not 
    boolean winner = false;
    //variables for graphics rendering
    BufferedImage bi = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
    //most importan variable. Everything is drawn as a result of adding things for this variable to do
    Graphics2D big;
    //the level is slightly shifted down
    static int verticalShift = 100;
    //map variable (very important)
    static Map1 map1;
    //static Enemy1 enemy1;
    //an array that holds all the different images
    static Image[] img = new Image[28]; //0-4 are towers. The rest are enemies
    //counter used for enemy spawn rate and wave completion
    int pc = 0;
    //array of custom radio buttons
    radioButtton rB[] = new radioButtton[5];
    //initialize these radio buttons
    RButton b1 = new RButton(150, 80, 50, 100, "Play");
    RButton b3 = new RButton(1020, 350, 50, 100, "Back");
    RButton b4 = new RButton(1020, 280, 50, 100, "Start Dey");
    RButton b6 = new RButton(150, 140, 50, 100, "Tutorial");
    RButton b7 = new RButton(150, 260, 50, 100, "Credits");
    RButton b8 = new RButton(150, 200, 50, 100, "High Scores");
    //variables for storing highscores along with names
    static String highName[];
    static int highScore[];
    //number of records
    static int fileSize = 0;

    public static void main(String[] args) throws InterruptedException {
        //imports the images and gifs
        img[0] = Toolkit.getDefaultToolkit().getImage("Towers\\R1GunSmall.png");
        img[1] = Toolkit.getDefaultToolkit().getImage("Towers\\R1LaserSmall.png");
        img[2] = Toolkit.getDefaultToolkit().getImage("Towers\\R1BladeSmall.png");
        img[3] = Toolkit.getDefaultToolkit().getImage("Towers\\R1MissileSmall.png");
        img[4] = Toolkit.getDefaultToolkit().getImage("Towers\\R1NukeSmall.png");
        img[5] = Toolkit.getDefaultToolkit().getImage("Enemies\\Basic1Small.png");
        img[6] = Toolkit.getDefaultToolkit().getImage("Enemies\\Basic2Small.png");
        img[7] = Toolkit.getDefaultToolkit().getImage("Enemies\\Basic3Small.png");
        img[8] = Toolkit.getDefaultToolkit().getImage("Enemies\\Medium1Small.png");
        img[9] = Toolkit.getDefaultToolkit().getImage("Enemies\\Medium2Small.png");
        img[10] = Toolkit.getDefaultToolkit().getImage("Enemies\\Medium3Small.png");
        img[11] = Toolkit.getDefaultToolkit().getImage("Enemies\\Hard1Small.png");
        img[12] = Toolkit.getDefaultToolkit().getImage("Enemies\\Hard2Small.png");
        img[13] = Toolkit.getDefaultToolkit().getImage("Enemies\\Hard3Small.png");
        img[14] = Toolkit.getDefaultToolkit().getImage("Effects\\missilesSmall.png");
        img[15] = Toolkit.getDefaultToolkit().getImage("Effects\\background.jpg");
        img[16] = Toolkit.getDefaultToolkit().getImage("Effects\\happyCutten.jpg");
        img[17] = Toolkit.getDefaultToolkit().getImage("Effects\\neutralCutten.jpg");
        img[18] = Toolkit.getDefaultToolkit().getImage("Effects\\angryCutten.jpg");
        img[19] = Toolkit.getDefaultToolkit().getImage("Effects\\crosshair.png");
        img[20] = Toolkit.getDefaultToolkit().getImage("Effects\\crosshair2.png");
        img[21] = Toolkit.getDefaultToolkit().getImage("Effects\\transparent.png");
        img[22] = Toolkit.getDefaultToolkit().getImage("Effects\\Explosion1.png");
        img[23] = Toolkit.getDefaultToolkit().getImage("Effects\\Explosion2.png");
        img[24] = Toolkit.getDefaultToolkit().getImage("Effects\\Explosion3.png");
        img[25] = Toolkit.getDefaultToolkit().getImage("Enemies\\Arnab.png");
        img[26] = Toolkit.getDefaultToolkit().getImage("Effects\\Logo.gif");
        img[27] = Toolkit.getDefaultToolkit().getImage("Effects\\Names.gif");
        readHighScores();//collect the high scores from the data file
        sortHighScores(highScore, 0, highScore.length - 1, highName);//sort the high scores
        map1 = new Map1(10, 11, 1, 50, 20, 5);//make the map object
        makeMap1();//set the coordinates of paths and keys on the map
        try {//create the music (but not play it yet)
            audioInputStream = AudioSystem.getAudioInputStream(new File("Sounds\\ballad.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);



        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
        //starts the game (I DARE YOU TO REMOVE THIS)
        CuttenDefence cD = new CuttenDefence();
    }

    public static void readHighScores() {//reads in the high scores from a data file
        try {
            FileReader fr = new FileReader("HighScores.txt");
            BufferedReader br = new BufferedReader(fr);

            fileSize = Integer.parseInt(br.readLine());
            highName = new String[fileSize];
            highScore = new int[fileSize];
            for (int i = 0; i < fileSize; i++) {
                highName[i] = br.readLine();
                highScore[i] = Integer.parseInt(br.readLine());
            }
            fr.close();
            br.close();
        } catch (IOException e) {
        }
    }

    public static void writeHighScores() {//write the new high scores to the same file that they were read from
        try {
            FileWriter fw = new FileWriter("HighScores.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fileSize + "");
            bw.newLine();
            for (int i = 0; i < fileSize; i++) {
                bw.write(highName[i]);
                bw.newLine();
                bw.write(highScore[i] + "");
                bw.newLine();
            }

            bw.close();
            fw.close();
        } catch (IOException e) {
        }

    }

    public static void sortHighScores(int[] a, int p, int r, String[] b) {//sort the high scores (uses quiksort algorithm so recursion)
        //sorts in descending order
        if (p < r) {
            int q = partition2(a, p, r, b);
            sortHighScores(a, p, q, b);
            sortHighScores(a, q + 1, r, b);
        }

    }

    private static int partition2(int[] a, int p, int r, String[] b) {//partions for quiksort, accepts both the highscores and the names of the people
        //only uses highscores for sorting and sorts names based on highscores

        int x = a[p];
        int i = p - 1;
        int j = r + 1;

        while (true) {
            i++;
            while (i < r && a[i] > x) {
                i++;
            }
            j--;
            while (j > p && a[j] < x) {
                j--;
            }

            if (i < j) {
                swap2(a, i, j, b);
            } else {
                return j;
            }
        }
    }

    private static void swap2(int[] a, int i, int j, String[] b) {//swamps stuff out of order in the arrays
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        String temp2 = b[i];
        b[i] = b[j];
        b[j] = temp2;
    }

    public CuttenDefence() throws InterruptedException {//makes a jFrame (this pretty much encompases the entire game)

        this.setTitle("Cutten Defence");//makes the title
        this.setIconImage(img[16]);//makes the icon
        this.setSize(1400, 700);//sets its initial size
        this.setLocation(20, 40);//set its location
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//allows it to close
        this.setResizable(false);//make it so you can't change the size
        this.setVisible(true);//allows you to see it        
        this.getContentPane().setLayout(new BorderLayout());//makes a border
        for (int i = 0; i < 5; i++) {//creates the radio buttons for the tower selection
            rB[i] = new radioButtton(false, 1010, (170 + 20 * i));
        }
        rB[0].setSelected(true);//make the first selected

        this.addMouseListener(new MouseAdapter() {//handles mouse related events
            public void mousePressed(MouseEvent e) {//if a mouse key was pressed in any way
                if (e.getButton() == MouseEvent.BUTTON1) {//if you click the left mouse button
                    mouse(e.getX(), e.getY());//gets its x and y position
                    if (menuSelection == -1) {//if you click anywhere it goes to the main menu
                        menuSelection = 0;
                        changeMenu = true;
                    }

                    if (menuSelection == 0) {//main menu mouse events
                        if (b1.getX() > x - b1.getWidth() && b1.getX() < x && b1.getY() > y - b1.getHeight() && b1.getY() < y) {//when you click the play button
                            //menu will dissapear and the game will now display
                            menuSelection = 1;
                            changeMenu = true;
                            //plays the song "ballad" once
                            if (playMusic) {
                                playMusic = false;
                                try {
                                    clip.close();
                                    audioInputStream = AudioSystem.getAudioInputStream(new File("Sounds\\ballad.wav").getAbsoluteFile());
                                    clip = AudioSystem.getClip();
                                    clip.open(audioInputStream);
                                    gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                                    clip.start();


                                } catch (Exception ex) {
                                    System.out.println("Error with playing sound.");
                                    ex.printStackTrace();
                                }
                            }
                        }


                        if (b6.getX() > x - b6.getWidth() && b6.getX() < x && b6.getY() > y - b6.getHeight() && b6.getY() < y) {
                            //Opens up the user manual if tutorial button is clicked
                            try {
                                String cmds[] = new String[]{"cmd", "/c",
                                    "UserManual.pdf"};
                                Runtime.getRuntime().exec(cmds);
                            } catch (IOException t) {
                            }

                        }

                        if (b7.getX() > x - b7.getWidth() && b7.getX() < x && b7.getY() > y - b7.getHeight() && b7.getY() < y) {//credits button
                            //displays CREDITS SCREEN if this button is clicked
                            JOptionPane.showMessageDialog(null, "Credits:\nProject Manager:\nRiley Freeborn\nLead Programmer:\nNikita Erokhine\nTechnical Writer & Artist:\nArnab Dey\nMajority Shareholder:\nLogan McLeod\nAdditional Thanks:\nMr. Cutten\nAdam Bibby");
                        }
                        if (b8.getX() > x - b8.getWidth() && b8.getX() < x && b8.getY() > y - b8.getHeight() && b8.getY() < y) {
                            //highscore button (displays the highscores if this button is clicked)
                            String str = "";
                            for (int i = 0; i < 10; i++) {
                                str += (i + 1) + ". " + highName[i] + ":\t" + highScore[i] + "\n";
                            }
                            JOptionPane.showMessageDialog(null, "High Scores:\n" + str);
                        }

                    } else if (menuSelection == 1) {//if a left mouse click happens in game
                        if (!first && winner) {//if you won the game
                            //asks you for your name so you can potentially be put in the highscores
                            name = JOptionPane.showInputDialog("Congratulations!\nYour highscore was " + yourScore + ".\nPlease enter your nickname.");
                            boolean in = false;//if you will be put in highscores
                            //checks if your score is higher than anybodies
                            //highscore is determined by the amount of lives that you have left
                            for (int l = 0; l < highScore.length; l++) {
                                if (yourScore > highScore[l]) {
                                    in = true;
                                }
                            }
                            //if your highscore is good enough, you are inserted as the last person in the highscores list and are then sorted and written to the file
                            if (in) {
                                try {
                                    highScore[highScore.length - 1] = yourScore;
                                    highName[highName.length - 1] = name;
                                } catch (IndexOutOfBoundsException c) {
                                    System.out.println("You know what to fix.");
                                }
                                sortHighScores(highScore, 0, highScore.length - 1, highName);
                                writeHighScores();
                            }
                            //prevents you from opening up this menu multiples times
                            first = true;
                        }
                        if (!selectedNuke) {//if the nuke is not selected, attempt to place a tower
                            map1.placeTower(x, y, towerSelection);
                        }
                        if (b3.getX() > x - b3.getWidth() && b3.getX() < x + 10 && b3.getY() > y - b3.getHeight() && b3.getY() < y + 10) {
                            //back menu button
                            //takes you back to the main menu
                            menuSelection = 0;
                            changeMenu = true;
                            //prevents glitching
                            showRange = false;
                        }

                        if (b4.getX() > x - b4.getWidth() && b4.getX() < x + 10 && b4.getY() > y - b4.getHeight() && b4.getY() < y + 10) {
                            //Start Dey button
                            //Starts a new wave
                            waveEnd = true;
                            pc = 0;
                        }

                        if (selectedNuke) {
                            //if you are in nuke mode shoot
                            //attempt to shoot a nuke
                            shoot = true;
                        }

                    }
                    //checks which radio button you pressed (if you even did) and declares it pressed while declaring the previous radio button not selected
                    for (int i = 0; i < 5; i++) {
                        if (rB[i].getX() > x - 10 && rB[i].getX() < x && rB[i].getY() > y - 10 && rB[i].getY() < y) {
                            for (int a = 0; a < 5; a++) {
                                rB[a].setSelected(false);
                            }
                            rB[i].setSelected(true);
                            towerSelection = i;
                        }
                    }
                } else if (e.getButton() == MouseEvent.BUTTON2) {//if you clicked the scroll button
                    //checks which tower you are hovering over (if any)
                    for (int x = 0; x < map1.getTowerNum(); x++) {
                        if (Math.abs(e.getX() - map1.getTower(x).getxPos()) < map1.getDimensions() && Math.abs(e.getY() - map1.getTower(x).getyPos()) < map1.getDimensions()) {
                            if (!showRange) {//if you are not displaying some tower range (to prevent glitches)
                                //sell the tower you are over
                                //you make 75% of the money you put into it back
                                //the tower is removed from the maps memory
                                map1.setMoney(map1.getMoney() + (int) map1.getTower(x).getPrice() * 3 / 4);
                                map1.removeTower(x);
                            }
                        }
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {//if your right clicked
                    //checks which tower you hovered over (if any)
                    for (int x = 0; x < map1.getTowerNum(); x++) {
                        if (Math.abs(e.getX() - map1.getTower(x).getxPos()) < map1.getDimensions() && Math.abs(e.getY() - map1.getTower(x).getyPos()) < map1.getDimensions()) {
                            if (selectedTower != x) {//if the tower is not the same one you hovered over last time you right clicked

                                if (map1.getTower(x).getTowerID() == 4) {//if hovering over the nuke tower
                                    //NUKE MODE ACTIVATED (you will have a different cursor and can blow stuff up instead of place towers)
                                    selectedNuke = true;

                                } else {
                                    //go out of nuke mode (if at all applicable)
                                    selectedNuke = false;
                                }
                                //tower selected is stored
                                selectedTower = x;
                                //you can now show the range of the tower selected
                                showRange = true;
                            } else {//if the tower is the same one you hovered over last time you right clicked
                                //deselect the tower, stop showing the towers range and go out of nuke mode (if at all applicable)
                                selectedTower = -1;
                                showRange = false;
                                selectedNuke = false;
                            }

                        }
                    }
                }
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {//if the mouse is moved, store its coordinates
            public void mouseMoved(MouseEvent e) {
                mouse(e.getX(), e.getY());
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {//if the mouse is dragged, store its coordinates
            public void mouseDragged(MouseEvent e) {
                mouse(e.getX(), e.getY());
            }
        });
        //this is what allows the game to function at all
        //updates the games graphics and runs it at about 60 frames per second (not exactly 60)
        while (true) {
            repaint();
            Thread.sleep(16);
        }
    }

    public void mouse(int x, int y) {//mouse method for getting the mouses coordinates
        this.x = x;
        this.y = y;
    }

    @Override
    public void paint(Graphics g) {//calls the update graphics method
        update(g);
    }

    @Override
    public void update(Graphics g) {//game loop

        Graphics2D g2 = (Graphics2D) g;
        if (firstTime) {
            //initializes all of the variables to do with drawing upon startup of the game
            Dimension dim = getSize();
            int w = dim.width;
            int h = dim.height;
            bi = (BufferedImage) createImage(w, h);
            big = bi.createGraphics();
            big.setStroke(new BasicStroke(8.0f));
            firstTime = false;
        }

        if (menuSelection == -1) {//INTRO SCREEN
            if (changeMenu) {//if the menuScreenb is changed
                this.setSize(1400, 700);//sets the screen size
                changeMenu = false;
            }
            big.setColor(Color.black);//draws a black background
            big.fillRect(0, 0, 1400, 700);
            big.drawImage(img[26], 300, 200, this);//cutten defence gif
            big.drawImage(img[27], 390, 400, this);//names gif
        }

        if (menuSelection == 0) {//menu screen
            //sets the size of the screen
            if (changeMenu) {
                this.setSize(400, 400);
                changeMenu = false;
            }

            //makes your mouse cursor invisible and replaces it with an image
            Cursor invisibleCursor = getToolkit().createCustomCursor(img[21], new Point(0, 0), "Invisible");
            this.setCursor(invisibleCursor);
            //creates the menu screen and all the buttons that inhabit it
            big.setStroke(new BasicStroke(2f));
            big.drawImage(img[15], 0, 0, this);
            big.setColor(Color.white);
            big.setFont(new Font("TimesRoman", Font.BOLD, 30));
            big.drawString("Cutten Defence", 100, 60);

            big.setColor(Color.black);
            big.fillRect(b1.getX(), b1.getY(), b1.getWidth(), b1.getHeight());
            big.setColor(Color.red);
            big.drawRect(b1.getX(), b1.getY(), b1.getWidth(), b1.getHeight());
            big.setColor(Color.red);
            big.setFont(new Font("TimesRoman", Font.PLAIN, 12));
            big.drawString(b1.getText(), b1.getX() + b1.getWidth() / 2 - 16, b1.getY() + b1.getHeight() / 2 + 5);


            big.setColor(Color.black);
            big.fillRect(b6.getX(), b6.getY(), b6.getWidth(), b6.getHeight());
            big.setColor(Color.red);
            big.drawRect(b6.getX(), b6.getY(), b6.getWidth(), b6.getHeight());
            big.setColor(Color.red);
            big.setFont(new Font("TimesRoman", Font.PLAIN, 12));
            big.drawString(b6.getText(), b6.getX() + b6.getWidth() / 2 - 20, b6.getY() + b6.getHeight() / 2 + 5);

            big.setColor(Color.black);
            big.fillRect(b7.getX(), b7.getY(), b7.getWidth(), b7.getHeight());
            big.setColor(Color.red);
            big.drawRect(b7.getX(), b7.getY(), b7.getWidth(), b7.getHeight());
            big.setColor(Color.red);
            big.setFont(new Font("TimesRoman", Font.PLAIN, 12));
            big.drawString(b7.getText(), b7.getX() + b7.getWidth() / 2 - 16, b7.getY() + b7.getHeight() / 2 + 5);

            big.setColor(Color.black);
            big.fillRect(b8.getX(), b8.getY(), b8.getWidth(), b8.getHeight());
            big.setColor(Color.red);
            big.drawRect(b8.getX(), b8.getY(), b8.getWidth(), b8.getHeight());
            big.setColor(Color.red);
            big.setFont(new Font("TimesRoman", Font.PLAIN, 12));
            big.drawString(b8.getText(), b8.getX() + b8.getWidth() / 2 - 30, b8.getY() + b8.getHeight() / 2 + 5);
            //end of making the menu screen
            //draws an image where the mouse should be
            big.drawImage(img[19], x - 20, y - 20, this);
        }
        if (menuSelection == 1) {//actual game screen
            //replays the song if the song is over (I kept track of the song length)
            //can't play if on the boss wave
            musicLoopCount++;
            if (musicLoopCount > 6000 && map1.getCurrentWave() != 30) {
                musicLoopCount = 0;
                try {
                    clip.close();
                    audioInputStream = AudioSystem.getAudioInputStream(new File("Sounds\\ballad.wav").getAbsoluteFile());
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    //gainControl.setValue(-25.0f); // Reduce volume by 10 decibels.
                    clip.start();


                } catch (Exception ex) {
                    System.out.println("Error with playing sound.");
                    ex.printStackTrace();
                }
            }
            //changes the menu size again
            if (changeMenu) {
                this.setSize(1200, 700);
                changeMenu = false;//game is not in menu change mode anymore (so it doesn't repeat this code)
            }
            //counter increases for enemy spawns
            pc += 1;
            if (enemyCounter == -1 && map1.getEnemyNum() == 0) {//if there are no more enemies to spawn and there are no enemies left on screen and it is not wave 30
                //a new wave is able to begin
                waveEnd = false;
                if (map1.getCurrentWave() == 30 && map1.getLives() > 0) {//if the final wave is finished and Arnab did not kill Mr. Cutten
                    //you win
                    winner = true;
                } else {
                    //next wave is initiated
                    enemyCounter = 0;
                    map1.setCurrentWave(map1.getCurrentWave() + 1);
                }
            }
            spawnWave();//spawn enemies in waves
            //sets the stroke of the brush
            big.setStroke(new BasicStroke(2f));
            big.drawImage(img[15], 0, 100, this);//background
            //checks if an enemy is at Mr. Cutten
            for (int x = 0; x < map1.getEnemyNum(); x++) {
                map1.getEnemy(x).move();
                if (map1.getEnemy(x).getxPos() > 965) {
                    if (map1.getEnemy(x).getEnemyID() == 9) {
                        //if Arnab got through, Mr. Cutten is insta killed
                        map1.setLives(0);
                    } else {
                        //if a regular enemy gets through, you lose a life
                        map1.setLives(map1.getLives() - 1);
                    }
                    //remove the successful enemies from the map
                    map1.removeEnemy(x);
                    AbstractEnemy.setNumEnemies(AbstractEnemy.getNumEnemies() - 1); //decrease number of enemies
                }

            }
            //moves all the missiles on the screen  
            for (int x = 0; x < map1.getMissileNum(); x++) {
                if (map1.getMissiles(x).move()) {
                    //if a missile hits something or is off of the map, remove the missile from the map
                    map1.removeMissiles(x);
                }
            }
            //draw the pathway in the map and outline it in red
            for (int x = 0; x < map1.getC(); x++) {
                big.setColor(Color.RED);
                big.drawRect(map1.getPathX1(x), map1.getPathY1(x), map1.getPathX2(x) - map1.getPathX1(x), map1.getPathY2(x) - map1.getPathY1(x));
            }
            for (int x = 0; x < map1.getC(); x++) {
                big.setColor(Color.BLACK);
                big.fillRect(map1.getPathX1(x), map1.getPathY1(x), map1.getPathX2(x) - map1.getPathX1(x), map1.getPathY2(x) - map1.getPathY1(x));
            }

            //draw all the images for the towers on the map based on what kind of tower they are
            for (int x = 0; x < map1.getTowerNum(); x++) {
                //for every tower, rotate the screen based on a rotation variable
                big.rotate(map1.getTower(x).getRotation(), map1.getTower(x).getxPos(), map1.getTower(x).getyPos());
                if (map1.getTower(x).getTowerID() == 0) {
                    big.drawImage(img[0], map1.getTower(x).getxPos() - map1.getDimensions(), map1.getTower(x).getyPos() - map1.getDimensions(), this);
                } else if (map1.getTower(x).getTowerID() == 1) {
                    big.drawImage(img[1], map1.getTower(x).getxPos() - map1.getDimensions(), map1.getTower(x).getyPos() - map1.getDimensions(), this);
                } else if (map1.getTower(x).getTowerID() == 2) {
                    big.drawImage(img[2], map1.getTower(x).getxPos() - map1.getDimensions(), map1.getTower(x).getyPos() - map1.getDimensions(), this);
                } else if (map1.getTower(x).getTowerID() == 3) {
                    big.drawImage(img[3], map1.getTower(x).getxPos() - map1.getDimensions(), map1.getTower(x).getyPos() - map1.getDimensions(), this);
                } else if (map1.getTower(x).getTowerID() == 4) {
                    big.drawImage(img[4], map1.getTower(x).getxPos() - map1.getDimensions(), map1.getTower(x).getyPos() - map1.getDimensions(), this);
                }
                //rotate the screen back to normal once the tower has been placed
                big.rotate(-map1.getTower(x).getRotation(), map1.getTower(x).getxPos(), map1.getTower(x).getyPos());
            }
            //draws and rotates all the enemies on the map based on what kind of enemy they are (same as above)
            for (int x = 0; x < map1.getEnemyNum(); x++) {
                big.rotate(map1.getEnemy(x).getRotation(), map1.getEnemy(x).getxPos(), map1.getEnemy(x).getyPos());
                if (map1.getEnemy(x).getEnemyID() == 0) {
                    big.drawImage(img[5], map1.getEnemy(x).getxPos() - 10, map1.getEnemy(x).getyPos() - 10, this);
                } else if (map1.getEnemy(x).getEnemyID() == 1) {
                    big.drawImage(img[6], map1.getEnemy(x).getxPos() - 10, map1.getEnemy(x).getyPos() - 10, this);
                } else if (map1.getEnemy(x).getEnemyID() == 2) {
                    big.drawImage(img[7], map1.getEnemy(x).getxPos() - 10, map1.getEnemy(x).getyPos() - 10, this);
                } else if (map1.getEnemy(x).getEnemyID() == 3) {
                    big.drawImage(img[8], map1.getEnemy(x).getxPos() - 20, map1.getEnemy(x).getyPos() - 20, this);
                } else if (map1.getEnemy(x).getEnemyID() == 4) {
                    big.drawImage(img[9], map1.getEnemy(x).getxPos() - 20, map1.getEnemy(x).getyPos() - 20, this);
                } else if (map1.getEnemy(x).getEnemyID() == 5) {
                    big.drawImage(img[10], map1.getEnemy(x).getxPos() - 20, map1.getEnemy(x).getyPos() - 20, this);
                } else if (map1.getEnemy(x).getEnemyID() == 6) {
                    big.drawImage(img[11], map1.getEnemy(x).getxPos() - 30, map1.getEnemy(x).getyPos() - 30, this);
                } else if (map1.getEnemy(x).getEnemyID() == 7) {
                    big.drawImage(img[12], map1.getEnemy(x).getxPos() - 30, map1.getEnemy(x).getyPos() - 30, this);
                } else if (map1.getEnemy(x).getEnemyID() == 8) {
                    big.drawImage(img[13], map1.getEnemy(x).getxPos() - 30, map1.getEnemy(x).getyPos() - 30, this);
                } else if (map1.getEnemy(x).getEnemyID() == 9) {
                    big.drawImage(img[25], map1.getEnemy(x).getxPos() - 50, map1.getEnemy(x).getyPos() - 50, this);
                }
                big.rotate(-map1.getEnemy(x).getRotation(), map1.getEnemy(x).getxPos(), map1.getEnemy(x).getyPos());
            }
            //draws all the missiles on the map and rotate them
            for (int x = 0; x < map1.getMissileNum(); x++) {
                big.rotate(map1.getMissiles(x).getRotation(), map1.getMissiles(x).getxPos(), map1.getMissiles(x).getyPos());
                big.drawImage(img[14], map1.getMissiles(x).getxPos() - 5, map1.getMissiles(x).getyPos() - 15, this);
                big.rotate(-map1.getMissiles(x).getRotation(), map1.getMissiles(x).getxPos(), map1.getMissiles(x).getyPos());
            }
            //draws health bars for all the enemies on the map
            for (int x = 0; x < map1.getEnemyNum(); x++) {
                big.setColor(Color.red);
                big.drawRect(map1.getEnemy(x).getxPos() - 20, map1.getEnemy(x).getyPos() - 20, 40, 1);//red part of health bar is constant
                big.setColor(Color.green);
                //green part of health bar changes based on the percentage of the enemies max health that is left
                big.drawRect(map1.getEnemy(x).getxPos() - 20, map1.getEnemy(x).getyPos() - 20, 4 * (int) (map1.getEnemy(x).getHealth() * 10 / map1.getEnemy(x).getOriginalHealth()), 1);

            }
            //draws the sidebar stuff
            big.setColor(Color.black);
            big.fillRect(1000, 0, 1400, 700);
            big.fillRect(0, 0, 1400, 100);
            big.setColor(Color.white);
            big.drawString("Money: $" + map1.getMoney(), 1010, 110);
            big.drawString("Lives: " + map1.getLives(), 1010, 130);
            big.drawString("Dey: " + map1.getCurrentWave(), 1010, 150);

            big.setColor(Color.black);
            big.fillRect(b3.getX(), b3.getY(), b3.getWidth(), b3.getHeight());
            if (waveEnd) {//changes the color of the start dey button if the wave is over
                big.setColor(Color.GRAY);
            }
            //draws buttons
            big.fillRect(b4.getX(), b4.getY(), b4.getWidth(), b4.getHeight());
            big.setColor(Color.white);
            big.setColor(Color.red);
            big.drawRect(b3.getX(), b3.getY(), b3.getWidth(), b3.getHeight());
            big.drawRect(b4.getX(), b4.getY(), b4.getWidth(), b4.getHeight());
            big.setFont(new Font("TimesRoman", Font.PLAIN, 12));
            big.drawString(b3.getText(), b3.getX() + b3.getWidth() / 2 - 16, b3.getY() + b3.getHeight() / 2 + 5);

            big.drawString(b4.getText(), b4.getX() + b4.getWidth() / 2 - 30, b4.getY() + b4.getHeight() / 2 + 5);
            //draws radio buttons
            for (int i = 0; i < 5; i++) {
                if (rB[i].isSelected()) {
                    big.setColor(Color.gray);
                } else {
                    big.setColor(Color.white);
                }
                big.fillOval(rB[i].getX(), rB[i].getY(), 10, 10);
            }
            //draws more sidebar stuff
            big.setColor(Color.white);
            big.drawString("Turret       $15", 1050, 180);
            big.drawString("Laser       $30", 1050, 200);
            big.drawString("Blade       $25", 1050, 220);
            big.drawString("Missile      $150", 1050, 240);
            big.drawString("Nuke        $200", 1050, 260);
            //draws different facial expressions for Mr. Cutten based on the number of lives you have left
            if (map1.getLives() >= 30) {//if over or equal to 30 lives. happy cutten
                big.drawImage(img[16], 1000 - 62, 620, this);
            } else if (map1.getLives() >= 10) {//if over or equal to 10 lives. neutral cutten
                big.drawImage(img[17], 1000 - 62, 620, this);
            } else if (map1.getLives() > 0) {//if over 0 lives. angry cutten
                big.drawImage(img[18], 1000 - 62, 620, this);
            } else {
                //Game over text is displayed if you have lost (Lives below 0)
                big.setColor(Color.DARK_GRAY);
                big.setFont(new Font("TimesRoman", Font.BOLD, 102));
                big.drawString("Game Over", this.getWidth() / 2 - 500 + 85, this.getHeight() / 2 + 45);
                big.setColor(Color.LIGHT_GRAY);
                big.setFont(new Font("TimesRoman", Font.BOLD, 100));
                big.drawString("Game Over", this.getWidth() / 2 - 495 + 85, this.getHeight() / 2 + 45);
                big.setFont(new Font("TimesRoman", Font.PLAIN, 12));
                //prevents you from starting a new wave
                waveEnd = true;
            }
            if (winner && map1.getLives() > 0) {
                //if you win, congratulates you with a draw message
                yourScore = map1.getLives();
                big.setColor(Color.orange);
                big.setFont(new Font("TimesRoman", Font.BOLD, 102));
                big.drawString("You Have Won", this.getWidth() / 2 - 500 - 30, this.getHeight() / 2 + 45);
                big.setColor(Color.yellow);
                big.setFont(new Font("TimesRoman", Font.BOLD, 100));
                big.drawString("You Have Won", this.getWidth() / 2 - 495 - 30, this.getHeight() / 2 + 45);
                big.setFont(new Font("TimesRoman", Font.PLAIN, 30));
                big.setColor(Color.white);
                big.drawString("Left click anywhere to continue.", this.getWidth() / 2 - 350, this.getHeight() / 2 + 100);
                big.setFont(new Font("TimesRoman", Font.PLAIN, 12));
                //prevents you from starting a new wave
                waveEnd = true;
            }

            //nuke picture explosion counter
            nukePicDuration += 1;
            if (selectedNuke == false) {//if a nuke is not selected
                //draws a custom cursor
                big.drawImage(img[19], x - 20, y - 20, this);

            } else {//if a nuke is selected
                //sets font (as you may have guessed with above code)
                big.setFont(new Font("TimesRoman", Font.PLAIN, 14));
                if (x < 1000 && y > 100) {//if you cursor is on the map
                    //draws a counter for how long you have left to wait to nuke
                    big.drawString(10 - (((TowerNuke) map1.getTower(selectedTower)).getShotSpeedCounterModified()) + "", x - 5, y + 5);
                    //if you are able to nuke (the if statement actually shoots the nuke itself if you can)
                    if ((((TowerNuke) map1.getTower(selectedTower)).shoot(shoot, this.x, y)) && shoot && nukePicDuration >= 30) {
                        //you can now begin to draw the explosion
                        nukePicDuration = 0;
                        nukeSuccess = true;
                    }
                    //if you can draw the explosion
                    if (nukePicDuration >= 0 && nukePicDuration <= 30 && nukeSuccess == true) {
                        //draw a nuke explosion
                        big.drawImage(img[23], x - 100, y - 100, this);
                    } else if (nukePicDuration > 30) {//if half a second has passed after nuking
                        //you cannot draw a nuke anymore
                        nukeSuccess = false;
                    }


                }
                //replace your cursor with a different looking one
                //custom just for nuking pleasure
                big.drawImage(img[20], x - 30, y - 30, this);
            }
            big.setColor(Color.black);
            //very important chunk of code
            //makes all towers on the map shoot if they can
            for (int x = 0; x < map1.getTowerNum(); x++) {
                if (map1.getTower(x).getTowerID() == 0) {
                    ((TowerTurret) map1.getTower(x)).shoot();
                } else if (map1.getTower(x).getTowerID() == 1) {
                    ((TowerLaser) map1.getTower(x)).shoot(big);

                } else if (map1.getTower(x).getTowerID() == 2) {
                    ((TowerBlade) map1.getTower(x)).shoot();
                } else if (map1.getTower(x).getTowerID() == 3) {
                    ((TowerMissile) map1.getTower(x)).shoot();
                } else if (map1.getTower(x).getTowerID() == 4) {
                    //nuke is different in that it does not shoot here but its shot timer increments here
                    ((TowerNuke) map1.getTower(x)).incrementCounter();
                }

            }
        }
        //if you are trying to display the range of a tower
        if (showRange) {
            //if the tower you selected is not a nuke or a missile tower (they have infinite range)
            if (map1.getTower(selectedTower).getTowerID() == 3 || map1.getTower(selectedTower).getTowerID() == 4) {
            } else {
                drawRange(selectedTower);//draw the range of the specified tower (call method)
            }
        }
        //resets the shoot variable (when trying to nuke)
        shoot = false;
        //important chunk
        //kills enemies if their health is below 0 and removes them from the map. This checks all enemies on the map
        for (int x = 0; x < map1.getEnemyNum(); x++) {
            if (map1.getEnemy(x).getHealth() <= 0) {
                map1.setMoney(map1.getMoney() + map1.getEnemy(x).getMoney());//you make money based on the value of the enemy killed
                map1.removeEnemy(x);
                AbstractEnemy.setNumEnemies(AbstractEnemy.getNumEnemies() - 1);
            }
        }
        //one of the most important lines of code. Draws everything
        g2.drawImage(bi, 0, 0, this);




    }

    public void drawRange(int t) {
        //draws a red square with 4 inward spokes over your selected tower that you right clicked on to represent its range
        big.setColor(Color.red);
        big.drawRect(map1.getTower(t).getxPos() - map1.getTower(t).getRange(), map1.getTower(t).getyPos() - map1.getTower(t).getRange(), map1.getTower(t).getRange() * 2, map1.getTower(t).getRange() * 2);
        big.drawLine(map1.getTower(t).getxPos(), map1.getTower(t).getyPos(), map1.getTower(t).getxPos() - map1.getTower(t).getRange(), map1.getTower(t).getyPos());
        big.drawLine(map1.getTower(t).getxPos(), map1.getTower(t).getyPos(), map1.getTower(t).getxPos() + map1.getTower(t).getRange(), map1.getTower(t).getyPos());
        big.drawLine(map1.getTower(t).getxPos(), map1.getTower(t).getyPos(), map1.getTower(t).getxPos(), map1.getTower(t).getyPos() + map1.getTower(t).getRange());
        big.drawLine(map1.getTower(t).getxPos(), map1.getTower(t).getyPos(), map1.getTower(t).getxPos(), map1.getTower(t).getyPos() - map1.getTower(t).getRange());
    }

    public static void makeMap1() {//creates the map's layout
        //sets the paths of the map
        map1.setPath(0, 0, 200, 100 + verticalShift, 180 + verticalShift);
        map1.setPath(1, 120, 200, 100 + verticalShift, 600 + verticalShift);
        map1.setPath(2, 120, 430, 520 + verticalShift, 600 + verticalShift);
        map1.setPath(3, 350, 430, 240 + verticalShift, 600 + verticalShift);
        map1.setPath(4, 350, 630, 240 + verticalShift, 320 + verticalShift);
        map1.setPath(5, 550, 630, 0 + verticalShift, 320 + verticalShift);
        map1.setPath(6, 550, 860, 0 + verticalShift, 80 + verticalShift);
        map1.setPath(7, 780, 860, 0 + verticalShift, 400 + verticalShift);
        map1.setPath(8, 630, 860, 320 + verticalShift, 400 + verticalShift);
        map1.setPath(9, 630, 710, 400 + verticalShift, 600 + verticalShift);
        map1.setPath(10, 630, 1000, 520 + verticalShift, 600 + verticalShift);
        //sets the keys on the map
        map1.setKey(0, 160, 140 + verticalShift, 3);
        map1.setKey(1, 160, 560 + verticalShift, 2);
        map1.setKey(2, 390, 560 + verticalShift, 1);
        map1.setKey(3, 390, 280 + verticalShift, 2);
        map1.setKey(4, 590, 280 + verticalShift, 1);
        map1.setKey(5, 590, 40 + verticalShift, 2);
        map1.setKey(6, 820, 40 + verticalShift, 3);
        map1.setKey(7, 820, 360 + verticalShift, 4);
        map1.setKey(8, 670, 360 + verticalShift, 3);
        map1.setKey(9, 670, 560 + verticalShift, 2);
    }

    public void spawnWave() {//very complex and important method
        //the next 400 or so lines are each wave (30 waves) hardcoded into the program
        if (waveEnd) {//if you clicked the start dey button
            //******** Very Important. Anything explained about the first wave can be applied to all waves after it except for the last one. ***********
            if (map1.getCurrentWave() == 1) {//if it is the first wave
                //every second, 6 enemies of the chosen type (in this case the basic enemy) are spawned
                if (pc == 60 && enemyCounter < 6 && enemyCounter >= 0) {
                    pc = 0;//resets spawn counter
                    enemyCounter += 1;//number of enemies on the map goes up
                    map1.spawnEnemy(0, 40, 240, 2);//spawns an enemy
                } else if (pc == 60) {
                    //if you are done spawning enemies, attempt to try to finish wave (first all enemies have to be cleared as seen previously in the code
                    pc = 0;
                    enemyCounter = -1;
                }


            } else if (map1.getCurrentWave() == 2) {
                //this time, an enemy spawn every 2/3s of a second (based on pc variable)
                //10 enemies are spawned this time
                if (pc == 40 && enemyCounter < 10 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(0, 40, 240, 2);
                } else if (pc == 40) {
                    pc = 0;
                    enemyCounter = -1;
                }


            } else if (map1.getCurrentWave() == 3) {

                if (pc == 20 && enemyCounter < 30 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(0, 40, 240, 2);
                } else if (pc == 40) {
                    pc = 0;
                    enemyCounter = -1;
                }
            } else if (map1.getCurrentWave() == 4) {
                if (pc == 20 && enemyCounter < 6 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(0, 40, 240, 2);
                    //in addition to the first type of enemy spawned, a new type of enemy is introduced (fast enemies) by using an else if statement
                    //these enemies will spawn after the first enemies have finished spawning (see if you can tell why)
                } else if (pc == 20 && enemyCounter < 14 && enemyCounter >= 6) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(1, 40, 240, 2);
                } else if (pc == 20) {
                    pc = 0;
                    enemyCounter = -1;
                }
                //everything after this point is self explanatory and similar to the waves described above in how they work (except the final wave)
            } else if (map1.getCurrentWave() == 5) {

                if (pc == 20 && enemyCounter < 15 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(0, 40, 240, 2);
                } else if (pc == 40 && enemyCounter < 30 && enemyCounter >= 15) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(1, 40, 240, 2);
                } else if (pc == 20) {
                    pc = 0;
                    enemyCounter = -1;
                }
            } else if (map1.getCurrentWave() == 6) {

                if (pc == 60 && enemyCounter < 35 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(1, 40, 240, 2);
                } else if (pc == 60) {
                    pc = 0;
                    enemyCounter = -1;
                }
            } else if (map1.getCurrentWave() == 7) {
                if (pc == 30 && enemyCounter < 7 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(2, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }
            } else if (map1.getCurrentWave() == 8) {
                if (pc == 30 && enemyCounter < 10 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(2, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 22 && enemyCounter >= 12) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(1, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 32 && enemyCounter >= 22) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(0, 40, 240, 2);

                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }
            } else if (map1.getCurrentWave() == 9) {
                if (pc == 30 && enemyCounter < 15 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(1, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }
            } else if (map1.getCurrentWave() == 10) {
                if (pc == 30 && enemyCounter < 10 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(0, 40, 240, 2);
                } else if (pc == 40 && enemyCounter < 16 && enemyCounter >= 10) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(3, 40, 240, 2);
                } else if (pc == 40) {
                    pc = 0;
                    enemyCounter = -1;
                }
            } else if (map1.getCurrentWave() == 11) {
                if (pc == 30 && enemyCounter < 12 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(3, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }
            } else if (map1.getCurrentWave() == 12) {
                if (pc == 30 && enemyCounter < 20 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(3, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 13) {

                if (pc == 30 && enemyCounter < 10 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(1, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 16 && enemyCounter >= 10) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(4, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 14) {
                if (pc == 30 && enemyCounter < 10 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(3, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 22 && enemyCounter >= 10) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(4, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 15) {
                if (pc == 30 && enemyCounter < 12 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(1, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 22 && enemyCounter >= 12) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(4, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 16) {
                if (pc == 30 && enemyCounter < 10 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(2, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 16 && enemyCounter >= 10) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(5, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 17) {
                if (pc == 30 && enemyCounter < 10 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(3, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 20 && enemyCounter >= 10) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(4, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 32 && enemyCounter >= 20) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(5, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 18) {
                if (pc == 30 && enemyCounter < 15 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(3, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 30 && enemyCounter >= 15) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(4, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 48 && enemyCounter >= 30) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(5, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 19) {
                if (pc == 30 && enemyCounter < 8 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(3, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 16 && enemyCounter >= 10) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(6, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 20) {
                if (pc == 30 && enemyCounter < 12 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(3, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 27 && enemyCounter >= 12) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(6, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 21) {
                if (pc == 30 && enemyCounter < 25 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(6, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 22) {
                if (pc == 30 && enemyCounter < 8 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(4, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 14 && enemyCounter >= 8) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(7, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 23) {
                if (pc == 30 && enemyCounter < 15 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(4, 40, 240, 2);

                } else if (pc == 30 && enemyCounter < 30 && enemyCounter >= 15) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(6, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 50 && enemyCounter >= 30) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(7, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 24) {
                if (pc == 30 && enemyCounter < 20 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(6, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 50 && enemyCounter >= 20) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(7, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 25) {
                if (pc == 30 && enemyCounter < 10 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(5, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 20 && enemyCounter >= 10) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(8, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 26) {
                if (pc == 30 && enemyCounter < 15 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(6, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 35 && enemyCounter >= 15) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(7, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 50 && enemyCounter >= 35) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(8, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 27) {
                if (pc == 30 && enemyCounter < 20 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(6, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 40 && enemyCounter >= 20) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(7, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 60 && enemyCounter >= 40) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(8, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 28) {
                if (pc == 30 && enemyCounter < 10 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(0, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 20 && enemyCounter >= 10) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(1, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 30 && enemyCounter >= 20) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(2, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 45 && enemyCounter >= 30) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(3, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 60 && enemyCounter >= 45) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(4, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 75 && enemyCounter >= 60) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(5, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 95 && enemyCounter >= 75) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(6, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 115 && enemyCounter >= 95) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(7, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 135 && enemyCounter >= 115) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(8, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 29) {
                if (pc == 30 && enemyCounter < 10 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(0, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 20 && enemyCounter >= 10) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(1, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 30 && enemyCounter >= 20) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(2, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 45 && enemyCounter >= 30) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(3, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 60 && enemyCounter >= 45) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(4, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 75 && enemyCounter >= 60) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(5, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 110 && enemyCounter >= 75) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(6, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 145 && enemyCounter >= 110) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(7, 40, 240, 2);
                } else if (pc == 30 && enemyCounter < 180 && enemyCounter >= 145) {
                    pc = 0;
                    enemyCounter += 1;
                    map1.spawnEnemy(8, 40, 240, 2);
                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;
                }

            } else if (map1.getCurrentWave() == 30) {//after this wave, no more waves will happen (checked earlier in the code)
                //only 1 enemy is spawned in this wave
                if (pc == 30 && enemyCounter < 1 && enemyCounter >= 0) {
                    pc = 0;
                    enemyCounter += 1;

                    map1.spawnEnemy(9, 40, 240, 2);
                    //play boss music
                    try {
                        clip.close();
                        audioInputStream = AudioSystem.getAudioInputStream(new File("Sounds\\Champion.wav").getAbsoluteFile());
                        clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        clip.start();


                    } catch (Exception ex) {
                        System.out.println("Error with playing sound.");
                        ex.printStackTrace();
                    }


                } else if (pc == 30) {
                    pc = 0;
                    enemyCounter = -1;

                }
            }
        }
        if (pc == 0) {//important code. Every time an enemy is spawned, the java garbage collector frees up RAM by deleting
            //unused spots in memory allocated to this game.
            System.gc();
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package summative;

/**
 *
 * @author Sei
 */
/**
 * HANDLES ONLY THE MAP, CURRENT LOCATION OF THE PLAYER, GENERATION OF PEACHES
 *
 */
import processing.core.*;

public class Map {

    private final int MAP_SIZE = 2; // 2 by 2 map size

    private final Minigame[][] minigames = new Minigame[MAP_SIZE][MAP_SIZE]; // holds all minigames by section
    public String[][] worldMap = new String[MAP_SIZE][MAP_SIZE]; // stores section names
    private final PImage[][] sectionBackground = new PImage[MAP_SIZE][MAP_SIZE]; // holds background images

    public int playerX = 1, playerY = 0; // current map section location of player
    private PApplet app; // reference to the sketch
    private Player user; // reference to the player

    /**
     * constructor for map sets player start and loads minigames
     *
     * @param app reference to sketch
     * @param user the player object
     */
    public Map(PApplet app, Player user) {
        String savedMapX = SaveFile.getValue("mapX"); // get saved x
        String savedMapY = SaveFile.getValue("mapY"); // get saved y

        if (!savedMapX.isEmpty() && !savedMapY.isEmpty()) { // if both saved
            playerX = Integer.parseInt(savedMapX); // set x
            playerY = Integer.parseInt(savedMapY); // set y
        }

        this.app = app; // store app
        this.user = user; // store user
        generation(); // build map
    }

    /**
     * returns minigame in current section
     *
     * @return current minigame or null
     */
    public Minigame getCurrentMinigame() {
        return minigames[playerX][playerY]; // return minigame at location
    }

    /**
     * builds the map and assigns backgrounds and minigames
     */
    private void generation() {
        for (int i = 0; i < MAP_SIZE; i++) { // loop through sections
            for (int j = 0; j < MAP_SIZE; j++) {
                worldMap[i][j] = "Section (" + i + "," + j + ")"; // label each
            }
        }

        minigames[0][0] = new WheatMinigame(app, user, 180, 140, 285); // top left
        sectionBackground[0][0] = app.loadImage("images/backgrounds/wheatfield.png");

        minigames[0][1] = new FriedRiceMinigame(app, user, 280, 420, 90); // top right
        sectionBackground[0][1] = app.loadImage("images/backgrounds/house.png");

        minigames[1][0] = new DressMinigame(app, user, 275, 300, 90); // bottom left
        sectionBackground[1][0] = app.loadImage("images/backgrounds/table.png");

        minigames[1][1] = new Feathers(app, user, 280, 360, 90, this); // bottom right
        sectionBackground[1][1] = app.loadImage("images/backgrounds/nighttimebridge.png");
    }

    /**
     * moves player to new section if inside bounds
     *
     * @param dx movement in x direction
     * @param dy movement in y direction
     */
    public void moveSection(int dx, int dy) {
        int nextX = playerX + dx;
        int nextY = playerY + dy;

        if (nextX >= 0 && nextX < MAP_SIZE && nextY >= 0 && nextY < MAP_SIZE) {
            playerX = nextX;
            playerY = nextY;

            SaveFile.setValue("mapX", String.valueOf(nextX)); // save position
            SaveFile.setValue("mapY", String.valueOf(nextY));
        }
    }

    /**
     * moves player or switches section based on screen bounds
     *
     * @param dx player movement in x
     * @param dy player movement in y
     * @param dir direction for sprite facing
     */
    public void moveOrBlock(int dx, int dy, String dir) {
        int nextX = user.getX() + dx;

        if (dx != 0) {
            if (minigames[playerX][playerY] == null || (minigames[playerX][playerY] != null && !minigames[playerX][playerY].isStarted())) {
                if (nextX < 0 && playerX > 0) {
                    moveSection(-1, 0); // go left
                    user.moveTo(650 - Player.SPRITE_WIDTH, user.getY(), dir); // wrap to right side
                } else if (nextX + Player.SPRITE_WIDTH > 650 && playerX < (MAP_SIZE - 1)) {
                    moveSection(1, 0); // go right
                    user.moveTo(0, user.getY(), dir); // wrap to left
                } else if (nextX >= 0 && nextX + Player.SPRITE_WIDTH <= 650) {
                    user.move(dx, 0, dir); // move normally
                }
            } else {
                if (nextX >= 0 && nextX + Player.SPRITE_WIDTH <= 650) {
                    user.move(dx, 0, dir); // move during minigame
                }
            }
        }

        int nextY = user.getY() + dy;
        if (dy != 0) {
            if (minigames[playerX][playerY] == null || (minigames[playerX][playerY] != null && !minigames[playerX][playerY].isStarted())) {
                if (nextY < 0 && playerY > 0) {
                    moveSection(0, -1); // go up
                    user.moveTo(user.getX(), 650 - Player.SPRITE_HEIGHT, dir); // wrap to bottom
                } else if (nextY + Player.SPRITE_HEIGHT > 650 && playerY < (MAP_SIZE - 1)) {
                    moveSection(0, 1); // go down
                    user.moveTo(user.getX(), 0, dir); // wrap to top
                } else if (nextY >= 0 && nextY + Player.SPRITE_HEIGHT <= 650) {
                    user.move(0, dy, dir); // normal move
                }
            } else {
                if (nextY >= 0 && nextY + Player.SPRITE_HEIGHT <= 650) {
                    user.move(0, dy, dir); // move during minigame
                }
            }
        }
    }

    /**
     * draws the map background and feathers if no minigame is active
     */
    public void draw() {
        PImage background = sectionBackground[playerX][playerY]; // get current bg
        if (background != null) {
            app.image(sectionBackground[playerX][playerY], 0, 0); // draw bg
        }

        if (minigames[playerX][playerY] == null || !minigames[playerX][playerY].started) {
            minigames[1][1].draw(); // always draw feathers
        }
    }
}

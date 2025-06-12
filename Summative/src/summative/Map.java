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
import processing.core.PApplet;
import processing.core.PImage;

public class Map {

    private final int MAP_SIZE = 2;

    private Minigame[][] minigames = new Minigame[MAP_SIZE][MAP_SIZE];
    public String[][] worldMap = new String[MAP_SIZE][MAP_SIZE];
    private PImage[][] sectionBackground = new PImage[MAP_SIZE][MAP_SIZE];

    public int playerX = 0, playerY = 0;
    private PApplet app;
    private Player user;

    public Map(PApplet app, Player user) {
        this.app = app;
        this.user = user;
        generation();
    }

    public Minigame getCurrentMinigame() {
        return minigames[playerX][playerY];
    }

    private void generation() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                worldMap[i][j] = "Section (" + i + "," + j + ")";
            }
        }

        minigames[0][0] = new WheatMinigame(app, user, 290, 230, 75);
        sectionBackground[0][0] = app.loadImage("images/backgrounds/wheatfield.png");
    }

    public void moveSection(int dx, int dy) {
        int nextX = playerX + dx;
        int nextY = playerY + dy;
        if (nextX >= 0 && nextX < MAP_SIZE && nextY >= 0 && nextY < MAP_SIZE) {
            playerX = nextX;
            playerY = nextY;
        }
    }

    public void moveOrBlock(Player user, int dx, int dy, String dir) {
        int nextX = user.getX() + dx;

        if (dx != 0) {
            if (minigames[playerX][playerY] == null || (minigames[playerX][playerY] != null && !minigames[playerX][playerY].isStarted())) {
                if (nextX < 0 && playerX > 0) {
                    moveSection(-1, 0);
                    user.moveTo(650 - Player.SPRITE_WIDTH, user.getY(), dir);
                } else if (nextX + Player.SPRITE_WIDTH > 650 && playerX < (MAP_SIZE - 1)) {
                    moveSection(1, 0);
                    user.moveTo(0, user.getY(), dir);
                } else if (nextX >= 0 && nextX + Player.SPRITE_WIDTH <= 650) {
                    user.move(dx, 0, dir);
                }
            } else {
                if (nextX >= 0 && nextX + Player.SPRITE_WIDTH <= 650) {
                    user.move(dx, 0, dir);
                }
            }
        }

        int nextY = user.getY() + dy;
        if (dy != 0) {
            if (minigames[playerX][playerY] == null || (minigames[playerX][playerY] != null && !minigames[playerX][playerY].isStarted())) {
                if (nextY < 0 && playerY > 0) {
                    moveSection(0, -1);
                    user.moveTo(user.getX(), 650 - Player.SPRITE_HEIGHT, dir);
                } else if (nextY + Player.SPRITE_HEIGHT > 650 && playerY < (MAP_SIZE - 1)) {
                    moveSection(0, 1);
                    user.moveTo(user.getX(), 0, dir);
                } else if (nextY >= 0 && nextY + Player.SPRITE_HEIGHT <= 650) {
                    user.move(0, dy, dir);
                }
            } else {
                if (nextY >= 0 && nextY + Player.SPRITE_HEIGHT <= 650) {
                    user.move(0, dy, dir);
                }
            }
        }
    }

    public void draw() {
        PImage background = sectionBackground[playerX][playerY];
        if (background != null) {
            app.image(sectionBackground[playerX][playerY], 0, 0);
        }

        // dev data
        app.fill(0);
        app.textSize(16);
        app.text("Section: (" + playerX + ", " + playerY + ")", 20, 20);
        app.text("Data: " + worldMap[playerX][playerY], 20, 40);
    }
}

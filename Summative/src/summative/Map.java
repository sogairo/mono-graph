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
    
    public String[][] worldMap = new String[MAP_SIZE][MAP_SIZE];
    public int playerX = 1, playerY = 1;
    private PApplet app;

    public Map(PApplet app) {
        this.app = app;
        generation();
    }

    private void generation() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                worldMap[i][j] = "Section (" + i + "," + j + ")";
            }
        }
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
        int nextY = user.getY() + dy;

        boolean moved = false;

        if (dx != 0) {
            if (nextX < 0 && playerX > 0) {
                moveSection(-1, 0);
                user.moveTo(650 - Player.SPRITE_WIDTH, user.getY(), dir);
                moved = true;
            } else if (nextX + Player.SPRITE_WIDTH > 650 && playerX < (MAP_SIZE - 1)) {
                moveSection(1, 0);
                user.moveTo(0, user.getY(), dir);
                moved = true;
            } else if (nextX >= 0 && nextX + Player.SPRITE_WIDTH <= 650) {
                user.move(dx, 0, dir);
                moved = true;
            }
        }

        nextY = user.getY() + dy;
        if (dy != 0) {
            if (nextY < 0 && playerY > 0) {
                moveSection(0, -1);
                user.moveTo(user.getX(), 650 - Player.SPRITE_HEIGHT, dir);
            } else if (nextY + Player.SPRITE_HEIGHT > 650 && playerY < (MAP_SIZE - 1)) {
                moveSection(0, 1);
                user.moveTo(user.getX(), 0, dir);
            } else if (nextY >= 0 && nextY + Player.SPRITE_HEIGHT <= 650) {
                user.move(0, dy, dir);
            }
        }
    }

    public void draw() {
        app.fill(0);
        app.textSize(16);
        app.text("Section: (" + playerX + ", " + playerY + ")", 20, 20);
        app.text("Data: " + worldMap[playerX][playerY], 20, 40);
    }
}

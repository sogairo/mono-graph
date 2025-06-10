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
import java.util.ArrayList;
import java.util.Random;

public class Map {

    public String[][] worldMap = new String[3][3];
    public int playerX = 1, playerY = 1;
    private PApplet app;
    private ArrayList<Tree>[][] treeMap;

    public Map(PApplet app) {
        this.app = app;
        generation();
    }

    private void generation() {
        treeMap = new ArrayList[3][3];
        Random rand = new Random();
        boolean divinePlaced = false;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                worldMap[i][j] = "Section (" + i + "," + j + ")";
                treeMap[i][j] = new ArrayList<>();
                int treeCount = 1 + rand.nextInt(2);
                for (int t = 0; t < treeCount; t++) {
                    int tx, ty;
                    boolean valid;
                    do {
                        tx = (int) app.random(0, 530);
                        ty = (int) app.random(0, 530);
                        valid = true;
                        for (Tree other : treeMap[i][j]) {
                            if (Math.abs(tx - other.x) < 250 && Math.abs(ty - other.y) < 250) {
                                valid = false;
                                break;
                            }
                        }
                    } while (!valid);
                    int rarity;
                    if (!divinePlaced && i == 2 && j == 2 && t == treeCount - 1) {
                        rarity = 3;
                        divinePlaced = true;
                    } else {
                        rarity = rand.nextInt(2) + 1;
                    }
                    treeMap[i][j].add(new Tree(app, tx, ty, rarity));
                }
            }
        }
    }

    public void moveSection(int dx, int dy) {
        int nextX = playerX + dx;
        int nextY = playerY + dy;
        if (nextX >= 0 && nextX < 3 && nextY >= 0 && nextY < 3) {
            playerX = nextX;
            playerY = nextY;
        }
    }

    public void moveOrBlock(Player user, int dx, int dy, String dir) {
        int nextX = user.getX() + dx * user.getSpeed();
        int nextY = user.getY() + dy * user.getSpeed();

        if (dx != 0) {
            if (nextX < 0 && playerX > 0) {
                moveSection(-1, 0);
                user.moveTo(650 - Player.SPRITE_WIDTH, user.getY(), dir);
            } else if (nextX + Player.SPRITE_WIDTH > 650 && playerX < 2) {
                moveSection(1, 0);
                user.moveTo(0, user.getY(), dir);
            } else if (nextX >= 0 && nextX + Player.SPRITE_WIDTH <= 650) {
                user.move(dx, 0, dir);
            }
        }

        if (dy != 0) {
            if (nextY < 0 && playerY > 0) {
                moveSection(0, -1);
                user.moveTo(user.getX(), 650 - Player.SPRITE_HEIGHT, dir);
            } else if (nextY + Player.SPRITE_HEIGHT > 650 && playerY < 2) {
                moveSection(0, 1);
                user.moveTo(user.getX(), 0, dir);
            } else if (nextY >= 0 && nextY + Player.SPRITE_HEIGHT <= 650) {
                user.move(0, dy, dir);
            }
        }

        for (Tree tree : treeMap[playerX][playerY]) {
            tree.checkCollision(user);
        }
    }

    public void draw() {
        app.fill(0);
        app.textSize(16);
        app.text("Section: (" + playerX + ", " + playerY + ")", 20, 20);
        app.text("Data: " + worldMap[playerX][playerY], 20, 40);

        for (Tree tree : treeMap[playerX][playerY]) {
            tree.draw(app);
        }
    }
}

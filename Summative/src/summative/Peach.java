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
 * HANDLES COLLISION DETECTION, PEACH TYPES (IN TERMS OF POINTS), IMAGE GEN FOR
 * PEACHES
 *
 */
import processing.core.PApplet;
import processing.core.PImage;

public class Peach {

    private int x, y;
    private int rarity; // 1 = common, 2 = rare, 3 = divine
    private boolean eaten = false;
    private PApplet app;
    private PImage image;

    public Peach(PApplet app, int x, int y, int rarity) {
        this.app = app;
        this.x = x;
        this.y = y;
        this.rarity = rarity;
        loadImage();
    }

    private void loadImage() {
        switch (rarity) {
            case 1 ->
                image = app.loadImage("images/common_peach.png");
            case 2 ->
                image = app.loadImage("images/rare_peach.png");
            case 3 ->
                image = app.loadImage("images/divine_peach.png");
        }
    }

    public void draw() {
        if (eaten) {
            return;
        }
        app.image(image, x, y, 55, 55);
    }

    public boolean checkCollision(int px, int py) {
//        if (!eaten && px + Player.SPRITE_WIDTH > x && px < x + 55 && py + Player.SPRITE_HEIGHT > y && py < y + 55) {
//            eaten = true;
//            return true;
//        }
        return false;
    }

    public int getRarity() {
        return rarity;
    }

    public boolean isEaten() {
        return eaten;
    }
}

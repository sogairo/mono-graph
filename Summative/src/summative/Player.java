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
 * HANDLES PLAYER MOVEMENT, COLLSION DETECTION
 *
 */
import processing.core.PApplet;
import processing.core.PImage;

public class Player {

    private int x = 240, y = 240;
    private String orientation;
    private int speed;

    private PApplet app;
    private PImage image;

    public static final int SPRITE_WIDTH = 100;
    public static final int SPRITE_HEIGHT = 100;

    public Player(PApplet app, int speed) {
        this.app = app;
        this.speed = speed;
        image = app.loadImage("images/temp.png");
    }

    public void move(int dx, int dy, String direction) {
        x += dx * speed;
        y += dy * speed;
        orientation = direction;
    }

    public void moveTo(int newX, int newY, String direction) {
        x = newX;
        y = newY;
        orientation = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public void draw() {
        app.image(image, x, y);
    }
}

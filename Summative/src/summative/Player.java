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

    public int x = 240, y = 240; // player position on screen
    private String orientation = "down"; // direction the sprite is facing
    private int speed; // how fast the player moves

    private PApplet app; // reference to main sketch
    private PImage spriteSheet; // image of all sprite frames
    private boolean pauseUpdates = false; // whether movement is paused

    private int frameIndex = 0; // which animation frame to draw
    private int frameCount = 4; // total frames per direction
    private int frameTimer = 0; // delay tracker
    private int frameDelay = 12; // delay between animation frames

    public static final int SPRITE_WIDTH = 50; // width of one frame
    public static final int SPRITE_HEIGHT = 50; // height of one frame

    private boolean isMoving = false; // true when player is walking

    /**
     * creates player using saved data if available and loads sprite image
     *
     * @param app reference to sketch
     * @param speed movement speed
     */
    public Player(PApplet app, int speed) {
        String savedX = SaveFile.getValue("playerX"); // get saved x
        String savedY = SaveFile.getValue("playerY"); // get saved y
        String savedDir = SaveFile.getValue("playerDirection"); // get saved direction

        if (!savedX.isEmpty() && !savedY.isEmpty()) { // if position saved
            this.x = Integer.parseInt(savedX); // set x
            this.y = Integer.parseInt(savedY); // set y
        }

        if (!savedDir.isEmpty()) { // if direction saved
            this.orientation = savedDir; // set direction
        }

        this.app = app; // store reference
        this.speed = speed; // store speed
        spriteSheet = app.loadImage("images/sprite.png"); // load spritesheet
    }

    /**
     * moves player by speed in a direction and animates frames
     *
     * @param dx x movement direction
     * @param dy y movement direction
     * @param direction the direction to face
     */
    public void move(int dx, int dy, String direction) {
        if (!pauseUpdates) { // only move if not paused
            if (dx == 0 && dy == 0) { // if no input
                isMoving = false; // stop animation
                frameIndex = 0; // reset frame
            } else {
                x += dx * speed; // move x
                y += dy * speed; // move y
                orientation = direction; // set direction
                isMoving = true; // flag as moving

                frameTimer++; // advance animation timer
                if (frameTimer >= frameDelay) { // if time to switch frame
                    frameIndex = (frameIndex + 1) % frameCount; // next frame
                    frameTimer = 0; // reset timer
                }
            }
        }
    }

    /**
     * teleports player to given location without animation
     *
     * @param newX new x coordinate
     * @param newY new y coordinate
     * @param direction direction to face
     */
    public void moveTo(int newX, int newY, String direction) {
        if (!pauseUpdates) {
            x = newX; // set x
            y = newY; // set y
            orientation = direction; // set direction
        }
    }

    /**
     * toggles whether the player can update or move
     */
    public void toggleUpdates() {
        pauseUpdates = !pauseUpdates; // flip boolean
    }

    /**
     * gets current x position
     *
     * @return player x
     */
    public int getX() {
        return x;
    }

    /**
     * gets current y position
     *
     * @return player y
     */
    public int getY() {
        return y;
    }

    /**
     * gets current speed of the player
     *
     * @return player speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * draws the player sprite with animation and saves position
     */
    public void draw() {
        if (!pauseUpdates) {
            int row = 0; // default frame row
            switch (orientation) { // pick row based on direction
                case "down":
                    row = 0;
                    break;
                case "left":
                    row = 1;
                    break;
                case "right":
                    row = 2;
                    break;
                case "up":
                    row = 3;
                    break;
            }

            if (!isMoving) { // if not moving
                frameIndex = 0; // use idle frame
            }

            PImage frame = spriteSheet.get(frameIndex * SPRITE_WIDTH, row * SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT); // get sprite frame
            app.image(frame, x, y); // draw it

            SaveFile.setValue("playerX", String.valueOf(x)); // save x
            SaveFile.setValue("playerY", String.valueOf(y)); // save y
            SaveFile.setValue("playerDirection", orientation); // save direction
        }
    }
}

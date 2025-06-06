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
    private int x, y;
    private String orientation;
    
    private PApplet app;
    
    public Player(PApplet app) {
        this.app = app;
    }
    
    public void moveTo(int dx, int dy, String direction) {
        x = dx;
        y = dy;
    }
    
    public void move(int dx, int dy, String direction) {
        x += dx;
        y += dy;
    }
    
    public void draw() {
        // draw player in the correct orientation here
    }
}

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
    public String[][] worldMap = new String[3][3]; 
    public int playerX, playerY = 2; // start player in middle of map
    private PApplet app;
    
    public Map(PApplet app) {
        this.app = app;
        generation(); // generate tree layout, map, etc...
    }
    
    private void generation() {
        
    }

    public void draw() {
        // draw the map
    }
}

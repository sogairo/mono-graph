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
    private PApplet app;
    private int sectionX = 1;
    private int sectionY = 1;
    private final int maxSections = 3;

    public Map(PApplet app) {
        this.app = app;
    }

    public void draw() {
        app.fill(0);
        app.textAlign(app.LEFT);
        app.textSize(16);
        app.text("Section: (" + sectionX + ", " + sectionY + ")", 20, 20);
    }

    public void moveSection(int dx, int dy) {
        int newX = sectionX + dx;
        int newY = sectionY + dy;

        if (newX >= 0 && newX < maxSections && newY >= 0 && newY < maxSections) {
            sectionX = newX;
            sectionY = newY;
        }
    }

    public int getSectionX() {
        return sectionX;
    }

    public int getSectionY() {
        return sectionY;
    }
}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package summative;

/**
 *
 * @author Sei
 */
import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dialogue {

    private PApplet app; // holds reference to the main app
    private ArrayList<String> lines; // holds the dialogue lines
    private int currentLineIndex; // tracks which line is being shown
    private boolean active; // checks if dialogue is currently active
    private Player user; // reference to player for toggling movement

    private boolean toggleExit = true; // controls if dialogue ends on last line

    
    /**
     * creates a dialogue object that loads lines from a file
     * sets toggleExit to true by default to allow ending dialogue
     * 
     * @param app the main game sketch
     * @param filePath the file path to the dialogue text file
     * @param user the player that will be paused during dialogue
     */
    public Dialogue(PApplet app, String filePath, Player user) {
        this.app = app; // store app
        this.lines = new ArrayList<>(); // prepare list
        this.currentLineIndex = 0; // start from first line
        this.user = user; // store player
        active = false; // not active yet
        loadDialogue(filePath); // load dialogue file
    }

    /**
     * creates a dialogue object with manual toggleExit option
     * if toggleExit is false the dialogue won't auto exit at the end
     * 
     * @param app the main game sketch
     * @param filePath the file path to the dialogue text file
     * @param user the player that will be paused during dialogue
     * @param toggleExit whether or not dialogue should close itself at the end
     */
    public Dialogue(PApplet app, String filePath, Player user, boolean toggleExit) {
        this.app = app; // store app
        this.lines = new ArrayList<>(); // prepare list
        this.currentLineIndex = 0; // start from first line
        this.user = user; // store player
        this.toggleExit = false; // disables ending toggle
        active = false; // not active yet
        loadDialogue(filePath); // load dialogue file
    }

    /**
     * reads all lines from a text file and adds them to the dialogue list
     * 
     * @param filePath the file to load dialogue lines from
     */
    private void loadDialogue(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) { // try opening file
            while (scanner.hasNextLine()) { // while file has next line
                lines.add(scanner.nextLine()); // add line to list
            }
        } catch (FileNotFoundException e) { // if file not found
            System.out.println("Error reading dialogue file: " + e.getMessage()); // show error
        }
    }

    /**
     * starts the dialogue if there are lines to show
     * pauses the player by toggling its updates
     */
    public void startDialogue() {
        if (!lines.isEmpty()) { // if there is something to show
            active = true; // turn dialogue on
            currentLineIndex = 0; // reset to first line
            user.toggleUpdates(); // stop player input
        }
    }

    /**
     * draws the current line of dialogue and optional background image
     * also shows a 'click to continue' prompt
     */
    public void draw() {
        if (active && currentLineIndex < lines.size()) { // only draw if active
            String line = lines.get(currentLineIndex); // get current line
            String[] parts = line.split("\\|"); // split into parts

            // Draw image centered if available
            if (parts.length > 2 && parts[2] != null) { // check if background exists
                PImage img = app.loadImage("images/backgrounds/" + parts[2].trim()); // load it
                if (img != null) { // if load worked
                    app.imageMode(PApplet.CENTER); // set mode to center
                    app.image(img, app.width / 2, app.height / 2, 650, 650); // draw background
                    app.imageMode(PApplet.CORNER); // reset mode
                }
            }

            // Draw dialogue box and text on top
            app.fill(0); // set text color
            app.textSize(Integer.parseInt(parts[1].trim())); // set font size
            app.textAlign(PApplet.CENTER, PApplet.TOP); // center align
            app.text(parts[0].trim(), 80, 270, 500, 999); // draw text

            if (toggleExit) { // if toggle is on
                app.textSize(20); // set prompt size
                app.text("Click to continue...", app.width - 100, app.height - 30); // draw continue hint
            } else if (!((currentLineIndex + 1) >= lines.size())) { // if more lines left
                app.textSize(20); // set prompt size
                app.text("Click to continue...", app.width - 100, app.height - 30); // draw hint
            }

            app.textAlign(PApplet.CORNER, PApplet.CORNER); // reset alignment
        }
    }

    /**
     * moves to the next line of dialogue
     * ends dialogue if finished and toggleExit is true
     */
    public void advance() {
        if (active) { // only run if active
            if (toggleExit) { // if toggle enabled
                currentLineIndex++; // next line
                if (currentLineIndex >= lines.size()) { // if no more lines
                    active = false; // turn off dialogue
                    user.toggleUpdates(); // resume player input
                }
            } else if (!((currentLineIndex + 1) >= lines.size())) { // if more lines remain
                currentLineIndex++; // go to next line
            }
        }
    }

    /**
     * checks if the dialogue is currently being shown
     * 
     * @return true if dialogue is showing otherwise false
     */
    public boolean isActive() {
        return active; // return if dialogue is active
    }
}

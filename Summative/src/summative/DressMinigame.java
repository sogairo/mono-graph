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
import processing.core.PVector;

import java.util.ArrayList;

public class DressMinigame extends Minigame {

    private PImage dressImg; // image of the dress
    private PImage[] tearImgs = new PImage[5]; // images of tears on dress
    private ArrayList<PVector> tearPositions = new ArrayList<>(); // positions of each tear
    private ArrayList<Boolean> torn = new ArrayList<>(); // tracks which tears were removed
    private boolean showPopup = false; // true when player finishes minigame
    private final int MAX_TEARS = 5; // max number of tears that can spawn
    private final int SIZE = 40; // size of tear images

    /**
     * creates the dress minigame object and loads dress assets also checks if
     * minigame was completed before
     *
     * @param app the main sketch
     * @param user the player
     * @param triggerX x coordinate of trigger area
     * @param triggerY y coordinate of trigger area
     * @param triggerSize size of the trigger zone
     */
    public DressMinigame(PApplet app, Player user, int triggerX, int triggerY, int triggerSize) {
        super(app, user, triggerX, triggerY, triggerSize); // setup parent minigame
        dressImg = app.loadImage("images/items/dress.png"); // load main dress image

        for (int i = 0; i < tearImgs.length; i++) { // load each tear image
            tearImgs[i] = app.loadImage("images/items/tears/tear" + (i + 1) + ".png"); // load each tear
        }

        if (SaveFile.getValue("dress").equals("true")) { // if minigame already completed
            completed = true; // mark completed
            completedMinigames++; // increase finished count
        }
    }

    /**
     * starts the dress minigame if not already completed generates tear
     * positions randomly on dress
     */
    @Override
    public void startMinigame() {
        if (completed) { // don't restart if already finished
            return;
        }
        started = true; // mark started
        tearPositions.clear(); // clear old tear data
        torn.clear(); // reset which ones are fixed

        int count = (int) app.random(2, MAX_TEARS + 1); // pick how many tears to make
        for (int i = 0; i < count; i++) { // generate each tear
            float x = app.random(260, 355); // pick random x
            float y = app.random(200, 435); // pick random y
            tearPositions.add(new PVector(x, y)); // store the position
            torn.add(false); // not yet fixed
        }

        user.toggleUpdates(); // disable player movement
    }

    /**
     * draws the dress minigame with tears or popup if completed
     */
    @Override
    public void draw() {
        if (completed) { // don't draw if done
            return;
        }

        if (!started) { // draw interact prompt if nearby
            if (super.inProximity()) {
                app.fill(255); // white text
                app.textSize(16); // small font
                app.text("[E] Interact", user.x - 15, user.y); // prompt
            }
            return;
        }

        app.imageMode(PApplet.CENTER); // center image
        app.image(dressImg, 325, 325); // draw the dress
        app.imageMode(PApplet.CORNER); // reset to top left

        for (int i = 0; i < tearPositions.size(); i++) { // loop through tears
            if (!torn.get(i)) { // only show unfixed
                PVector p = tearPositions.get(i); // get position
                app.image(tearImgs[i % tearImgs.length], p.x, p.y, SIZE, SIZE); // draw it
            }
        }

        if (showPopup) { // if finished
            app.fill(255); // white
            app.textAlign(PApplet.CENTER, PApplet.CENTER); // center text
            app.textSize(30); // large title
            app.text("You fixed the dress!", 325, 550); // main message
            app.textSize(15); // smaller font
            app.text("Click to continue...", 325, 575); // continue prompt
            app.textAlign(PApplet.CORNER, PApplet.CORNER); // reset
        } else {
            app.textAlign(PApplet.CENTER, PApplet.CENTER); // center text
            app.textSize(25); // instructions
            app.text("Fix the dress by removing the tears on it!", 325, 70); // help
            app.textAlign(PApplet.CORNER, PApplet.CORNER); // reset
        }
    }

    /**
     * checks if a tear was clicked and shows win popup if all are fixed
     */
    @Override
    public void mousePressed() {
        if (showPopup) { // if showing win popup
            completed = true; // mark done
            started = false; // stop game
            showPopup = false; // hide popup
            user.toggleUpdates(); // resume player control
            completedMinigames++; // increase counter
            SaveFile.setValue("dress", "true"); // save result
            return;
        }

        if (!started) { // do nothing if not started
            return;
        }

        for (int i = 0; i < tearPositions.size(); i++) { // check all tears
            if (!torn.get(i)) { // only allow if not fixed
                PVector p = tearPositions.get(i); // get position
                if (app.mouseX >= p.x && app.mouseX <= p.x + SIZE // check if clicked inside
                        && app.mouseY >= p.y && app.mouseY <= p.y + SIZE) {
                    torn.set(i, true); // mark as fixed
                    if (getTornCount() >= tearPositions.size()) { // if all fixed
                        showPopup = true; // show win popup
                    }
                    break; // stop checking
                }
            }
        }
    }

    /**
     * counts how many tears are currently fixed
     *
     * @return number of fixed tears
     */
    private int getTornCount() {
        int count = 0; // start at zero
        for (boolean b : torn) { // loop all tear states
            if (b) { // if torn
                count++; // add one
            }
        }
        return count; // return total
    }
}

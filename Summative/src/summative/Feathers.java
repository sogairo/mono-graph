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

public class Feathers extends Minigame {

    private static final int TOTAL_FEATHERS = 5; // total number of feathers
    private final int FEATHER_SIZE = 50; // size of each feather sprite
    private final float MIN_DISTANCE = 250; // minimum distance between feathers

    private final PImage featherImg; // image used to draw the feather
    private static final ArrayList<PVector> featherPositions = new ArrayList<>(); // list of feather positions
    private static final ArrayList<Boolean> collectedFlags = new ArrayList<>(); // tracks whether feather is collected
    private final int[][] featherCountPerSection = new int[2][2]; // counts feathers in each 2x2 map section

    private Map map; // reference to the game map
    private Dialogue dialogue; // optional cutscene that plays after collecting all feathers

    /**
     * creates the feather minigame and loads from save if available generates
     * random feather positions if save is missing
     *
     * @param app the main sketch
     * @param user the player
     * @param triggerX x coordinate of the interaction zone
     * @param triggerY y coordinate of the interaction zone
     * @param triggerSize radius for triggering interaction
     * @param map the map object to check player section
     */
    public Feathers(PApplet app, Player user, int triggerX, int triggerY, int triggerSize, Map map) {
        super(app, user, triggerX, triggerY, triggerSize); // setup minigame base

        boolean allFeathersExist = true; // assume save exists

        for (int i = 0; i < TOTAL_FEATHERS; i++) { // check each feather save entry
            String pos = SaveFile.getValue("feather" + (i + 1) + "pos"); // get saved position
            String collected = SaveFile.getValue("feather" + (i + 1) + "collected"); // get collected flag
            if (pos.isEmpty() || collected.isEmpty()) { // missing info
                allFeathersExist = false; // flag as incomplete
                break; // stop checking
            }
        }

        if (allFeathersExist) { // if save was valid
            for (int i = 0; i < TOTAL_FEATHERS; i++) { // restore all feathers
                String[] coords = SaveFile.getValue("feather" + (i + 1) + "pos").split(","); // split x y
                float x = Float.parseFloat(coords[0]); // convert x
                float y = Float.parseFloat(coords[1]); // convert y
                featherPositions.add(new PVector(x, y)); // save position
                collectedFlags.add(SaveFile.getValue("feather" + (i + 1) + "collected").equals("true")); // save status
            }
        } else {
            featherPositions.clear(); // clear old data
            collectedFlags.clear(); // clear collected states
            generateFeathers(); // make new random feathers and save them
        }

        this.featherImg = app.loadImage("images/items/feather.png"); // load image
        this.map = map; // store map
    }

    /**
     * randomly places feathers around the 2x2 map makes sure no feathers are
     * too close to each other saves their data to file
     */
    private void generateFeathers() {
        int generated = 0; // count how many feathers placed

        while (generated < TOTAL_FEATHERS) { // keep placing until full
            int sectionX = (int) app.random(0, 2); // pick horizontal map section
            int sectionY = (int) app.random(0, 2); // pick vertical map section

            if (featherCountPerSection[sectionX][sectionY] >= 2) { // limit per section
                continue; // skip and retry
            }

            float x = sectionX * 650 + app.random(0, 650 - FEATHER_SIZE); // random x in section
            float y = sectionY * 650 + app.random(0, 650 - FEATHER_SIZE); // random y in section

            boolean farEnough = true; // assume it's okay

            for (PVector pos : featherPositions) { // check each existing feather
                if (Math.abs(pos.x - x) < MIN_DISTANCE && Math.abs(pos.y - y) < MIN_DISTANCE) {
                    farEnough = false; // too close
                    break;
                }
            }

            if (farEnough) { // if valid
                featherPositions.add(new PVector(x, y)); // store position
                collectedFlags.add(false); // not yet collected
                featherCountPerSection[sectionX][sectionY]++; // update section count
                generated++; // one more placed

                SaveFile.setValue("feather" + generated + "pos", (int) x + "," + (int) y); // save x y
                SaveFile.setValue("feather" + generated + "collected", "false"); // save status
            }
        }
    }

    /**
     * counts how many feathers are collected so far
     *
     * @return number of feathers collected
     */
    private static int getCollectedCount() {
        int count = 0; // start at zero
        for (boolean b : collectedFlags) { // check all feathers
            if (b) { // if collected
                count++; // increase count
            }
        }
        return count; // final result
    }

    /**
     * checks if all feathers have been collected
     *
     * @return true if all feathers are collected
     */
    public static boolean allFeathersCollected() {
        return getCollectedCount() == TOTAL_FEATHERS; // check total
    }

    /**
     * runs when player interacts in center after collecting all feathers shows
     * alternate ending dialogue depending on minigames completed
     */
    @Override
    public void startMinigame() {
        if (completedMinigames == 3) { // all minigames done
            dialogue = new Dialogue(app, "trueending.txt", user, false); // true ending
        } else {
            dialogue = new Dialogue(app, "ending.txt", user, false); // normal ending
        }
        dialogue.startDialogue(); // play it
        SaveFile.wipe(); // clear save after story ends
    }

    /**
     * forwards dialogue when mouse is clicked
     */
    @Override
    public void mousePressed() {
        if (dialogue != null && dialogue.isActive()) { // if active
            dialogue.advance(); // go to next
        }
    }

    /**
     * draws feathers and handles collection also draws final dialogue if
     * available
     */
    @Override
    public void draw() {
        if (dialogue != null && dialogue.isActive()) { // if cutscene active
            dialogue.draw(); // show it
            return; // skip gameplay
        }

        for (int i = 0; i < featherPositions.size(); i++) { // draw all uncollected feathers
            PVector pos = featherPositions.get(i); // get position
            int sectionX = (int) (pos.x / 650); // figure out what section it is in
            int sectionY = (int) (pos.y / 650);

            if (sectionX == map.playerX && sectionY == map.playerY && !collectedFlags.get(i)) {
                app.image(featherImg, pos.x % 650, pos.y % 650, FEATHER_SIZE, FEATHER_SIZE); // draw feather

                float px = user.getX() + Player.SPRITE_WIDTH / 2f; // player center x
                float py = user.getY() + Player.SPRITE_HEIGHT / 2f; // player center y

                if (px > pos.x % 650 && px < (pos.x % 650 + FEATHER_SIZE)
                        && py > pos.y % 650 && py < (pos.y % 650 + FEATHER_SIZE)) {
                    collectedFlags.set(i, true); // mark as collected
                    SaveFile.setValue("feather" + (i + 1) + "collected", "true"); // update save
                }
            }
        }

        app.fill(255); // white color
        app.textSize(16); // set size
        app.text("Feathers: " + getCollectedCount() + " / " + TOTAL_FEATHERS, 500, 20); // draw counter

        if (map.playerX == 1 && map.playerY == 1 && allFeathersCollected()) { // if at center and all done
            if (super.inProximity()) { // if near altar
                app.fill(255); // white
                app.textSize(16); // set size
                app.text("[E] Interact", user.x - 15, user.y); // draw prompt
            }
        }
    }
}

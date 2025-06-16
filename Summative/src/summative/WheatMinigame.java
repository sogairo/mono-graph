/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package summative;

/**
 *
 * @author Sei
 */
import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class WheatMinigame extends Minigame {

    private PImage wheatImg, goldenWheatImg; // regular and golden wheat images
    private boolean showPopup = false; // flag for showing win popup

    private ArrayList<PVector> wheatPositions = new ArrayList<>(); // stores positions of all wheat
    private ArrayList<Boolean> collectedFlags = new ArrayList<>(); // tracks whether each wheat is collected

    private int totalWheat; // total wheat needed to complete game

    /**
     * sets up the wheat minigame and loads saved status
     *
     * @param app sketch reference
     * @param user the player object
     * @param triggerX x of trigger zone
     * @param triggerY y of trigger zone
     * @param triggerSize size of trigger zone
     */
    public WheatMinigame(PApplet app, Player user, int triggerX, int triggerY, int triggerSize) {
        super(app, user, triggerX, triggerY, triggerSize); // base class init
        this.wheatImg = app.loadImage("images/items/wheat.png"); // load wheat
        this.goldenWheatImg = app.loadImage("images/items/goldenwheat.png"); // load golden wheat

        if (SaveFile.getValue("wheat").equals("true")) { // check save status
            completed = true; // mark as complete
            completedMinigames++; // increment shared count
        }
    }

    /**
     * starts the minigame by generating wheat positions
     */
    @Override
    public void startMinigame() {
        if (completed) { // skip if already done
            return;
        }
        started = true; // mark as started

        totalWheat = (int) app.random(3, 5); // pick number of wheat

        for (int i = 0; i < totalWheat; i++) { // make each wheat
            int x = (int) app.random(40, 610); // random x
            int y = (int) app.random(40, 610); // random y
            wheatPositions.add(new PVector(x, y)); // store position
            collectedFlags.add(false); // mark uncollected
        }
    }

    /**
     * draws wheat or popup depending on progress
     */
    @Override
    public void draw() {
        if (completed) { // do nothing if done
            return;
        }

        if (!started) { // show prompt if nearby
            if (super.inProximity()) {
                app.fill(255); // white
                app.textSize(16); // size
                app.text("[E] Interact", user.x - 15, user.y); // prompt
            }
            return;
        }

        if (showPopup) { // show win image
            app.imageMode(PApplet.CENTER); // center it
            app.image(goldenWheatImg, 325, 325); // draw golden wheat
            app.imageMode(PApplet.CORNER); // reset mode

            app.fill(255); // white
            app.textSize(23); // big text
            app.textAlign(PApplet.CENTER, PApplet.CENTER); // center
            app.text("You collected all the wheat and it merged into one golden wheat!", 325, 550); // win message

            app.textSize(10); // small prompt
            app.text("Click to continue...", 325, 575); // next

            app.textAlign(PApplet.CORNER, PApplet.CORNER); // reset
            return;
        } else { // draw all wheat still left
            for (int i = 0; i < wheatPositions.size(); i++) {
                if (!collectedFlags.get(i)) {
                    PVector pos = wheatPositions.get(i); // get pos
                    app.image(wheatImg, pos.x - wheatImg.width / 2f, pos.y - wheatImg.height / 2f); // draw wheat
                }
            }

            app.textAlign(PApplet.CENTER, PApplet.CENTER); // center
            app.textSize(25); // title size
            app.text("Collect the wheat!", 325, 70); // draw title
            app.textAlign(PApplet.CORNER, PApplet.CORNER); // reset
        }

        checkCollection(); // see if player picked one up
    }

    /**
     * handles win popup click
     */
    @Override
    public void mousePressed() {
        if (showPopup) {
            completed = true; // done
            started = false; // stop game
            showPopup = false; // hide popup
            user.toggleUpdates(); // resume movement
            completedMinigames++; // track completion
            SaveFile.setValue("wheat", "true"); // save status
        }
    }

    /**
     * checks if the player is close enough to pick up wheat
     */
    private void checkCollection() {
        if (completed) {
            return;
        }

        float px = user.getX() + Player.SPRITE_WIDTH / 2f; // center x
        float py = user.getY() + Player.SPRITE_HEIGHT / 2f; // center y

        for (int i = 0; i < wheatPositions.size(); i++) { // check each wheat
            if (!collectedFlags.get(i)) {
                PVector pos = wheatPositions.get(i); // get wheat pos
                if (PVector.dist(new PVector(px, py), pos) < 35) { // close enough
                    collectedFlags.set(i, true); // mark as collected
                    break;
                }
            }
        }

        if (getCollectedCount() >= totalWheat) { // check win
            showPopup = true; // show result
            user.toggleUpdates(); // pause player
        }
    }

    /**
     * returns how many wheat have been picked up
     *
     * @return number of collected wheat
     */
    private int getCollectedCount() {
        int count = 0;
        for (boolean collected : collectedFlags) {
            if (collected) {
                count++;
            }
        }
        return count;
    }
}

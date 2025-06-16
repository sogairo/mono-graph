/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package summative;

/**
 *
 * @author Sei
 */
import processing.core.*;

import java.util.*;

public class FriedRiceMinigame extends Minigame {

    private PImage[] ingredientImgs; // holds images of the ingredients
    private PVector[] ingredientPositions; // holds the on screen position of ingredients
    private boolean[] clicked; // tracks if each ingredient was clicked

    private List<String> clickedOrder = new ArrayList<>(); // keeps track of clicked order
    private List<String> recipe = List.of("egg", "rice", "scallions", "soy"); // correct cooking order

    private String[] ingredientNames = {"egg", "rice", "scallions", "soy"}; // ingredient names
    private PImage kitchenbg = app.loadImage("images/backgrounds/kitchen.png"); // background image of kitchen

    private final float SIZE = 100; // size of each ingredient image
    private int currentStep = 0; // what step the player is on
    private boolean showPopup = false, failed = false; // show result flags
    private PImage popupImg; // image to show when player wins

    /**
     * constructor for the fried rice minigame loads images sets up ingredient
     * positions and checks save data
     *
     * @param app main game sketch
     * @param user player object
     * @param triggerX x coordinate of activation zone
     * @param triggerY y coordinate of activation zone
     * @param triggerSize size of activation zone
     */
    public FriedRiceMinigame(PApplet app, Player user, int triggerX, int triggerY, int triggerSize) {
        super(app, user, triggerX, triggerY, triggerSize); // set base values

        if (SaveFile.getValue("friedrice").equals("true")) { // check if completed
            completed = true; // mark done
            completedMinigames++; // add to total
        }

        ingredientImgs = new PImage[4]; // make space for images
        ingredientPositions = new PVector[4]; // make space for positions
        clicked = new boolean[4]; // mark all as not clicked

        List<String> tempList = Arrays.asList(ingredientNames); // convert to list
        Collections.shuffle(tempList); // shuffle order
        ingredientNames = tempList.toArray(ingredientNames); // assign shuffled result

        for (int i = 0; i < 4; i++) { // loop through ingredients
            ingredientImgs[i] = app.loadImage("images/items/" + ingredientNames[i] + ".png"); // load image
            ingredientPositions[i] = new PVector(100 + i * 120, 325); // set position
            clicked[i] = false; // mark not clicked
        }

        popupImg = app.loadImage("images/items/friedrice.png"); // load popup image
    }

    /**
     * starts the minigame and disables player movement
     */
    @Override
    public void startMinigame() {
        if (completed) { // don't restart if done
            return;
        }
        started = true; // mark as started
        user.toggleUpdates(); // disable movement
    }

    /**
     * draws the minigame depending on its current state
     */
    @Override
    public void draw() {
        if (completed) { // skip if done
            return;
        }

        if (!started) { // show prompt if player nearby
            if (super.inProximity()) {
                app.fill(255); // white text
                app.textSize(16); // set size
                app.text("[E] Interact", user.x - 15, user.y); // draw prompt
            }
            return;
        }

        app.image(kitchenbg, 0, 0); // draw kitchen background

        // if showing popup
        if (showPopup) {
            app.imageMode(PApplet.CENTER); // center popup
            app.image(popupImg, 325, 325); // draw popup image
            app.imageMode(PApplet.CORNER); // reset mode

            app.fill(255); // white
            app.textAlign(PApplet.CENTER, PApplet.CENTER); // center text
            app.textSize(40); // large text
            app.text("You made delicious fried rice!", 325, 500); // win text
            app.textSize(20); // smaller
            app.text("Click to continue...", 325, 535); // next prompt
            app.textAlign(PApplet.CORNER, PApplet.CORNER); // reset
            return;
        } else {
            if (failed) { // if failed before
                app.fill(255); // white
                app.textAlign(PApplet.CENTER, PApplet.CENTER); // center
                app.textSize(40); // big fail
                app.text("You failed to cook fried rice.", 325, 300); // fail text
                app.textSize(20); // small retry
                app.text("Click to retry...", 325, 345); // retry prompt
                app.textAlign(PApplet.CORNER, PApplet.CORNER); // reset
                return;
            }
            app.textAlign(PApplet.CENTER, PApplet.CENTER); // center
            app.textSize(25); // title
            app.text("Cook fried rice!", 325, 70); // draw title
            app.textSize(15); // instructions
            app.text("Cook the eggs first, then add rice, scallions, and finally, soy sauce!", 325, 100); // instructions
            app.textAlign(PApplet.CORNER, PApplet.CORNER); // reset
        }

        for (int i = 0; i < ingredientImgs.length; i++) { // draw all unclicked ingredients
            if (!clicked[i]) {
                app.image(ingredientImgs[i], ingredientPositions[i].x, ingredientPositions[i].y, SIZE, SIZE); // draw it
            }
        }
    }

    /**
     * handles logic when player clicks on ingredients
     */
    @Override
    public void mousePressed() {
        if (!started || completed) { // skip if not started or done
            return;
        }

        if (showPopup) { // on win screen
            showPopup = false; // close popup
            completed = true; // mark done
            started = false; // stop minigame
            user.toggleUpdates(); // enable movement
            completedMinigames++; // track complete
            SaveFile.setValue("friedrice", "true"); // save it
            return;
        }

        if (failed) { // reset after fail
            failed = false;
            return;
        }

        for (int i = 0; i < ingredientImgs.length; i++) { // check if ingredient clicked
            if (!clicked[i] && over(ingredientPositions[i])) { // if not clicked and inside
                clicked[i] = true; // mark clicked
                currentStep++; // next step
                clickedOrder.add(ingredientNames[i]); // add to order

                if (clickedOrder.size() == 4 && clickedOrder.equals(recipe)) { // check win
                    showPopup = true;
                } else if (clickedOrder.size() == 4) { // if wrong
                    System.out.println("FAILED..."); // debug
                    System.out.println(clickedOrder); // show order
                    reset(); // restart
                }
                return;
            }
        }
    }

    /**
     * checks if the mouse is over a given ingredient
     *
     * @param pos position of the ingredient
     * @return true if mouse is over the ingredient
     */
    private boolean over(PVector pos) {
        return app.mouseX >= pos.x && app.mouseX <= pos.x + SIZE // inside x range
                && app.mouseY >= pos.y && app.mouseY <= pos.y + SIZE; // inside y range
    }

    /**
     * resets the minigame if failed
     */
    private void reset() {
        for (int i = 0; i < clicked.length; i++) { // clear all clicks
            clicked[i] = false;
        }
        currentStep = 0; // reset step
        failed = true; // flag as failed
        clickedOrder.clear(); // clear history
    }
}

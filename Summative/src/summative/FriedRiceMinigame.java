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

import java.util.ArrayList;
import java.util.List;

public class FriedRiceMinigame extends Minigame {

    private PImage[] ingredientImgs;
    private PVector[] ingredientPositions;
    private boolean[] clicked;

    List<String> clickedOrder = new ArrayList<>();
    List<String> recipe = List.of("rice", "egg", "scallion", "soy");

    private String[] ingredientNames = {"egg", "rice", "scallions", "soy"};

    private float size = 80;
    private int currentStep = 0;
    private boolean showPopup = false, failed = false;
    private PImage popupImg;

    public FriedRiceMinigame(PApplet app, Player user, int triggerX, int triggerY, int triggerSize) {
        super(app, user, triggerX, triggerY, triggerSize);

        ingredientImgs = new PImage[4];
        ingredientPositions = new PVector[4];
        clicked = new boolean[4];

        for (int i = 0; i < 4; i++) {
            ingredientImgs[i] = app.loadImage("images/items/" + ingredientNames[i] + ".png");
            ingredientPositions[i] = new PVector(100 + i * 100, 400);
            clicked[i] = false;
        }

        popupImg = app.loadImage("images/items/friedrice.png");
    }

    @Override
    public void startMinigame() {
        if (completed) {
            return;
        }
        started = true;
        user.toggleUpdates();
    }

    @Override
    public void draw() {
        if (completed) {
            return;
        }

        if (!started) {
            if (super.inProximity()) {
                app.fill(0);
                app.textSize(16);
                app.text("[E] Interact", user.x - 15, user.y);
            }

            // dev visualization
            app.noFill();
            app.stroke(255, 0, 0);
            app.rect(triggerX, triggerY, triggerSize, triggerSize);
            return;
        }

        // If showing popup
        if (showPopup) {
            app.imageMode(PApplet.CENTER);
            app.image(popupImg, 325, 325);
            app.imageMode(PApplet.CORNER);

            app.fill(0);
            app.textAlign(PApplet.CENTER, PApplet.CENTER);
            app.textSize(16);
            app.text("You made delicious fried rice!", 325, 550);
            app.textSize(10);
            app.text("Click to continue...", 325, 575);
            app.textAlign(PApplet.CORNER, PApplet.CORNER);
            return;
        }
        
        if (failed) {
            app.fill(0);
            app.textAlign(PApplet.CENTER, PApplet.CENTER);
            app.textSize(16);
            app.text("YOU FAILED...", 325, 550);
            app.textSize(10);
            app.text("Click to retry...", 325, 575);
            app.textAlign(PApplet.CORNER, PApplet.CORNER);
            return;
        }

        for (int i = 0; i < ingredientImgs.length; i++) {
            if (!clicked[i]) {
                app.image(ingredientImgs[i], ingredientPositions[i].x, ingredientPositions[i].y, size, size);
            }
        }

        app.fill(100);
        app.textSize(12);
        app.text("Step: " + currentStep + " / " + recipe.size(), 10, 20);
    }

    @Override
    public void mousePressed() {
        if (!started || completed) {
            return;
        }

        if (showPopup) {
            showPopup = false;
            completed = true;
            started = false;
            user.toggleUpdates();
            return;
        }
        
        if (failed) {
            failed = false;
            return;
        }

        for (int i = 0; i < ingredientImgs.length; i++) {
            if (!clicked[i] && over(ingredientPositions[i])) {
                clicked[i] = true;
                currentStep++;
                clickedOrder.add(recipe.get(i));
                if (clickedOrder.size() == 4 && clickedOrder.equals(recipe)) {
                    showPopup = true;
                } else if (clickedOrder.size() == 4) {
                    System.out.println("FAILED...");
                    reset();
                }
                return;
            }
        }
    }

    private boolean over(PVector pos) {
        return app.mouseX >= pos.x && app.mouseX <= pos.x + size
                && app.mouseY >= pos.y && app.mouseY <= pos.y + size;
    }

    private void reset() {
        for (int i = 0; i < clicked.length; i++) {
            clicked[i] = false;
        }
        currentStep = 0;
        failed = true;
        clickedOrder.clear();
    }
}

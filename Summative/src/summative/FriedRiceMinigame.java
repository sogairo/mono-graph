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

    private PImage[] ingredientImgs;
    private PVector[] ingredientPositions;
    private boolean[] clicked;

    List<String> clickedOrder = new ArrayList<>();
    List<String> recipe = List.of("egg", "rice", "scallions", "soy");

    private String[] ingredientNames = {"egg", "rice", "scallions", "soy"};
    private final PImage kitchenbg = app.loadImage("images/backgrounds/kitchen.png");

    private final float SIZE = 100;
    private int currentStep = 0;
    private boolean showPopup = false, failed = false;
    private final PImage popupImg;

    public FriedRiceMinigame(PApplet app, Player user, int triggerX, int triggerY, int triggerSize) {
        super(app, user, triggerX, triggerY, triggerSize);

        ingredientImgs = new PImage[4];
        ingredientPositions = new PVector[4];
        clicked = new boolean[4];

        List<String> tempList = Arrays.asList(ingredientNames);
        Collections.shuffle(tempList);
        ingredientNames = tempList.toArray(ingredientNames);

        for (int i = 0; i < 4; i++) {
            ingredientImgs[i] = app.loadImage("images/items/" + ingredientNames[i] + ".png");
            ingredientPositions[i] = new PVector(100 + i * 120, 325);
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
        app.image(kitchenbg, 0, 0);

        // If showing popup
        if (showPopup) {
            app.imageMode(PApplet.CENTER);
            app.image(popupImg, 325, 325);
            app.imageMode(PApplet.CORNER);

            app.fill(255);
            app.textAlign(PApplet.CENTER, PApplet.CENTER);
            app.textSize(40);
            app.text("You made delicious fried rice!", 325, 500);
            app.textSize(20);
            app.text("Click to continue...", 325, 535);
            app.textAlign(PApplet.CORNER, PApplet.CORNER);
            return;
        }

        if (failed) {
            app.fill(255);
            app.textAlign(PApplet.CENTER, PApplet.CENTER);
            app.textSize(40);
            app.text("YOU FAILED...", 325, 300);
            app.textSize(20);
            app.text("Click to retry...", 325, 345);
            app.textAlign(PApplet.CORNER, PApplet.CORNER);
            return;
        }

        for (int i = 0; i < ingredientImgs.length; i++) {
            if (!clicked[i]) {
                app.image(ingredientImgs[i], ingredientPositions[i].x, ingredientPositions[i].y, SIZE, SIZE);
            }
        }

        app.fill(255);
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
                clickedOrder.add(ingredientNames[i]);
                if (clickedOrder.size() == 4 && clickedOrder.equals(recipe)) {
                    showPopup = true;
                } else if (clickedOrder.size() == 4) {
                    System.out.println("FAILED...");
                    System.out.println(clickedOrder);
                    reset();
                }
                return;
            }
        }
    }

    private boolean over(PVector pos) {
        return app.mouseX >= pos.x && app.mouseX <= pos.x + SIZE
                && app.mouseY >= pos.y && app.mouseY <= pos.y + SIZE;
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

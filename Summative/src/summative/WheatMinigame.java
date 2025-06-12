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

    private PImage wheatImg, goldenWheatImg;
    private boolean showPopup = false;

    private ArrayList<PVector> wheatPositions = new ArrayList<>();
    private ArrayList<Boolean> collectedFlags = new ArrayList<>();
    private int totalWheat;

    public WheatMinigame(PApplet app, Player user, int triggerX, int triggerY, int triggerSize) {
        super(app, user, triggerX, triggerY, triggerSize);
        this.wheatImg = app.loadImage("images/items/wheat.png");
        this.goldenWheatImg = app.loadImage("images/items/goldenwheat.png");
    }

    public void generateWheat() {

    }

    @Override
    public void startMinigame() {
        if (completed) {
            return;
        }
        started = true;

        totalWheat = (int) app.random(3, 5); // 3 to 5 wheat

        for (int i = 0; i < totalWheat; i++) {
            int x = (int) app.random(40, 610);
            int y = (int) app.random(40, 610);
            wheatPositions.add(new PVector(x, y));
            collectedFlags.add(false);
        }
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
            // dev testing
            app.noFill();
            app.stroke(255, 0, 0);
            app.rect(triggerX, triggerY, triggerSize, triggerSize);
            return;
        }
        
        if (showPopup) {
            app.imageMode(PApplet.CENTER);
            app.image(goldenWheatImg, 325, 325);
            app.imageMode(PApplet.CORNER);
            
            app.fill(255);
            app.textSize(23);
            app.textAlign(PApplet.CENTER, PApplet.CENTER);
            app.text("You collected all the wheat and it merged into one golden wheat!", 325, 550);
            
            app.textSize(10);
            app.text("Click to continue...", 325, 575);
            
            app.textAlign(PApplet.CORNER, PApplet.CORNER);
            return;
        }

        for (int i = 0; i < wheatPositions.size(); i++) {
            if (!collectedFlags.get(i)) {
                PVector pos = wheatPositions.get(i);
                app.image(wheatImg, pos.x - wheatImg.width / 2f, pos.y - wheatImg.height / 2f);
            }
        }

        checkCollection();

        app.fill(255);
        app.text("Wheat collected: " + getCollectedCount() + " / " + totalWheat, 10, 20);
    }

    @Override
    public void mousePressed() {
        if (showPopup) {
            completed = true;
            started = false;
            showPopup = false;
            user.toggleUpdates();
        }
    }

    private void checkCollection() {
        if (completed) {
            return;
        }

        float px = user.getX() + Player.SPRITE_WIDTH / 2f;
        float py = user.getY() + Player.SPRITE_HEIGHT / 2f;

        for (int i = 0; i < wheatPositions.size(); i++) {
            if (!collectedFlags.get(i)) {
                PVector pos = wheatPositions.get(i);
                if (PVector.dist(new PVector(px, py), pos) < 35) {
                    collectedFlags.set(i, true);
                    break;
                }
            }
        }

        // 🧠 Auto check completion internally
        if (getCollectedCount() >= totalWheat) {
            showPopup = true;
            user.toggleUpdates();
        }
    }

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

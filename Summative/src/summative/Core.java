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
 * WHAT THIS WILL HANDLE CORE IMPLENTATION: DRAWING, COLLISION PROMPTING,
 * HOSTING OTHER JAVA FILES, ETC
 *
 */
import processing.core.PApplet;

public class Core extends PApplet {
    private DialogueManager dialogueManager;
    private Map gameMap;
    private boolean mapInitialized = false;

    public void settings() {
        size(650, 650);
    }

    public void setup() {
        dialogueManager = new DialogueManager(this, "Scene1.txt");
        dialogueManager.startDialogue();
    }

    public void draw() {
        background(255);

        if (dialogueManager.isActive()) {
            dialogueManager.draw();
        } else {
            if (!mapInitialized) {
                mapInitialized = true;
                gameMap = new Map(this);
            }
            gameMap.draw();
        }
    }

    public void mousePressed() {
        if (dialogueManager.isActive()) {
            dialogueManager.advance();
        }
    }

    public void keyReleased() {
        if (mapInitialized && gameMap != null) {
            int dx = 0, dy = 0;
            if (key == 'a') dx = -1;
            if (key == 'd') dx = 1;
            if (key == 'w') dy = -1;
            if (key == 's') dy = 1;

            if (dx != 0 || dy != 0) {
                gameMap.moveSection(dx, dy);
            }
        }
    }
} 

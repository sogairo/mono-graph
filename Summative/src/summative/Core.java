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
import java.util.HashSet;
import java.util.Set;

import java.io.File;

public class Core extends PApplet {

    private Player user; // holds the player object
    private Map gameMap; // holds the current map
    private Dialogue dialogue; // handles all dialogue related things

    private final Set<Character> heldKeys = new HashSet<>(); // tracks which keys are being held

    /**
     * sets canvas size for the game
     */
    public void settings() {
        size(650, 650);// sets screen to 650 by 650
    }

    /**
     * runs once when the program starts sets up savefile user map and dialogue
     */
    public void setup() {
        background(255);// white background

        File save = new File("savefile.txt"); // load the save file

        if (!save.exists() || save.length() == 0) { // if no save file or empty
            SaveFile.initDefaults(); // make default values
        } else {
            SaveFile.load(); // load savefile values
        }

        user = new Player(this, 5); // create player and pass speed
        gameMap = new Map(this, user); // create map and give it player
        dialogue = new Dialogue(this, "intro.txt", user); // load intro dialogue

        if (!SaveFile.getBool("passedIntro")) { // if intro not yet done
            dialogue.startDialogue(); // start the intro
        }
    }

    /**
     * runs when mouse is clicked handles dialogue or minigame click
     */
    public void mousePressed() {
        if (dialogue.isActive()) { // if dialogue showing
            dialogue.advance(); // go to next line

            if (!dialogue.isActive() && !SaveFile.getBool("passedIntro")) { // if intro just finished
                SaveFile.setBool("passedIntro", true); // mark intro as passed
            }
            return; // skip minigame if dialogue just finished
        }

        Minigame currentMinigame = gameMap.getCurrentMinigame(); // get current minigame
        if (currentMinigame != null && currentMinigame.isStarted()) { // if one exists and already started
            currentMinigame.mousePressed(); // pass mouse click
        } else if (currentMinigame != null && gameMap.playerX == 1 && gameMap.playerY == 1) { // or player is in center
            currentMinigame.mousePressed(); // allow click
        }
    }

    /**
     * runs when a key is pressed adds to held keys and handles minigame start
     */
    public void keyPressed() {
        heldKeys.add(Character.toLowerCase(key)); // add key to held keys set

        // handle special key press scenarios
        if (Character.toLowerCase(key) == 'e') { // if user presses e
            Minigame currentMinigame = gameMap.getCurrentMinigame(); // get minigame at current spot
            if (currentMinigame != null && currentMinigame.inProximity()) { // if minigame is nearby
                if (gameMap.playerX == 1 && gameMap.playerY == 1) { // if player is at center
                    if (Feathers.allFeathersCollected()) { // only start if all feathers collected
                        currentMinigame.startMinigame(); // start minigame
                    }
                } else {
                    currentMinigame.startMinigame(); // otherwise just start it
                }
            }
        }
    }

    /** 
     * runs when key is released 
     * removes it from held keys
     */
    public void keyReleased() {
        heldKeys.remove(Character.toLowerCase(key));// stop tracking the key
    }

    
    /** 
     * this is the main game loop 
     * runs 60 times a second by default
     */
    public void draw() {
        background(255); // refresh background to white

        if (dialogue.isActive()) { // if dialogue is playing
            dialogue.draw(); // draw it
            return; // exit early
        }

        // testing
        gameMap.draw(); // draw the map

        Minigame currentMinigame = gameMap.getCurrentMinigame(); // get the minigame

        if (currentMinigame != null && (gameMap.playerX != 1 || gameMap.playerY != 1)) { // if one exists and player not in center
            currentMinigame.draw(); // draw it
        }

        // user info
        user.draw(); // draw the player

        int dx = 0, dy = 0; // x and y movement direction
        String dir = ""; // direction name

        if (heldKeys.contains('a')) { // if a is held
            dx -= 1; // move left
            dir = "left"; // direction is left
        }
        if (heldKeys.contains('d')) { // if d is held
            dx += 1; // move right
            dir = "right"; // direction is right
        }
        if (heldKeys.contains('w')) { // if w is held
            dy -= 1; // move up
            dir = "up"; // direction is up
        }
        if (heldKeys.contains('s')) { // if s is held
            dy += 1; // move down
            dir = "down"; // direction is down
        }

        if (dx != 0 || dy != 0) { // if player is trying to move
            gameMap.moveOrBlock(dx, dy, dir); // attempt movement
        } else {
            user.move(0, 0, dir); // keep idle in direction
        }
    }
}

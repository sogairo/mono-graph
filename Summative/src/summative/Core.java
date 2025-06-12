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

public class Core extends PApplet {

    private Player user;
    private Map gameMap;

    private final Set<Character> heldKeys = new HashSet<>();

    public void settings() {
        size(650, 650);
    }

    public void setup() {
        background(255);
        user = new Player(this, 5);
        gameMap = new Map(this, user);
    }

    public void mousePressed() {
        Minigame currentMinigame = gameMap.getCurrentMinigame();
        if (currentMinigame != null && currentMinigame.isStarted()) {
            currentMinigame.mousePressed();
        }
    }

    public void keyPressed() {
        heldKeys.add(Character.toLowerCase(key));

        // handle special key press scenarios
        if (Character.toLowerCase(key) == 'e') {
            Minigame currentMinigame = gameMap.getCurrentMinigame();
            if (currentMinigame != null && currentMinigame.inProximity()) {
                currentMinigame.startMinigame();
            }
        }
    }

    public void keyReleased() {
        heldKeys.remove(Character.toLowerCase(key));
    }

    public void draw() {
        background(255);

        // testing
        gameMap.draw();

        Minigame currentMinigame = gameMap.getCurrentMinigame();
        if (currentMinigame != null) {
            currentMinigame.draw();
        }

        // user info
        user.draw();

        int dx = 0, dy = 0;
        String dir = "";

        if (heldKeys.contains('a')) {
            dx -= 1;
            dir = "LEFT";
        }
        if (heldKeys.contains('d')) {
            dx += 1;
            dir = "RIGHT";
        }
        if (heldKeys.contains('w')) {
            dy -= 1;
            dir = "UP";
        }
        if (heldKeys.contains('s')) {
            dy += 1;
            dir = "DOWN";
        }

        if (dx != 0 || dy != 0) {
            gameMap.moveOrBlock(user, dx, dy, dir);
        }
    }
}

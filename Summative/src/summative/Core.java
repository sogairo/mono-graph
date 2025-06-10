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
        gameMap = new Map(this);
    }

    public void keyPressed() {
        heldKeys.add(Character.toLowerCase(key));
    }

    public void keyReleased() {
        heldKeys.remove(Character.toLowerCase(key));
    }

    public void draw() {
        background(255);
        gameMap.draw();
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

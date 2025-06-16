package summative;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Sei
 */
import processing.core.PApplet;

public class Minigame {

    protected PApplet app; // reference to the main sketch
    protected Player user; // reference to the player object
    protected boolean started = false, completed = false; // track game state
    public static int completedMinigames = 0; // shared counter for how many games finished

    protected int triggerX, triggerY, triggerSize; // zone to activate the minigame

    /**
     * constructor that sets up the minigame with trigger info and references
     *
     * @param app the main sketch
     * @param user the player object
     * @param triggerX the x position of the trigger zone
     * @param triggerY the y position of the trigger zone
     * @param triggerSize the size of the trigger square
     */
    public Minigame(PApplet app, Player user, int triggerX, int triggerY, int triggerSize) {
        this.app = app; // store sketch
        this.user = user; // store player
        this.triggerX = triggerX; // store x
        this.triggerY = triggerY; // store y
        this.triggerSize = triggerSize; // store size
    }

    /**
     * checks if the player is touching the activation zone
     *
     * @return true if player overlaps with trigger area
     */
    public boolean inProximity() {
        boolean isLeftOfOtherRight = triggerX < user.x + Player.SPRITE_WIDTH; // player right edge is past left edge
        boolean isRightOfOtherLeft = triggerX + triggerSize > user.x; // player left edge is before right edge
        boolean isAboveOfOtherBottom = triggerY < user.y + Player.SPRITE_HEIGHT; // player bottom is past top
        boolean isBelowOfOtherTop = triggerY + triggerSize > user.y; // player top is before bottom

        return isLeftOfOtherRight && isRightOfOtherLeft
                && isAboveOfOtherBottom && isBelowOfOtherTop; // all sides overlap
    }

    /**
     * draws the minigame visuals to be overridden by subclasses
     */
    public void draw() {}

    /**
     * starts the minigame logic to be overridden by subclasses
     */
    public void startMinigame() {}

    /**
     * handles mouse input to be overridden by subclasses
     */
    public void mousePressed() {}

    /**
     * tells if the minigame has started
     *
     * @return true if started
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * tells if the minigame is completed
     *
     * @return true if completed
     */
    public boolean isCompleted() {
        return completed;
    }
}

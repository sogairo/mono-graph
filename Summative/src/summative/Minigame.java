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
    protected PApplet app;
    protected Player user;
    protected boolean started = false, completed = false;

    // Trigger zone position and size
    protected int triggerX, triggerY, triggerSize;
    
    public Minigame(PApplet app, Player user, int triggerX, int triggerY, int triggerSize) {
        this.app = app;
        this.user = user;
        this.triggerX = triggerX;
        this.triggerY = triggerY;
        this.triggerSize = triggerSize;
    }
    
    public boolean inProximity() {
        boolean isLeftOfOtherRight = triggerX < user.x + Player.SPRITE_WIDTH;
        boolean isRightOfOtherLeft = triggerX + triggerSize > user.x;
        boolean isAboveOfOtherBottom = triggerY < user.y + Player.SPRITE_HEIGHT;
        boolean isBelowOfOtherTop = triggerY + triggerSize > user.y;
        
        return isLeftOfOtherRight && isRightOfOtherLeft 
                && isAboveOfOtherBottom && isBelowOfOtherTop;
    }
    
    public void draw() {}
    public void startMinigame() {}
    public void mousePressed() {}
    
    public boolean isStarted() {
        return started;
    }
    
    public boolean isCompleted() {
        return completed;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package summative;

/**
 *
 * @author Sei
 */
import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Random;

public class Tree {

    int x, y;
    ArrayList<Peach> peaches = new ArrayList<>();
    private PImage treeImage;

    public Tree(PApplet app, int x, int y, int fixedRarity) {
        this.x = x;
        this.y = y;
        treeImage = app.loadImage("images/tree.png");
        generatePeaches(app, fixedRarity);
    }

    private void generatePeaches(PApplet app, int rarity) {
        Random rand = new Random();
        int count = 2 + rand.nextInt(3);
        for (int i = 0; i < count; i++) {
            int px = x + rand.nextInt(40);
            int py = y + rand.nextInt(40);
            peaches.add(new Peach(app, px, py, rarity));
        }
    }

    public void draw(PApplet app) {
        app.image(treeImage, x, y, 120, 120);
        for (Peach p : peaches) {
            p.draw();
        }
    }

    public void checkCollision(Player player) {
        for (Peach p : peaches) {
            p.checkCollision(player.getX(), player.getY());
        }
    }
}

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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DialogueManager {
    private PApplet app;
    private ArrayList<String> lines;
    private int currentLineIndex;
    private boolean active;

    public DialogueManager(PApplet app, String filePath) {
        this.app = app;
        this.lines = new ArrayList<>();
        this.currentLineIndex = 0;
        this.active = false;
        loadDialogue(filePath);
    }

    private void loadDialogue(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading dialogue file: " + e.getMessage());
        }
    }

    public void startDialogue() {
        if (!lines.isEmpty()) {
            active = true;
            currentLineIndex = 0;
        }
    }

    public void draw() {
        if (active && currentLineIndex < lines.size()) {
            String line = lines.get(currentLineIndex);
            String[] parts = line.split("\\|");

            // Draw image centered if available
            if (parts.length > 1 && parts[1] != null) {
                PImage img = app.loadImage(parts[1]);
                if (img != null) {
                    app.imageMode(PApplet.CENTER);
                    app.image(img, app.width / 2, app.height / 2);
                    app.imageMode(PApplet.CORNER);
                }
            }

            // Draw dialogue box and text on top
            app.fill(255);
            app.rect(50, app.height - 150, app.width - 100, 100);
            app.fill(0);
            app.textSize(16);
            app.text(parts[0], 70, app.height - 120);
            app.text("Click to continue...", 70, app.height - 90);
        }
    }

    public void advance() {
        if (active) {
            currentLineIndex++;
            if (currentLineIndex >= lines.size()) {
                active = false;
            }
        }
    }

    public boolean isActive() {
        return active;
    }
} 

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package summative;

/**
 *
 * @author Sei
 */
import java.util.*;
import java.io.*;

public class SaveFile {

    private static final String SAVE_PATH = "savefile.txt"; // file path for save
    private static List<String> lines = new ArrayList<>(); // stores raw save lines

    /**
     * loads save file into memory trims each line before storing
     */
    public static void load() {
        lines.clear(); // start clean
        try {
            File file = new File("savefile.txt"); // open file
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine(); // read line
                    lines.add(line.trim()); // store trimmed
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage()); // show error
        }
    }

    /**
     * saves current memory values to file
     */
    private static void save() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_PATH))) {
            for (String line : lines) { // write each line
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error writing savefile: " + e.getMessage()); // error if write fails
        }
    }

    /**
     * finds index of the key in lines
     *
     * @param key the setting name
     * @return line index or -1 if not found
     */
    private static int findIndex(String key) {
        for (int i = 0; i < lines.size(); i++) { // check each line
            if (lines.get(i).startsWith(key + "=")) { // if key matches
                return i;
            }
        }
        return -1; // not found
    }

    /**
     * gets a boolean value from save
     *
     * @param key key name
     * @return true or false depending on value
     */
    public static boolean getBool(String key) {
        int index = findIndex(key);
        if (index == -1) {
            return false;
        }
        String[] parts = lines.get(index).split("=", 2); // split into key value
        return parts.length == 2 && parts[1].equalsIgnoreCase("true"); // true if value is true
    }

    /**
     * sets a boolean value in save
     *
     * @param key key name
     * @param value true or false
     */
    public static void setBool(String key, boolean value) {
        setValue(key, String.valueOf(value)); // store as string
    }

    /**
     * gets a string value from save
     *
     * @param key key name
     * @return value or empty if missing
     */
    public static String getValue(String key) {
        int index = findIndex(key); // get index
        if (index == -1) {
            return "";
        }
        String[] parts = lines.get(index).split("=", 2); // split key value
        return parts.length == 2 ? parts[1] : ""; // return value
    }

    /**
     * sets a string value in save
     *
     * @param key key name
     * @param value value to store
     */
    public static void setValue(String key, String value) {
        int index = findIndex(key); // find line
        String entry = key + "=" + value; // make line
        if (index != -1) {
            lines.set(index, entry); // update existing
        } else {
            lines.add(entry); // add new
        }
        save(); // write to file
    }

    /**
     * checks if a feather was collected
     *
     * @param featherNum which feather
     * @return true if collected
     */
    public static boolean getFeatherCollected(int featherNum) {
        return getBool("feather" + featherNum + "collected");
    }

    /**
     * sets collection status for a feather
     *
     * @param featherNum which feather
     * @param collected true or false
     */
    public static void setFeatherCollected(int featherNum, boolean collected) {
        setBool("feather" + featherNum + "collected", collected);
    }

    /**
     * gets the saved position of a feather
     *
     * @param featherNum which feather
     * @return string of x,y
     */
    public static String getFeatherPos(int featherNum) {
        return getValue("feather" + featherNum + "pos");
    }

    /**
     * sets the saved position of a feather
     *
     * @param featherNum which feather
     * @param x x coordinate
     * @param y y coordinate
     */
    public static void setFeatherPos(int featherNum, int x, int y) {
        setValue("feather" + featherNum + "pos", x + "," + y);
    }

    /**
     * clears all save data from memory and disk
     */
    public static void wipe() {
        lines.clear(); // reset memory
        save(); // save empty
    }

    /**
     * fills save with default values for first time setup
     */
    public static void initDefaults() {
        lines.clear(); // reset everything

        setBool("dress", false);
        setBool("friedrice", false);
        setBool("wheat", false);

        setFeatherCollected(1, false);
        setFeatherCollected(2, false);
        setFeatherCollected(3, false);
        setFeatherCollected(4, false);
        setFeatherCollected(5, false);

        setBool("passedIntro", false);

        setValue("playerX", "0");
        setValue("playerY", "0");
        setValue("playerDirection", "down");

        setValue("mapX", "1");
        setValue("mapY", "0");

        save(); // commit everything
    }
}

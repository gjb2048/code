/*
 * Flipper
 *
 * @copyright  2022 G J Barnard
 * @license    http://www.gnu.org/copyleft/gpl.html GNU GPL v3 or later
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Flipper {
    private InputStreamReader isr = null;
    private BufferedReader br = null;
    private HashMap<Character, Character> map = null; 

    private static final boolean DEBUG = false;
    private static final int DIFFERENCE = 4;
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz@*.";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ&%#";
    
    public static void main(String args[]) {
        Flipper us = new Flipper();

        System.out.println("Flipped: " + us.flip("Flip   : "));
    }

    public Flipper() {
        this.isr = new InputStreamReader(System.in);
        this.br = new BufferedReader(this.isr);

        char[] lowerChars = LOWER.toCharArray();
        char[] upperChars = UPPER.toCharArray();
        int lowerMax = lowerChars.length - 1;
        int toIndex;

        if (Flipper.DEBUG) {
            for (int index = 0; index <= lowerMax; index++) {
                toIndex = index + Flipper.DIFFERENCE;
                if (toIndex > lowerMax) {
                    toIndex = (toIndex - 1) - lowerMax;
                }

                System.out.println(index + " -> " + toIndex);
            }
        }
        
        int half = (lowerMax + 1) / 2;
        int fromIndex;
        int mapLength = lowerChars.length * 2;
        this.map = new HashMap<>(mapLength);
        for (int index = 0; index <= half; index++) {
            fromIndex = index + Flipper.DIFFERENCE;
            if (fromIndex > lowerMax) {
                fromIndex = fromIndex - lowerMax;
            }
            toIndex = (lowerMax - index) + Flipper.DIFFERENCE;
            if (toIndex > lowerMax) {
                toIndex = (toIndex - 1) - lowerMax;
            }
            
            if (Flipper.DEBUG) {
                System.out.println(
                    index + ": " + lowerChars[fromIndex] + " -> " + lowerChars[toIndex] +
                    " & " + upperChars[fromIndex] + " -> " + upperChars[toIndex]
                );
            }
            
            // Add to map.
            this.map.put(lowerChars[fromIndex], lowerChars[toIndex]);
            this.map.put(lowerChars[toIndex], lowerChars[fromIndex]);
            this.map.put(upperChars[fromIndex], upperChars[toIndex]);
            this.map.put(upperChars[toIndex], upperChars[fromIndex]);
        }
        
        // Test map.
        if (Flipper.DEBUG) {
            for (int index = 0; index < lowerChars.length; index++) {
                System.out.println(
                    index + ": " + lowerChars[index] + " -> " + this.map.get(lowerChars[index]) +
                    " & " + upperChars[index] + " -> " + this.map.get(upperChars[index])
                );
            }
        }
    }

    private String flip(String message) {
        System.out.print(message);

        try {
            String input = br.readLine();

            char[] inputChars = input.toCharArray();
            Character theChar;
            for (int index = 0; index < inputChars.length; index++) {
                theChar = this.map.get(inputChars[index]);
                if (theChar != null) {
                    inputChars[index] = theChar.charValue();
                }
            }

            return new String(inputChars);
        } catch (java.io.IOException ioe) {
            return ("Ops: " + ioe.getLocalizedMessage());
        }
       
    }
}
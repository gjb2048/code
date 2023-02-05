/*
 * Flipper
 *
 * Flips an entered string with a simple substitution cypher.
 *
 * @copyright  2022 G J Barnard
 * @license    http://www.gnu.org/copyleft/gpl.html GNU GPL v3 or later
 */

// Import classes we need.
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Flipper class.
 * @author G J Barnard
 */
public class Flipper {
    private InputStreamReader isr = null; // Stream to get the key presses from the keyboard.
    private BufferedReader br = null; // Buffer to store the pressed keys from the input stream.
    private HashMap<Character, Character> map = null; // Map to store the character to character relationship.
    private String[] ourArgs = null;

    private static final boolean DEBUG = false; // Show the debug messages.  Static so recompile when change.
    private static final int DIFFERENCE = 4; // How many characters to 'shift to the left' the characters we flip.

    // Characters we flip, both strings must be the same length so that the combined tally of the two is even.
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz@*.:";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ&%#;";

    /**
     * Main program entry point.
     * @param args Not used.
     */
    public static void main(String args[]) {
        Flipper us = new Flipper(); // Instantiate our class.

        if (args.length > 1) {
            us.checkArgs(args);
        }

        System.out.println("Flipped: " + us.flip("Flip   : ")); // Flip with the before and after string prefixes.
    }

    /**
     * Constructor to setup the input and create the character code map.
     */
    public Flipper() {
        // Convert the string to characters we can access via an array index with a number.
        char[] lowerChars = LOWER.toCharArray();
        char[] upperChars = UPPER.toCharArray();
        // Array indexes start at zero and not one, but the length is the actual number of characters.
        int lowerMax = lowerChars.length - 1;
        // Ditto for indexing with the difference.
        int difference = Flipper.DIFFERENCE - 1;
        int fromIndex; // From character.
        int toIndex; // To character.

        // Confirm our logic for looping around the toIndex when its larger than the number of characters.
        if (Flipper.DEBUG) {
            for (int index = 0; index <= lowerMax; index++) {
                fromIndex = index + difference;
                if (fromIndex > lowerMax) {
                    fromIndex = fromIndex - lowerMax;
                }
                System.out.println(index + " -> " + fromIndex);
            }
        }
        
        int half = (lowerMax + 1) / 2;  // Find the mid point in the character string.
        int mapLength = lowerChars.length * 2; // Work out how big the map will be.
        this.map = new HashMap<>(mapLength);  // Construct the map, character to character.
        for (int index = 0; index <= half; index++) {
            fromIndex = index + difference; // Shift.
            if (fromIndex > lowerMax) {
                fromIndex = fromIndex - lowerMax;
            }

            toIndex = (lowerMax - index) + difference; // Flip.
            if (toIndex > lowerMax) {
                toIndex = (toIndex - 1) - lowerMax;
            }

            // Check the mappings.
            if (Flipper.DEBUG) {
                System.out.println(
                    index + " (From: " + fromIndex + " To: " + toIndex + ")" +
                    ": " + lowerChars[fromIndex] + " -> " + lowerChars[toIndex] + " -> " + lowerChars[fromIndex] +
                    " & " + upperChars[fromIndex] + " -> " + upperChars[toIndex] + " -> " + upperChars[fromIndex]
                );
            }
            
            // Add the key value pairs to the map.  For example, a -> z and back again, z -> a.
            this.map.put(lowerChars[fromIndex], lowerChars[toIndex]);
            this.map.put(lowerChars[toIndex], lowerChars[fromIndex]);
            this.map.put(upperChars[fromIndex], upperChars[toIndex]);
            this.map.put(upperChars[toIndex], upperChars[fromIndex]);
        }
        
        // Check that the map is what we intended.
        if (Flipper.DEBUG) {
            for (int index = 0; index < lowerChars.length; index++) {
                System.out.println(
                    index + ": " + lowerChars[index] + " -> " + this.map.get(lowerChars[index]) +
                    " & " + upperChars[index] + " -> " + this.map.get(upperChars[index])
                );
            }
        }
    }

    private boolean checkArgs(String args[]) {
        /*for (String var: args ) {
            this.ourArgs[] = 
        }*/
        
        this.ourArgs = args.clone();
        
        return true;
    }

    /**
     * Flip the entered text.
     * @param message What to show as the input question.
     * @return 
     */
    private String flip(String message) {
        String input;

        System.out.print(message);

        if (this.ourArgs == null) {
            this.isr = new InputStreamReader(System.in); // Attach a stream to the system input.
            this.br = new BufferedReader(this.isr); // Attach a buffer to store the pressed keys.

            // Using a 'try / catch' block as reading key presses is an input output operation that can fail.
            try {
                input = br.readLine(); // Read a line of text from the user.
            } catch (java.io.IOException ioe) { // An error has happened when getting the text from the user.
                // Tell the user what the error is.
                System.out.println(); // New line after the message.
                this.processError(ioe);
                return ("");
            }
        } else {
            input = this.ourArgs[1];
        }

        /* Using a 'try / catch' block as pressing 'Ctrl-C' during input caused the 'input' to be null and
           so the 'toCharArray()' call fails. */
        char[] inputChars;
        try {
            // Convert the text from the user (string) into an array of indexable characters.
            inputChars = input.toCharArray();
        } catch (java.lang.NullPointerException npe) {
            // Tell the user what the error is.
            System.out.println();
            this.processError(npe);
            return ("");       
        }

        Character theChar; // Temporary store for the character in the 'type' we need it in from the map.

        // Process each character in the input text from the user.
        for (int index = 0; index < inputChars.length; index++) {
            theChar = this.map.get(inputChars[index]); // Get the mapped character, key -> value.
            if (theChar != null) { // If the character is mapped then flip, otherwise it will not be transposed.
                /* Replace the entered character with the mapped one.  Being clear here with the Character to char
                   type conversion.  Removing 'charValue()' call will still work. */
                inputChars[index] = theChar.charValue();
            }
        }

        return new String(inputChars); // Return the flipped entered text as a string we can output.
    }

    /**
     * Tell the user what happened.
     * @param ex The exception to process.
     */
    private void processError(java.lang.Exception ex) {
        System.err.print("Ops! -> "); // For some reason string concatenation does not work here.
        System.err.print(ex.getLocalizedMessage()); // What happened.
        System.err.print(" @ ");                    // And.
        System.err.println(ex.getStackTrace()[0]);  // Where.
    }
}

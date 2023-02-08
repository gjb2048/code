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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Flipper class.
 * @author G J Barnard
 */
public class Flipper {
    private InputStreamReader isr = null; // Stream to get the key presses from the keyboard.
    private BufferedReader br = null; // Buffer to store the pressed keys from the input stream.
    private HashMap<Character, Character> map = null; // Map to store the character to character relationship.
    private ArrayList<String> ourArgs = null; // Arguments.

    private boolean DEBUG = false; // Show the debug messages.  Static so recompile when change.
    private int DIFFERENCE = 4; // How many characters to 'shift to the left' the characters we flip.

    // Characters we flip, both strings must be the same length so that the combined tally of the two is even.
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz@*.:";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ&%#;";

    /**
     * Main program entry point.
     * @param args Not used.
     */
    public static void main(String args[]) {
        Flipper us = new Flipper(args); // Instantiate our class.

        System.out.println("Flipped: " + us.flip("Flip   : ")); // Flip with the before and after string prefixes.
    }

    /**
     * Constructor to setup the input and create the character code map.
     * @param args The arguments.
     */
    public Flipper(String args[]) {
        // Check arguments.
        this.checkArgs(args);

        // Convert the string to characters we can access via an array index with a number.
        char[] lowerChars = LOWER.toCharArray();
        char[] upperChars = UPPER.toCharArray();
        // Array indexes start at zero and not one, but the length is the actual number of characters.
        int lowerMax = lowerChars.length - 1;
        // Ditto for indexing with the difference.
        int difference = this.DIFFERENCE - 1;
        int fromIndex; // From character.
        int toIndex; // To character.

        // Confirm our logic for looping around the toIndex when its larger than the number of characters.
        if (this.DEBUG) {
            System.out.println("Difference is: " + this.DIFFERENCE);
            System.out.println();
            System.out.println("Loop check:");
            for (int index = 0; index <= lowerMax; index++) {
                fromIndex = index + difference;
                if (fromIndex > lowerMax) {
                    fromIndex = fromIndex - lowerMax;
                }
                System.out.println(index + " -> " + fromIndex);
            }
            System.out.println();
        }

        int half = (lowerMax + 1) / 2;  // Find the mid point in the character string.
        int mapLength = lowerChars.length * 2; // Work out how big the map will be.
        this.map = new HashMap<>(mapLength);  // Construct the map, character to character.
        if (this.DEBUG) {
            System.out.println("Mappings:");
        }
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
            if (this.DEBUG) {
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
        if (this.DEBUG) {
            System.out.println();
            System.out.println("The map is:");
            for (int index = 0; index < lowerChars.length; index++) {
                System.out.println(
                    index + ": " + lowerChars[index] + " -> " + this.map.get(lowerChars[index]) +
                    " & " + upperChars[index] + " -> " + this.map.get(upperChars[index])
                );
            }
            System.out.println();
        }
    }

    /**
     * Check the arguments
     *
     * Note: The exit status code, derived from 'C's 'errno.h', see: https://en.wikipedia.org/wiki/Errno.h
     *
     * @param args The arguments.
     */
    private void checkArgs(String args[]) {
        if (args.length > 0) {
            for (String var: args) {
                if (var.charAt(0) == '-') { // Argument prefix.
                    String param = var.substring(1);
                    if (param.charAt(0) == '?') { // Help.
                        this.displayHelp();
                        System.exit(0); // Bye!
                    }
                    if (param.charAt(0) == 'd') { // Debug.
                        // Debug.
                        this.DEBUG = true;
                        continue;
                    }
                    try { // If a number then this is the difference to use instead of the default assigned during construction.
                        this.DIFFERENCE = Integer.parseInt(param);
                    } catch (NumberFormatException nfe) { // Ops!
                        System.out.println("Invalid argument: " + param);
                        System.out.println();
                        this.displayHelp();
                        System.exit(22); // EINVAL - Invalid argument.
                    }
                } else { // We have text to flip.  The command line is delimited by a space, so we can have many words.
                    if (this.ourArgs == null) {
                        this.ourArgs = new ArrayList<>();
                    }
                    this.ourArgs.add(var);
                }
            }
        }
    }

    /**
     * Display the help.
     */
    private void displayHelp() {
        System.out.println("Usage: ");
        System.out.println("java Flipper [arguments] [text to flip or leave empty to be asked]");
        System.out.println("Optional arguments:");
        System.out.println("-[number] The difference.");
        System.out.println("-d Turn on debugging.");
        System.out.println("-? Show This help.");
    }

    /**
     * Flip the entered text.
     * @param message What to show as the input question.
     * @return
     */
    private String flip(String message) {
        String input;

        if (this.ourArgs == null) { // No text to flip, so ask.
            System.out.print(message);

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
        } else { // Get the text supplied on the command line and concatenate into one line.
            input = new String();
            Iterator<String> args = this.ourArgs.iterator(); // Iterate over each word.
            while (args.hasNext()) { // Another word?
                input += args.next(); // Get the word.
                if (args.hasNext()) {
                    input += " "; // Put the spaces back.
                }
            }
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

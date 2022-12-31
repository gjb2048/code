/*
 * CalGen.
 *
 * Generates the calendar for the year set, both as a HTML page from 'templated' and as text.
 *
 * @copyright  2022 G J Barnard
 * @license    http://www.gnu.org/copyleft/gpl.html GNU GPL v3 or later
 */

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * CalGen class.
 *
 * @author G J Barnard
 */
public class CalGen {

    // Calendar attributes.
    private final GregorianCalendar gc = new GregorianCalendar(); // The calenadar.
    private final LinkedList<Integer> months = new LinkedList<>(); // The days of the week.
    private final LinkedList<Integer> days = new LinkedList<>(); // The days of the week.
    private int currentMonth; // Keep track of the current month between methods.
    private int previousMonth; // Keep track of the previous month between methods.
    private final int theYear; // The year we are using.

    // Template attributes.
    private char[] calendarTemplate = null; // The template for the calendar.
    private char[] monthTemplate = null; // The template of a month within the calendar.

    private final char[] mpre = {'{', '{'}; // Template markup token start characters.
    private final char[] mpost = {'}', '}'}; // Template markup token end characters.
    private final FileOutputStream mout; // The stream for the markup html file.
    private final StringBuffer markupOut = new StringBuffer(); // Stores the markup html as its being generated before it is output to the file.

    /**
     *
     * Create the calendar and generate both the text and markup versions.
     *
     * @param args the command line arguments - not used.
     * @throws java.io.FileNotFoundException If a template file cannot be found.
     * @throws java.io.IOException If a problem occurs when reading a template file.
     */
    public static void main(String args[]) throws FileNotFoundException, IOException {
        CalGen us = new CalGen();

        us.calendar();
        us.calendarTemplate();
    }

    /**
     * Constructor.
     *
     * @throws FileNotFoundException If a template file cannot be found.
     */
    public CalGen() throws FileNotFoundException {
        this.gc.setFirstDayOfWeek(Calendar.TUESDAY); // Change to another day if wished.
        this.theYear = 2023;

        this.mout = new FileOutputStream("./calm.html"); // The name of the markup file in the current directory.

        // Add the months in the order we wish to output them as text.
        months.add(Calendar.JANUARY);
        months.add(Calendar.FEBRUARY);
        months.add(Calendar.MARCH);
        months.add(Calendar.APRIL);
        months.add(Calendar.MAY);
        months.add(Calendar.JUNE);
        months.add(Calendar.JULY);
        months.add(Calendar.AUGUST);
        months.add(Calendar.SEPTEMBER);
        months.add(Calendar.OCTOBER);
        months.add(Calendar.NOVEMBER);
        months.add(Calendar.DECEMBER);
        
        // Add the days in the order used by default in the GregorianCalendar.
        days.add(Calendar.SUNDAY);
        days.add(Calendar.MONDAY);
        days.add(Calendar.TUESDAY);
        days.add(Calendar.WEDNESDAY);
        days.add(Calendar.THURSDAY);
        days.add(Calendar.FRIDAY);
        days.add(Calendar.SATURDAY);

        // Rotate the days around if needed so that the start day is first in the list.
        Iterator<Integer> daysIt = days.iterator(); // The means of iterating over our list.
        boolean found = false; // Have we found the day we are looking for?
        int count = 0; // The number of positions the day we are looking for is away from Sunday.
        Integer current; // The reference to the current day.
        int firstDayOfWeek = this.gc.getFirstDayOfWeek(); // The day that the calendar has been set to be the first day of the week.

        while (daysIt.hasNext() && found == false) { // While we have another day to check and we've not found the day we are looking for.
            current = daysIt.next(); // Get the next day.
            if (current == firstDayOfWeek) { // Have we found the day we are looking for?
                found = true; // Yes.
            } else {
                count++; // Increment the position.
            }
        }

        if (count > 0) { // The day we are looking for is not Sunday, but is 'count' positions away from it.
            // Rotate the list to the left by the number of positions we have calculated.
            // The day we are looking for will then be the first.
            Collections.rotate(days, -count);
        }
    }

    /**
     * Generate the text version of the calendar.
     */
    public void calendar() {
        this.gc.set(theYear, 0, 1); // Set to the 1st January for the year we want.

        // Output the year.
        System.out.println(this.gc.get(Calendar.YEAR));

        // Output the months.
        Iterator<Integer> mit = this.months.iterator();
        while (mit.hasNext()) {
            this.month(mit.next());
            System.out.println();
        }
    }

    /**
     * Output the month.
     *
     * @param theMonth The month to output.
     */
    private void month(int theMonth) {
        this.gc.set(Calendar.MONTH, theMonth); // Tell the calendar the month we wish to use.
        // Set both months to be the same so that we can detect when the current changes.
        this.currentMonth = theMonth;
        this.previousMonth = theMonth;

        System.out.println(this.getMonthText(gc.get(Calendar.MONTH))); // Output the month text.

        // Output the day names.
        Iterator<Integer> daysIt = this.days.iterator();
        Integer current;
        while (daysIt.hasNext()) {
            current = daysIt.next();
            this.day(this.getDayText(current));
            if (daysIt.hasNext()) {
                System.out.print(" ");            
            } else {
                System.out.println();                
            }
        }

        // Output the 'blank days' before the day on which the 1st of the month is.
        int currentPosition = 1; // The current 'position' of the day in the week we are outputing, so '1' is the first day of the week.
        daysIt = this.days.iterator();
        boolean startDayReached = false; // Have we found the start day?
        int monthStartPostion = this.gc.get(Calendar.DAY_OF_WEEK); // Day of the week that the month starts on.

        while (daysIt.hasNext() && (startDayReached == false)) {
            current = daysIt.next();
            if (current == monthStartPostion) {
                startDayReached = true;
            } else {
                currentPosition++;
                this.day("");
                System.out.print(" -! ");
            }
        }

        // Loop until we have reached the next month.
        while (this.currentMonth == this.previousMonth) {
            
            // Loop through the day 'positions' as we have outputted them with the day names.
            while (currentPosition < 8) {

                // Have we reached the next month?
                if (this.currentMonth != this.previousMonth) {
                    // Are we on a week that has been started but not finished?
                    if (currentPosition != 1) {
                        // Loop through the remaining positions and output 'blank' days.
                        while (currentPosition < 8) {
                            this.day("");
                            System.out.print(" -* ");
                            currentPosition++;
                        }
                    }
                } else {
                    // Output the day.
                    if (this.gc.get(Calendar.DAY_OF_MONTH) > 9) { // Get the prefixing spacing correct.
                        System.out.print(" ");
                    } else {
                        System.out.print("  ");
                    }
                    this.day(this.gc.get(Calendar.DAY_OF_MONTH)); // The day.
                    if (currentPosition < 7) {
                        System.out.print(" "); // Postfix space.
                    }

                    // Get the next day.
                    this.gc.add(Calendar.DAY_OF_MONTH, 1);
                    this.currentMonth = this.gc.get(Calendar.MONTH);

                    currentPosition++; // The next position in the week.
                }
            }
            currentPosition = 1; // Reset to the next week.
            System.out.println();
        }
    }

    /**
     * Output the day.
     * @param day As an integer.
     */
    private void day(Integer day) {
        this.day(day.toString());
    }

    /**
     * Output the day.
     * @param day As a string.
     */
    private void day(String day) {
        System.out.print(day);
    }

    /**
     * Generate the markup version of the calendar.
     *
     * @throws FileNotFoundException If a template file cannot be found.
     * @throws IOException If a problem occurs when reading a template file.
     */
    public void calendarTemplate() throws FileNotFoundException, IOException {
        this.gc.set(theYear, 0, 1); // Reset to the 1st January for the year we want.

        this.loadTemplates(); // Load the templates.

        // Process the calendar template.
        int currentIndex = 0; // Index of the current character.
        while (currentIndex < this.calendarTemplate.length) {
            if ((this.calendarTemplate[currentIndex] == this.mpre[0]) &&
                (this.calendarTemplate[currentIndex + 1]) == this.mpre[1]) {
                // Start token.
                currentIndex = currentIndex + 2; // Jump over the token start characters.
                currentIndex = this.processCalendarToken(currentIndex); // Process the token.
            } else {
                // Pass through.
                this.markupOut.append(this.calendarTemplate[currentIndex]); // Copy the character to the output.
                currentIndex++; // Get the next character.
            }
        }

        this.mout.write(this.markupOut.toString().getBytes()); // Write the markup to the output file.
        this.mout.close(); // Close the file.
    }

    /**
     * Load the templates.
     *
     * Ref: https://stackoverflow.com/questions/21980090/javas-randomaccessfile-eofexception
     *
     * @throws FileNotFoundException If a template file cannot be found.
     * @throws IOException If a problem occurs when reading a template file.
     */
    private void loadTemplates() throws FileNotFoundException, IOException {
        // Calendar template.
        java.io.File templateFile = new java.io.File("CalendarTemplate.txt"); // Using the File object so that we can get the length.

        char[] buffer = new char[(int) templateFile.length()]; // Buffer to store the read characters.

        java.io.FileInputStream fin = new java.io.FileInputStream(templateFile); // Stream to read the file.
        // Reader to read the file that is encoded with UTF-8 characters.
        java.io.InputStreamReader isr = new java.io.InputStreamReader(fin, "UTF-8");

        isr.read(buffer); // Read the file into the buffer.
        isr.close(); // Close the file.

        // To allow us to convert the bytes into characters.
        StringBuilder sb = new StringBuilder((int) templateFile.length());
        sb.append(buffer);

        this.calendarTemplate = sb.toString().toCharArray(); // Convert into a string and then an array of chars.

        // Month template.
        // Same processing as the Calendar Template.
        templateFile = new java.io.File("MonthTemplate.txt");
        buffer = new char[(int) templateFile.length()];

        fin = new java.io.FileInputStream(templateFile);
        isr = new java.io.InputStreamReader(fin, "UTF-8");

        isr.read(buffer);
        isr.close();

        sb = new StringBuilder((int) templateFile.length());
        sb.append(buffer);

        this.monthTemplate = sb.toString().toCharArray();
    }

    /**
     * Process a token in the Calendar template.
     * 
     * @param currentIndex The current character in the calendar template.
     * @return The updated position in the template after processing the token so that we can continue.
     */
    private int processCalendarToken(int currentIndex) {
        int end = this.calendarTemplate.length;
        StringBuilder token = new StringBuilder();

        // Extract the whole token until the end token characters are reached/
        while (currentIndex < end) {
            if ((this.calendarTemplate[currentIndex] == this.mpost[0]) &&
                (this.calendarTemplate[currentIndex + 1]) == this.mpost[1]) {
                // End token.
                currentIndex = currentIndex + 2;
                end = currentIndex; // Exit the loop.
            } else {
                // Characters of the token.
                token.append(this.calendarTemplate[currentIndex]);
                currentIndex++;
            }
        }
        this.processCalendarToken(token.toString()); // Process the token.

        return currentIndex;
    }

    /**
     * Process the extracted token. 
     *
     * @param token The token to process.
     */
    private void processCalendarToken(String token) {
        int dataIndex = token.indexOf('-'); // Do we have 'parameter' data in the token?
        String data = null;
        String dataExtra = null;
        if (dataIndex != -1) {
            // We have data, so extract it.
            data = token.substring(dataIndex + 1, token.length());
            token = token.substring(0, dataIndex);

            int ampIndex = data.indexOf('&'); // Do we have a second parameter data in the token?
            if (ampIndex != -1) {
                dataExtra = data.substring(ampIndex + 1, data.length());
                data = data.substring(0, ampIndex);
            }
        }

        // Identify and execute the action of the token with its data if any.
        // Rule switch, Java 12 - https://blogs.oracle.com/javamagazine/post/new-switch-expressions-in-java-12
        switch (token) {
            case "calendartitle" -> this.markupOut.append(this.gc.get(Calendar.YEAR)).append(" Calendar");
            case "jan" -> this.monthTemplate(Calendar.JANUARY, data, dataExtra);
            case "feb" -> this.monthTemplate(Calendar.FEBRUARY, data, dataExtra);
            case "mar" -> this.monthTemplate(Calendar.MARCH, data, dataExtra);
            case "apr" -> this.monthTemplate(Calendar.APRIL, data, dataExtra);
            case "may" -> this.monthTemplate(Calendar.MAY, data, dataExtra);
            case "jun" -> this.monthTemplate(Calendar.JUNE, data, dataExtra);
            case "jul" -> this.monthTemplate(Calendar.JULY, data, dataExtra);
            case "aug" -> this.monthTemplate(Calendar.AUGUST, data, dataExtra);
            case "sep" -> this.monthTemplate(Calendar.SEPTEMBER, data, dataExtra);
            case "oct" -> this.monthTemplate(Calendar.OCTOBER, data, dataExtra);
            case "nov" -> this.monthTemplate(Calendar.NOVEMBER, data, dataExtra);
            case "dec" -> this.monthTemplate(Calendar.DECEMBER, data, dataExtra);
            default -> this.markupOut.append("<p>Calendar error!  Unknown token.</p>");
        }
    }

    /**
     * Execute a month token by processing the month template with the supplied parameters.
     *
     * @param theMonth The month.
     * @param imageName The name of the image for the month.
     * @param imageDescription The description of the image for the month.
     */
    private void monthTemplate(int theMonth, String imageName, String imageDescription) {
        this.gc.set(Calendar.MONTH, theMonth); // Tell the calendar the month we want so that it tells us the correct days.
        this.previousMonth = theMonth;
        this.currentMonth = theMonth;

        int currentIndex = 0; // Current character in the month template.
        while (currentIndex < this.monthTemplate.length) {
            if ((this.monthTemplate[currentIndex] == this.mpre[0]) && (this.monthTemplate[currentIndex + 1]) == this.mpre[1]) {
                // Start token.
                currentIndex = currentIndex + 2;
                currentIndex = this.processMonthToken(currentIndex, imageName, imageDescription);
            } else {
                // Pass through.
                this.markupOut.append(this.monthTemplate[currentIndex]);
                currentIndex++;
            }
        }
    }

    /**
     * Execute a month token.
     *
     * @param currentIndex The current character in the month template.
     * @param imageName The name of the image for the month.
     * @param imageDescription The description of the image for the month.
     */
    private int processMonthToken(int currentIndex, String imageName, String imageDescription) {
        int end = this.monthTemplate.length;
        StringBuilder token = new StringBuilder();
        while (currentIndex < end) {
            if ((this.monthTemplate[currentIndex] == this.mpost[0]) && (this.monthTemplate[currentIndex + 1]) == this.mpost[1]) {
                // End token.
                currentIndex = currentIndex + 2;
                end = currentIndex; // Exit the loop.
            } else {
                // Characters of the token.
                token.append(this.monthTemplate[currentIndex]);
                currentIndex++;
            }
        }
        this.processMonthToken(token.toString(), imageName, imageDescription);

        return currentIndex;
    }

    /**
     * Process a month token.
     *
     * @param token The month token.
     * @param imageName The name of the image for the month.
     * @param imageDescription The description of the image for the month.
     */
    private void processMonthToken(String token, String imageName, String imageDescription) {
        int dataIndex = token.indexOf('-');
        String data = null;
        if (dataIndex != -1) {
            // We have data.
            data = token.substring(dataIndex + 1, token.length());
            token = token.substring(0, dataIndex);
        }

        switch (token) {
            case "monthtitle" -> this.markupOut.append(this.getMonthText(gc.get(Calendar.MONTH)));
            case "monthdaynames" -> this.monthDayNames(data);
            case "monthweek" -> this.monthWeek(data);
            case "monthimage" -> this.monthImage(imageName);
            case "monthimagedescription" -> this.monthImage(imageDescription);
            default -> this.markupOut.append("<p>Month error!  Unknown token.</p>");
        }
    }

    /**
     * Put the month day in the markup output.
     * @param data The token parameter, which is the wrapper html around the day text.
     */
    private void monthDayNames(String data) {
        int starIndex = data.indexOf('*'); // Where the day text should be placed in the wrapper markup.
        String pre = data.substring(0, starIndex); // Wrapper opening tag.
        String post = data.substring(starIndex + 1, data.length()); // Wrapper closing tag.

        // Output the days of the week in the order that we have set.
        Iterator<Integer> daysIt = this.days.iterator();
        Integer current;
        while (daysIt.hasNext()) {
            current = daysIt.next();
            this.monthDay(this.getDayText(current), pre, post);
        }
    }

    /**
     * Output a day as an integer wrapped within the pre and post wrapper markup, the opening / closing tags.
     *
     * @param day The day.
     * @param pre The opening wrapper tag.
     * @param post The closing wrapper tag.
     */
    private void monthDay(Integer day, String pre, String post) {
        this.monthDay(day.toString(), pre, post);
    }

    /**
     * Output a day as text wrapped within the pre and post wrapper markup, the opening / closing tags.
     *
     * @param day The day.
     * @param pre The opening wrapper tag.
     * @param post The closing wrapper tag.
     */
    private void monthDay(String day, String pre, String post) {
        this.markupOut.append(pre).append(day).append(post);
    }

    /**
     * 
     * @param data 
     */
    private void monthWeek(String data) {
        int exlamationIndex = data.indexOf('!');
        int ampIndex = data.indexOf('&');
        int starIndex = data.indexOf('*');

        String weekPre = data.substring(0, exlamationIndex);
        String weekPost = data.substring(exlamationIndex + 1, ampIndex);

        String dayPre = data.substring(ampIndex + 1, starIndex);
        String dayPost = data.substring(starIndex + 1, data.length());

        int currentPosition = 1;
        boolean startDayReached = false;

        while (this.currentMonth == this.previousMonth) {
            this.markupOut.append(weekPre);

            if (startDayReached == false) {
                Iterator<Integer> daysIt = this.days.iterator();
                int monthStartPostion = this.gc.get(Calendar.DAY_OF_WEEK); // Day of the week that the month starts on.
                Integer current;
                
                while (daysIt.hasNext() && (startDayReached == false)) {
                    current = daysIt.next();
                    if (current == monthStartPostion) {
                        startDayReached = true;
                    } else {
                        currentPosition++;
                        this.monthDay("", dayPre, dayPost);
                    }
                }
            }

            while (currentPosition < 8) {

                if (this.currentMonth != this.previousMonth) {
                    if (currentPosition != 1) {
                        while (currentPosition < 8) {
                            this.monthDay("", dayPre, dayPost);
                            currentPosition++;
                        }
                    }
                } else {
                    this.monthDay(this.gc.get(Calendar.DAY_OF_MONTH), dayPre, dayPost);
                    this.gc.add(Calendar.DAY_OF_MONTH, 1);
                    this.currentMonth = this.gc.get(Calendar.MONTH);

                    currentPosition++;
                }
            }
            currentPosition = 1;

            this.markupOut.append(weekPost);
        }
    }

    private void monthImage(String text) {
        if (text != null) {
            this.markupOut.append(text);
        }
    }

    private String getMonthText(int theMonth) {
        return switch (theMonth) {
            case Calendar.JANUARY -> "January";
            case Calendar.FEBRUARY -> "February";
            case Calendar.MARCH -> "March";
            case Calendar.APRIL -> "April";
            case Calendar.MAY -> "May";
            case Calendar.JUNE -> "June";
            case Calendar.JULY -> "July";
            case Calendar.AUGUST -> "August";
            case Calendar.SEPTEMBER -> "September";
            case Calendar.OCTOBER -> "October";
            case Calendar.NOVEMBER -> "November";
            case Calendar.DECEMBER -> "December";
            default -> "Unknown";
        };
    }

    private String getDayText (int theDay) {
        return switch (theDay) {
            case Calendar.SUNDAY -> "Sun";
            case Calendar.MONDAY -> "Mon";
            case Calendar.TUESDAY -> "Tue";
            case Calendar.WEDNESDAY -> "Wed";
            case Calendar.THURSDAY -> "Thu";
            case Calendar.FRIDAY -> "Fri";
            case Calendar.SATURDAY -> "Sat";
            default -> "Unknown";
        };
    }
}

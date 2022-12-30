/*
 * CalGen.
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
    private final GregorianCalendar gc = new GregorianCalendar();
    private final LinkedList<Integer> days = new LinkedList<>();
    private int currentMonth;
    private int nextMonth;
    private final boolean calendarStartsOnSunday;
    private final int calendarStartDay;
    private final int theYear;

    // Template attributes.
    private char[] calendarTemplate;
    private char[] monthTemplate = null;

    private final char[] mpre = {'{', '{'};
    private final char[] mpost = {'}', '}'};
    private final FileOutputStream mout;
    private final StringBuffer markupOut = new StringBuffer();

    /**
     * @param args the command line arguments - not used.
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws FileNotFoundException, IOException {
        CalGen us = new CalGen();

        us.calendar();
        us.calendarTemplate();
    }

    public CalGen() throws FileNotFoundException {
        this.gc.setFirstDayOfWeek(Calendar.TUESDAY); // Change to SUNDAY if wished.
        this.calendarStartDay = this.gc.getFirstDayOfWeek();
        this.calendarStartsOnSunday = (this.calendarStartDay == Calendar.SUNDAY);

        this.theYear = this.gc.get(Calendar.YEAR) + 1;
        this.mout = new FileOutputStream("./calm.html");

        days.add(Calendar.SUNDAY);
        days.add(Calendar.MONDAY);
        days.add(Calendar.TUESDAY);
        days.add(Calendar.WEDNESDAY);
        days.add(Calendar.THURSDAY);
        days.add(Calendar.FRIDAY);
        days.add(Calendar.SATURDAY);

        Iterator<Integer> daysIt = days.iterator();
        boolean found = false;
        int count = 0;
        Integer current;
        int firstDayOfWeek = this.gc.getFirstDayOfWeek();

        while (daysIt.hasNext() && found == false) {
            current = daysIt.next();
            if (current == firstDayOfWeek) {
                found = true;
            } else {
                count++;
            }
        }

        if (count > 0) {
            Collections.rotate(days, -count);
        }
    }

    public void calendar() throws IOException, FileNotFoundException {
        this.gc.set(theYear, 0, 1);

        System.out.println(this.gc.get(Calendar.YEAR));


        for (int theMonths = 0; theMonths < 12; theMonths++) {
            this.month(theMonths);
            System.out.println();
        }
    }

    private void month(int theMonth) {
        int monthStartPostion;
        int currentPosition;

        this.gc.set(Calendar.MONTH, theMonth);
        this.nextMonth = theMonth;
        this.currentMonth = this.nextMonth;

        System.out.println(this.getMonthText(gc.get(Calendar.MONTH)));

        /*if (!this.calendarStartsOnSunday) {
            this.day("Mon");
            System.out.print(" ");
            this.day("Tue");
            System.out.print(" ");
            this.day("Wed");
            System.out.print(" ");
            this.day("Thu");
            System.out.print(" ");
            this.day("Fri");
            System.out.print(" ");
            this.day("Sat");
            System.out.print(" ");
            this.day("Sun");
            System.out.println();
        } else {
            this.day("Sun");
            System.out.print(" ");
            this.day("Mon");
            System.out.print(" ");
            this.day("Tue");
            System.out.print(" ");
            this.day("Wed");
            System.out.print(" ");
            this.day("Thu");
            System.out.print(" ");
            this.day("Fri");
            System.out.print(" ");
            this.day("Sat");
            System.out.println();
        } */
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

        currentPosition = 1;
        daysIt = this.days.iterator();
        boolean startDayReached = false;
        monthStartPostion = this.gc.get(Calendar.DAY_OF_WEEK); // Day of the week that the month starts on.

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

        //if (!this.calendarStartsOnSunday) {
            // Work out where that day is in our row, its position.
            /*monthStartPostion = monthStartPostion - (this.calendarStartDay - 1);
            if (monthStartPostion < 1) {
                monthStartPostion = 8 - (this.calendarStartDay - 1);
            }
            startDayReached = (1 == monthStartPostion); */  // Position one is the start day of this month.
        //}
        
        while (this.currentMonth == this.nextMonth) {
            while (currentPosition < 8) {
                /*currentDayOfWeek = this.gc.get(Calendar.DAY_OF_WEEK) - this.startDay;
                if (currentDayOfWeek < 1) {
                    currentDayOfWeek = 7 - this.startDay;
                } */

                /*currentDayOfWeek = this.gc.get(Calendar.DAY_OF_WEEK);
                if (!this.startOnSunday) {
                    currentDayOfWeek = currentDayOfWeek - (this.startDay - 1);
                    if (currentDayOfWeek < 1) {
                        currentDayOfWeek = 8 - (this.startDay - 1);
                    }
                }*/

                /*currentDayOfWeek = this.gc.get(Calendar.DAY_OF_WEEK);
                if (!this.startOnSunday) {
                    //currentDayOfWeek--;
                    currentDayOfWeek = currentDayOfWeek - (this.startDay - 1);
                    //currentDayOfWeek = currentPrintedDay - (this.startDay - 1);
                    if (currentDayOfWeek < 1) {
                        currentDayOfWeek = 8 - (this.startDay - 1);
                    }
                }*/

                if (this.currentMonth != this.nextMonth) {
                    if (currentPosition != 1) {
                        while (currentPosition < 8) {
                            this.day("");
                            System.out.print(" -* ");
                            currentPosition++;
                        }
                    }
                /*} else if (!startDayReached) {
                    this.day("");
                    System.out.print(" -! ");
                    if ((currentPosition + 1) == monthStartPostion) {
                        startDayReached = true;
                    }*/
                } else {
                    if (this.gc.get(Calendar.DAY_OF_MONTH) > 9) {
                        System.out.print(" ");
                    } else {
                        System.out.print("  ");
                    }
                    this.day(this.gc.get(Calendar.DAY_OF_MONTH));
                    System.out.print(" ");
                    this.gc.add(Calendar.DAY_OF_MONTH, 1);
                    this.nextMonth = this.gc.get(Calendar.MONTH);

                    currentPosition++;
                }
            }
            currentPosition = 1;
            System.out.println();
        }
    }

    private void day(Integer day) {
        this.day(day.toString());
    }

    private void day(String day) {
        System.out.print(day);
    }

    public void calendarTemplate() throws FileNotFoundException, IOException {
        // Reset.
        this.gc.set(theYear, 0, 1);
        try (this.mout) {
            this.loadTemplates();
            int currentIndex = 0;

            while (currentIndex < this.calendarTemplate.length) {
                if ((this.calendarTemplate[currentIndex] == this.mpre[0]) && (this.calendarTemplate[currentIndex + 1]) == this.mpre[1]) {
                    // Start token.
                    currentIndex = currentIndex + 2;
                    currentIndex = this.processCalendarToken(currentIndex);
                } else {
                    // Pass through.
                    this.markupOut.append(this.calendarTemplate[currentIndex]);
                    currentIndex++;
                }
            }

            this.mout.write(this.markupOut.toString().getBytes());
        }
    }

    /**
     * Load the templates.
     *
     * Ref: https://stackoverflow.com/questions/21980090/javas-randomaccessfile-eofexception
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void loadTemplates() throws FileNotFoundException, IOException {
        // Calendar template.
        java.io.File templateFile = new java.io.File("CalendarTemplate.txt");

        char[] buffer = new char[(int) templateFile.length()];

        java.io.FileInputStream fin = new java.io.FileInputStream(templateFile);
        java.io.InputStreamReader isr = new java.io.InputStreamReader(fin, "UTF-8");

        isr.read(buffer);
        isr.close();

        StringBuilder sb = new StringBuilder((int) templateFile.length());
        sb.append(buffer);

        this.calendarTemplate = sb.toString().toCharArray();

        // Month template.
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

    private int processCalendarToken(int currentIndex) {
        int end = this.calendarTemplate.length;
        StringBuilder token = new StringBuilder();
        while (currentIndex < end) {
            if ((this.calendarTemplate[currentIndex] == this.mpost[0]) && (this.calendarTemplate[currentIndex + 1]) == this.mpost[1]) {
                // End token.
                currentIndex = currentIndex + 2;
                end = currentIndex; // Exit the loop.
            } else {
                // Characters of the token.
                token.append(this.calendarTemplate[currentIndex]);
                currentIndex++;
            }
        }
        this.processCalendarToken(token.toString());

        return currentIndex;
    }

    private void processCalendarToken(String token) {
        int dataIndex = token.indexOf('-');
        String data = null;
        String dataExtra = null;
        if (dataIndex != -1) {
            // We have data.
            data = token.substring(dataIndex + 1, token.length());
            token = token.substring(0, dataIndex);

            int ampIndex = data.indexOf('&');
            if (ampIndex != 1) {
                dataExtra = data.substring(ampIndex + 1, data.length());
                data = data.substring(0, ampIndex);
            }
        }

        // Rule switch, Java 12 - https://blogs.oracle.com/javamagazine/post/new-switch-expressions-in-java-12
        switch (token) {
            case "calendartitle" -> this.markupOut.append(this.gc.get(Calendar.YEAR)).append(" Calendar");
            case "jan" -> this.monthTemplate(0, data, dataExtra);
            case "feb" -> this.monthTemplate(1, data, dataExtra);
            case "mar" -> this.monthTemplate(2, data, dataExtra);
            case "apr" -> this.monthTemplate(3, data, dataExtra);
            case "may" -> this.monthTemplate(4, data, dataExtra);
            case "jun" -> this.monthTemplate(5, data, dataExtra);
            case "jul" -> this.monthTemplate(6, data, dataExtra);
            case "aug" -> this.monthTemplate(7, data, dataExtra);
            case "sep" -> this.monthTemplate(8, data, dataExtra);
            case "oct" -> this.monthTemplate(9, data, dataExtra);
            case "nov" -> this.monthTemplate(10, data, dataExtra);
            case "dec" -> this.monthTemplate(11, data, dataExtra);
            default -> this.markupOut.append("<p>Calendar error!</p>");
        }
    }

    private void monthTemplate(int theMonth, String imageName, String imageDescription) {
        this.gc.set(Calendar.MONTH, theMonth);
        this.nextMonth = theMonth;
        this.currentMonth = this.nextMonth;

        int currentIndex = 0;

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
            default -> this.markupOut.append("<p>Month error!</p>");
        }
    }

    private void monthDayNames(String data) {
        int starIndex = data.indexOf('*');
        String pre = data.substring(0, starIndex);
        String post = data.substring(starIndex + 1, data.length());

        if (!this.calendarStartsOnSunday) {
            this.monthDay("Mon", pre, post);
            this.monthDay("Tue", pre, post);
            this.monthDay("Wed", pre, post);
            this.monthDay("Thu", pre, post);
            this.monthDay("Fri", pre, post);
            this.monthDay("Sat", pre, post);
            this.monthDay("Sun", pre, post);
        } else {
            this.monthDay("Sun", pre, post);
            this.monthDay("Mon", pre, post);
            this.monthDay("Tue", pre, post);
            this.monthDay("Wed", pre, post);
            this.monthDay("Thu", pre, post);
            this.monthDay("Fri", pre, post);
            this.monthDay("Sat", pre, post);
        }
    }

    private void monthDay(Integer day, String pre, String post) {
        this.monthDay(day.toString(), pre, post);
    }

    private void monthDay(String day, String pre, String post) {
        this.markupOut.append(pre).append(day).append(post);
    }

    private void monthWeek(String data) {
        int exlamationIndex = data.indexOf('!');
        int ampIndex = data.indexOf('&');
        int starIndex = data.indexOf('*');

        String weekPre = data.substring(0, exlamationIndex);
        String weekPost = data.substring(exlamationIndex + 1, ampIndex);

        String dayPre = data.substring(ampIndex + 1, starIndex);
        String dayPost = data.substring(starIndex + 1, data.length());

        int currentDayOfWeek;
        int currentPrintedDay;

        while (this.currentMonth == this.nextMonth) {

            this.markupOut.append(weekPre);

            for (currentPrintedDay = 1; currentPrintedDay < 8; currentPrintedDay++) {
                currentDayOfWeek = this.gc.get(Calendar.DAY_OF_WEEK);
                if (!this.calendarStartsOnSunday) {
                    currentDayOfWeek--;
                    if (currentDayOfWeek < 1) {
                        currentDayOfWeek = 7;
                    }
                }

                if (currentPrintedDay != currentDayOfWeek) {
                    this.monthDay("", dayPre, dayPost);
                } else {
                    this.monthDay(this.gc.get(Calendar.DAY_OF_MONTH), dayPre, dayPost);
                    this.gc.add(Calendar.DAY_OF_MONTH, 1);
                    this.nextMonth = this.gc.get(Calendar.MONTH);
                }
                if (this.currentMonth != this.nextMonth) {
                    if (currentPrintedDay != 1) {
                        currentPrintedDay++;
                        while (currentPrintedDay < 8) {
                            this.monthDay("", dayPre, dayPost);
                            currentPrintedDay++;
                        }
                    }
                }
            }
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

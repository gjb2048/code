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

/**
 * CalGen class.
 *
 * @author G J Barnard
 */
public class CalGen {

    private final GregorianCalendar gc = new GregorianCalendar();
    private int currentMonth;
    private int nextMonth;
    private final boolean startOnMonday;
    private final int theYear;

    private final FileOutputStream fout;

    // Temp internal HTML.
    private final String prestart = "<!doctype html><html><head>"
            + "<style type=\"text/css\">"
            + ".cal-row {display: flex; flex-wrap: wrap; justify-content: space-between; margin: 10px; }"
            + ".cal-row.nowrap {flex-wrap: nowrap; }"
            + ".cal-1 { flex-basis: 100%;  text-align: center; }"
            + ".cal-4 { flex-basis: 25%; }"
            + ".cal-7 { flex-basis: 14.28%; text-align: end; }"
            + "* { font-family: sans-serif; }"
            + "</style>";
    private final String preend = "</head><body>";
    private final String post = "</body></html>";
    private final StringBuffer markup = new StringBuffer();

    // Temp template markup.
    private final String calMarkup = "<!doctype html><html><head>"
            + "<style type=\"text/css\">"
            + ".cal-row {display: flex; flex-wrap: wrap; justify-content: space-between; margin: 10px; }"
            + ".cal-row.nowrap {flex-wrap: nowrap; }"
            + ".cal-1 { flex-basis: 100%;  text-align: center; }"
            + ".cal-4 { flex-basis: 25%; }"
            + ".cal-7 { flex-basis: 14.28%; text-align: end; }"
            + "* { font-family: sans-serif; }"
            + "</style>"
            + "<title>{{calendartitle}}</title>"
            + "</head><body>"
            + "<h1 style=\"text-align: center;\">{{calendartitle}}</h1>"
            + "<div class=\"cal-row\">"
            + "{{jan}}"
            + "{{feb}}"
            + "{{mar}}"
            + "{{apr}}"
            + "{{may}}"
            + "{{jun}}"
            + "{{jul}}"
            + "{{aug}}"
            + "{{sep}}"
            + "{{oct}}"
            + "{{nov}}"
            + "{{dec}}"
            + "</div>"
            + "</body></html>";
    private char[] calendarTemplate;

    private final String monthMarkup = "<div class=\"cal-4\">"
            + "<div class=\"cal-row nowrap month\">"
            + "<div class=\"cal-1 monthtitle\">{{monthtitle}}</div>"
            + "</div>"
            + "<div class=\"cal-row nowrap monthdaynames\">"
            + "{{monthdaynames-<div class=\"cal-7\">*</div>}}"
            + "</div>"
            + "{{monthweek-<div class=\"cal-row nowrap monthweek\">!</div>&<div class=\"cal-7 monthday\">*</div>}}"
            + "</div>";
            /*+ "<div class=\"cal-row\">"
            + "{{monthdays-<div class=\"cal-7\">*</div>}}"
            + "</div>";*/
    private char[] monthTemplate = null;
    
    private final char[] mpre = {'{', '{'};
    private final char[] mpost = {'}', '}'};
    private final FileOutputStream mout;
    private final StringBuffer markupOut = new StringBuffer();

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String args[]) throws FileNotFoundException, IOException {
        CalGen us = new CalGen();

        us.calendar();
        us.calendarTemplate();
    }

    public CalGen() throws FileNotFoundException {
        this.gc.setFirstDayOfWeek(Calendar.MONDAY);
        this.startOnMonday = (this.gc.getFirstDayOfWeek() == Calendar.MONDAY);

        this.theYear = this.gc.get(Calendar.YEAR) + 1;
        this.fout = new FileOutputStream("./cala.html");
        this.mout = new FileOutputStream("./calm.html");
    }

    public void calendar() throws IOException, FileNotFoundException {
        this.gc.set(theYear, 0, 1);

        try (this.fout) {
            System.out.println(this.gc.get(Calendar.YEAR));

            this.markup.append(this.prestart);
            this.markup.append("<title>").append(this.gc.get(Calendar.YEAR)).append(" Calendar</title>");
            this.markup.append(this.preend);
            this.markup.append("<h1 style=\"text-align: center;\">").append(this.gc.get(Calendar.YEAR)).append(" Calendar").append("</h1>");

            this.markup.append("<div class=\"cal-row\">");

            for (int theMonths = 0; theMonths < 12; theMonths++) {
                this.month(theMonths);
                System.out.println();
            }

            this.markup.append("</div>");

            this.markup.append(this.post);
            this.fout.write(this.markup.toString().getBytes());
        }
    }

    public void calendarTemplate() throws IOException {
        // Reset.
        this.gc.set(theYear, 0, 1);
        try (this.mout) {
            this.calendarTemplate = this.calMarkup.toCharArray();
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
        switch (token) {
            case "calendartitle":
                this.markupOut.append(this.gc.get(Calendar.YEAR)).append(" Calendar");
                break;
            case "jan":
                this.monthTemplate(0);
                break;
            case "feb":
                this.monthTemplate(1);
                break;
            case "mar":
                this.monthTemplate(2);
                break;
            case "apr":
                this.monthTemplate(3);
                break;
            case "may":
                this.monthTemplate(4);
                break;
            case "jun":
                this.monthTemplate(5);
                break;
            case "jul":
                this.monthTemplate(6);
                break;
            case "aug":
                this.monthTemplate(7);
                break;
            case "sep":
                this.monthTemplate(8);
                break;
            case "oct":
                this.monthTemplate(9);
                break;
            case "nov":
                this.monthTemplate(10);
                break;
            case "dec":
                this.monthTemplate(11);
                break;
            default:
                this.markupOut.append("<p>Error!</p>");
        }
    }

    private void month(int theMonth) {
        int currentDayOfWeek;
        int currentPrintedDay;

        this.gc.set(Calendar.MONTH, theMonth);
        this.nextMonth = theMonth;
        this.currentMonth = this.nextMonth;

        this.markup.append("<div class=\"cal-4\">");
        this.markup.append("<div class=\"cal-row nowrap\">");
        this.markup.append("<div class=\"cal-1\">").append(this.getMonthText(gc.get(Calendar.MONTH))).append("</div>");
        this.markup.append("</div>");

        this.markup.append("<div class=\"cal-row nowrap\">");
        System.out.println(this.getMonthText(gc.get(Calendar.MONTH)));

        if (this.startOnMonday) {
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
        }
        this.markup.append("</div>");

        this.markup.append("<div class=\"cal-row\">");
        while (this.currentMonth == this.nextMonth) {

            for (currentPrintedDay = 1; currentPrintedDay < 8; currentPrintedDay++) {
                currentDayOfWeek = this.gc.get(Calendar.DAY_OF_WEEK);
                if (this.startOnMonday) {
                    currentDayOfWeek--;
                    if (currentDayOfWeek < 1) {
                        currentDayOfWeek = 7;
                    }
                }

                //System.out.println("Date = MON: " + gc.get(Calendar.MONTH) + " DOM: " + gc.get(Calendar.DAY_OF_MONTH) +
                //    " DOW: " + gc.get(Calendar.DAY_OF_WEEK) + " currentDayOfWeek = " + currentDayOfWeek + " currentPrintedDay = " + currentPrintedDay);
                if (currentPrintedDay != currentDayOfWeek) {
                    this.day("");
                    System.out.print(" -! ");
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
                }
                if (this.currentMonth != this.nextMonth) {
                    if (currentPrintedDay != 1) {
                        currentPrintedDay++;
                        while (currentPrintedDay < 8) {
                            this.day("");
                            System.out.print(" -* ");
                            currentPrintedDay++;
                        }
                    }
                }
            }
            System.out.println();
        }
        this.markup.append("</div>");
        this.markup.append("</div>");
    }

    private void day(Integer day) {
        this.day(day.toString());
    }

    private void day(String day) {
        System.out.print(day);
        this.markup.append("<div class=\"cal-7\">");
        this.markup.append(day);
        this.markup.append("</div>");
    }

    private void monthTemplate(int theMonth) {
        this.gc.set(Calendar.MONTH, theMonth);
        this.nextMonth = theMonth;
        this.currentMonth = this.nextMonth;

        if (this.monthTemplate == null) {
            this.monthTemplate = this.monthMarkup.toCharArray();
        }
            int currentIndex = 0;

            while (currentIndex < this.monthTemplate.length) {
                if ((this.monthTemplate[currentIndex] == this.mpre[0]) && (this.monthTemplate[currentIndex + 1]) == this.mpre[1]) {
                    // Start token.
                    currentIndex = currentIndex + 2;
                    currentIndex = this.processMonthToken(currentIndex);
                } else {
                    // Pass through.
                    this.markupOut.append(this.monthTemplate[currentIndex]);
                    currentIndex++;
                }
            }
    }

    private int processMonthToken(int currentIndex) {
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
        this.processMonthToken(token.toString());

        return currentIndex;
    }

    private void processMonthToken(String token) {
        int dataIndex = token.indexOf('-');
        String data = null;
        if (dataIndex != -1) {
            // We have data.
            data = token.substring(dataIndex + 1, token.length());
            token = token.substring(0, dataIndex);
        }
        
        switch (token) {
            case "monthtitle":
                this.markupOut.append(this.getMonthText(gc.get(Calendar.MONTH)));
                break;
            case "monthdaynames":
                this.monthDayNames(data);
                break;
            case "monthweek":
                this.monthWeek(data);
                break;
            default:
                this.markupOut.append("<p>Error!</p>");
        }
    }

    /* private void monthTitle() {
        this.markupOut.append("<div class=\"cal-row nowrap\">");
        this.markupOut.append("<div class=\"cal-1\">").append(this.getMonthText(gc.get(Calendar.MONTH))).append("</div>");
        this.markupOut.append("</div>");
    } */
    
    private void monthDayNames(String data) {
        // "{{monthdaynames-<div class=\"cal-7\">*</div>}}"
        int starIndex = data.indexOf('*');
        String pre = data.substring(0, starIndex);
        String post = data.substring(starIndex + 1, data.length());

        if (this.startOnMonday) {
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
        // "{{monthweek-<div class=\"cal-row nowrap monthweek\">!</div>&<div class=\"cal-7 monthday\">*</div>}}";
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
                if (this.startOnMonday) {
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

    private void dayTemplate(String day) {
        this.dayTemplate(day, true);
    }

    private void dayTemplate(Integer day) {
        this.dayTemplate(day.toString(), true);
    }

    private void dayTemplate(String day, boolean dayToMarkup) {
        if (day == null) {
            this.markupOut.append("<div class=\"cal-7\">").append(this.gc.get(Calendar.DAY_OF_MONTH)).append("</div>");
        } else {
            this.markupOut.append("<div class=\"cal-7\">");
            if (dayToMarkup) {
                this.markupOut.append(day);
            }
            this.markupOut.append("</div>");
        }
    }

    private String getMonthText(int theMonth) {
        String retr;

        retr = switch (theMonth) {
            case Calendar.JANUARY ->
                "January";
            case Calendar.FEBRUARY ->
                "February";
            case Calendar.MARCH ->
                "March";
            case Calendar.APRIL ->
                "April";
            case Calendar.MAY ->
                "May";
            case Calendar.JUNE ->
                "June";
            case Calendar.JULY ->
                "July";
            case Calendar.AUGUST ->
                "August";
            case Calendar.SEPTEMBER ->
                "September";
            case Calendar.OCTOBER ->
                "October";
            case Calendar.NOVEMBER ->
                "November";
            case Calendar.DECEMBER ->
                "December";
            default ->
                "Unknown";
        };

        return retr;
    }

}

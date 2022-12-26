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

    private final FileOutputStream fout;
    private final StringBuffer markup = new StringBuffer();

    // Temp internal HTML.
    private final String prestart = "<!doctype html><html ><head>"
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

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String args[]) throws FileNotFoundException, IOException {
        CalGen us = new CalGen();

        us.calendar();
    }

    public CalGen() throws FileNotFoundException {
        this.gc.setFirstDayOfWeek(Calendar.MONDAY);
        this.startOnMonday = (this.gc.getFirstDayOfWeek() == Calendar.MONDAY);
        this.gc.set(this.gc.get(Calendar.YEAR) + 1, 0, 1);

        this.fout = new FileOutputStream("./cal.html");
    }

    public void calendar() throws IOException {
        System.out.println(this.gc.get(Calendar.YEAR));

        this.markup.append(this.prestart);
        this.markup.append("<title>").append(this.gc.get(Calendar.YEAR)).append(" Calendar </title>");
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
        while (this.currentMonth == this.nextMonth) {
            this.markup.append("<div class=\"cal-row nowrap\">");
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
                    this.day(" -! ", false);
                } else {
                    if (this.gc.get(Calendar.DAY_OF_MONTH) > 9) {
                        System.out.print(" ");
                    } else {
                        System.out.print("  ");
                    }
                    this.day("" + this.gc.get(Calendar.DAY_OF_MONTH));
                    System.out.print(" ");
                    this.gc.add(Calendar.DAY_OF_MONTH, 1);
                    this.nextMonth = this.gc.get(Calendar.MONTH);
                }
                if (this.currentMonth != this.nextMonth) {
                    if (currentPrintedDay != 1) {
                        currentPrintedDay++;
                        while (currentPrintedDay < 8) {
                            this.day(" -* ", false);
                            currentPrintedDay++;
                        }
                    }
                }
            }
            System.out.println();
            this.markup.append("</div>");
        }
        this.markup.append("</div>");
    }

    private void day(String day) {
        this.day(day, true);
    }

    private void day(String day, boolean dayToMarkup) {
        if (day == null) {
            System.out.print(this.gc.get(Calendar.DAY_OF_MONTH));
            this.markup.append("<div class=\"cal-7\">").append(this.gc.get(Calendar.DAY_OF_MONTH)).append("</div>");
        } else {
            System.out.print(day);
            this.markup.append("<div class=\"cal-7\">");
            if (dayToMarkup) {
                this.markup.append(day);
            }
            this.markup.append("</div>");
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

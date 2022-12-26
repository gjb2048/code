/*
 * CalGen.
 *
 * @copyright  2022 G J Barnard
 * @license    http://www.gnu.org/copyleft/gpl.html GNU GPL v3 or later
 */

import java.util.Calendar;
import java.util.GregorianCalendar;

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

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        CalGen us = new CalGen();

        us.calendar();
    }

    public CalGen() {
        this.gc.setFirstDayOfWeek(Calendar.MONDAY);
        this.startOnMonday = (this.gc.getFirstDayOfWeek() == Calendar.MONDAY);
        this.gc.set(this.gc.get(Calendar.YEAR) + 1, 0, 1);
    }

    public void calendar() {
        System.out.println(this.gc.get(Calendar.YEAR));
        for (int theMonths = 0; theMonths < 12; theMonths++) {
            this.month(theMonths);
            System.out.println();
        }
    }

    private void month(int theMonth) {
        int currentDayOfWeek;
        int currentPrintedDay;

        this.gc.set(Calendar.MONTH, theMonth);
        this.nextMonth = theMonth;
        this.currentMonth = this.nextMonth;

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
            System.out.println("Sun Mon Tue Wed Thu Fri Sat");
        }
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
                    this.day(" -! ");
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
                            this.day(" -* ");
                            currentPrintedDay++;
                        }
                    }
                }
            }
            System.out.println();
        }
    }
    
    private void day(String day) {
        if (day == null) {
            System.out.print(this.gc.get(Calendar.DAY_OF_MONTH));
        } else {
            System.out.print(day);
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

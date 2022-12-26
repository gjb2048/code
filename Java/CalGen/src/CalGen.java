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
        this.nextMonth = gc.get(Calendar.MONTH);
        for (int theMonths = 0; theMonths < 12; theMonths++) {
            this.currentMonth = this.nextMonth;

            this.month();

            System.out.println();
        }
    }

    private void month() {
        int currentDayOfWeek;
        int currentPrintedDay;

        System.out.println(this.getMonthText(gc.get(Calendar.MONTH)) + " " + this.gc.get(Calendar.YEAR));
        if (this.startOnMonday) {
            System.out.println("Mon Tue Wed Thu Fri Sat Sun");
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
                    System.out.print(" -! ");
                } else {
                    if (this.gc.get(Calendar.DAY_OF_MONTH) > 9) {
                        System.out.print(" ");
                    } else {
                        System.out.print("  ");
                    }
                    System.out.print(this.gc.get(Calendar.DAY_OF_MONTH) + " ");
                    this.gc.add(Calendar.DAY_OF_MONTH, 1);
                    this.nextMonth = this.gc.get(Calendar.MONTH);
                }
                if (this.currentMonth != this.nextMonth) {
                    if (currentPrintedDay != 1) {
                        currentPrintedDay++;
                        while (currentPrintedDay < 8) {
                            System.out.print(" -* ");
                            currentPrintedDay++;
                        }
                    }
                }
            }
            System.out.println();
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

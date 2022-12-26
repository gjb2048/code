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
 * @author G J Barnard
 */
public class CalGen {

    private GregorianCalendar gc = new GregorianCalendar();

        
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        CalGen us = new CalGen();
        
        us.calendar();
    }

    public CalGen() {
    }

    public void calendar() {
        int currentDayOfWeek;
        int currentMonth;
        int nextMonth;
        int currentPrintedDay;
        int dmonths = 0;
    
        this.gc.setFirstDayOfWeek(Calendar.MONDAY);
        boolean startOnMonday = (this.gc.getFirstDayOfWeek() == Calendar.MONDAY);

        nextMonth = gc.get(Calendar.MONTH);
        currentMonth = nextMonth - 1;
        while (dmonths <= 12) {
            for (currentPrintedDay = 1; currentPrintedDay < 8; currentPrintedDay++) {
                if (currentMonth != nextMonth) {
                    if (currentPrintedDay != 1) {
                        while (currentPrintedDay < 8) {
                            System.out.print(" -- ");
                            currentPrintedDay++;
                        }
                        System.out.println();
                    }
                    System.out.println();
                    System.out.println(this.getMonthText(gc.get(Calendar.MONTH)) + " " + this.gc.get(Calendar.YEAR));
                    if (startOnMonday) {
                        System.out.println("Mon Tue Wed Thu Fri Sat Sun");
                    } else {
                        System.out.println("Sun Mon Tue Wed Thu Fri Sat");
                    }
                    currentPrintedDay = 1;
                    currentMonth = nextMonth;
                    dmonths++;
                }

                currentDayOfWeek = this.gc.get(Calendar.DAY_OF_WEEK);
                if (startOnMonday) {
                    currentDayOfWeek--;
                    if (currentDayOfWeek < 1) {
                        currentDayOfWeek = 7;
                    }
                }

                //System.out.println("Date = MON: " + gc.get(Calendar.MONTH) + " DOM: " + gc.get(Calendar.DAY_OF_MONTH) +
                //    " DOW: " + gc.get(Calendar.DAY_OF_WEEK) + " currentDayOfWeek = " + currentDayOfWeek + " currentPrintedDay = " + currentPrintedDay);

                if (currentPrintedDay != currentDayOfWeek) {
                    System.out.print(" -- ");
                } else {
                    if (this.gc.get(Calendar.DAY_OF_MONTH) > 9) {
                        System.out.print(" ");
                    } else {
                        System.out.print("  ");
                    }
                    System.out.print(this.gc.get(Calendar.DAY_OF_MONTH) + " ");
                    this.gc.add(Calendar.DAY_OF_MONTH, 1);
                    nextMonth = this.gc.get(Calendar.MONTH);
                }
            }
            System.out.println();

            //System.out.println("Date = " + gc.toString()); 
        }        
    }
    
    private String getMonthText(int dMonth) {
        String retr;

        retr = switch (dMonth) {
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

        return retr;
    }
}

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

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        GregorianCalendar gc = new GregorianCalendar();
        int currentDayOfWeek;
        int monthChange;
        int monthChange2;
        int currentPrintedDay;
        int dmonths = 0;

        gc.setFirstDayOfWeek(Calendar.MONDAY);
        boolean startOnMonday = (gc.getFirstDayOfWeek() == Calendar.MONDAY);

        monthChange2 = gc.get(Calendar.MONTH);
        monthChange = monthChange2 - 1;
        while (dmonths <= 12) {
            for (currentPrintedDay = 1; currentPrintedDay < 8; currentPrintedDay++) {
                if (monthChange != monthChange2) {
                    if (currentPrintedDay != 1) {
                        while (currentPrintedDay < 8) {
                            System.out.print(" -- ");
                            currentPrintedDay++;
                        }
                        System.out.println();
                    }
                    System.out.println();
                    System.out.println(getMonthText(gc.get(Calendar.MONTH)) + " " + gc.get(Calendar.YEAR));
                    if (startOnMonday) {
                        System.out.println("Mon Tue Wed Thu Fri Sat Sun");
                    } else {
                        System.out.println("Sun Mon Tue Wed Thu Fri Sat");
                    }
                    currentPrintedDay = 1;
                    monthChange = monthChange2;
                    dmonths++;
                }

                currentDayOfWeek = gc.get(Calendar.DAY_OF_WEEK);
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
                    if (gc.get(Calendar.DAY_OF_MONTH) > 9) {
                        System.out.print(" ");
                    } else {
                        System.out.print("  ");
                    }
                    System.out.print(gc.get(Calendar.DAY_OF_MONTH) + " ");
                    gc.add(Calendar.DAY_OF_MONTH, 1);
                    monthChange2 = gc.get(Calendar.MONTH);
                }
            }
            System.out.println();

            //System.out.println("Date = " + gc.toString()); 
        }
    }

    public static String getMonthText(int dMonth) {
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

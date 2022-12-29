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

    // Temp template markup.
    private final String calMarkup = "<!doctype html><html><head>"
            + "<style type=\"text/css\">"
            + ".cal-row {display: flex; flex-wrap: wrap; justify-content: space-evenly; margin: 10px; }"
            + ".cal-row.nowrap {flex-wrap: nowrap; }"
            + ".cal-1 { flex-basis: 100%; }"
            + ".cal-4 { flex-basis: 25%; }"
            + ".cal-7 { flex-basis: 14.28%; margin-left: 2px; margin-right: 2px; }"
            + ".cal-7:first-child { margin-left: 0; }"
            + ".cal-7:first-child { margin-right: 0; }"
            + ".cal-img { max-width: 100%; height: auto; }"
            + ".monthdayname, .monthday { text-align: end; }"
            + "h1, .monthtitle { text-align: center; }"
            + "* { font-family: sans-serif; }"
            + "</style>"
            + "<title>{{calendartitle}}</title>"
            + "</head><body>"
            + "<h1>{{calendartitle}}</h1>"
            + "<div class=\"cal-row\">"
            + "{{jan-Jan_760D_3389_sRGB.webp&Female mallard duck on ice}}"
            + "{{feb-Feb_760D_0509_sRGB.webp&Pigeon looking at the camera standing on a fence}}"
            + "{{mar-Mar_760D_5005_sRGB.webp&Cygnet in the sun}}"
            + "{{apr-Apr_760D_8255_sRGB.webp&Mallard duckling}}"
            + "{{may-May_760D_4222_sRGB.webp&Midland Pullman Train, Class 43 version}}"
            + "{{jun-Jun_760D_4905_sRGB.webp&Beer beach, Devon}}"
            + "{{jul-Jul_760D_4809_sRGB.webp&Moorhen}}"
            + "{{aug-Aug_760D_6863_sRGB.webp&Male mallard duck}}"
            + "{{sep-Sep_760D_4164_sRGB.webp&Great Western Railway Class 43, 43198, Driver Stan Martin 25th June 1950 - 6th November 2004}}"
            + "{{oct-Oct_760D_0803_sRGB.webp&White swan looking at its reflection in the water}}"
            + "{{nov-Nov_760D_3724_sRGB.webp&Chaffinch on a fence}}"
            + "{{dec-Dec_760D_3366_sRGB.webp&Robin looking skywards}}"
            + "</div>"
            + "</body></html>";
    private char[] calendarTemplate;

    private final String monthMarkup = "<div class=\"cal-4 month\">"
            + "<div class=\"cal-row nowrap monthheader\">"
            + "<div class=\"cal-1 monthtitle\">{{monthtitle}}</div>"
            + "</div>"
            + "<div class=\"cal-row monthimagewrapper\">"
            + "<img class=\"cal-1 cal-img monthimage\" src=\"{{monthimage}}\" alt=\"{{monthimagedescription}}\">"
            + "</div>"
            + "<div class=\"cal-row nowrap monthdaynames\">"
            + "{{monthdaynames-<div class=\"cal-7 monthdayname\">*</div>}}"
            + "</div>"
            + "{{monthweek-<div class=\"cal-row nowrap monthweek\">!</div>&<div class=\"cal-7 monthday\">*</div>}}"
            + "</div>";
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

        //us.fileTest();
    }

    public CalGen() throws FileNotFoundException {
        this.gc.setFirstDayOfWeek(Calendar.MONDAY);
        this.startOnMonday = (this.gc.getFirstDayOfWeek() == Calendar.MONDAY);

        this.theYear = this.gc.get(Calendar.YEAR) + 1;
        this.mout = new FileOutputStream("./calm.html");
    }

    public String fileTest() throws FileNotFoundException, IOException {
        // Ref: https://stackoverflow.com/questions/21980090/javas-randomaccessfile-eofexception
        java.io.File file = new java.io.File("CalendarTemplate.txt");
        System.out.println(file.length());
        
        char[] buffer = new char[(int) file.length()];
        
        java.io.FileInputStream fin = new java.io.FileInputStream(file);
        java.io.InputStreamReader fr = new java.io.InputStreamReader(fin, "UTF-8");
        fr.read(buffer);
        StringBuilder sb = new StringBuilder();
        
        sb.append(buffer);
        
        System.out.println(sb.toString());
        
        return sb.toString();
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

        while (this.currentMonth == this.nextMonth) {

            for (currentPrintedDay = 1; currentPrintedDay < 8; currentPrintedDay++) {
                currentDayOfWeek = this.gc.get(Calendar.DAY_OF_WEEK);
                if (this.startOnMonday) {
                    currentDayOfWeek--;
                    if (currentDayOfWeek < 1) {
                        currentDayOfWeek = 7;
                    }
                }

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
    }

    private void day(Integer day) {
        this.day(day.toString());
    }

    private void day(String day) {
        System.out.print(day);
    }

    public void calendarTemplate() throws IOException {
        // Reset.
        this.gc.set(theYear, 0, 1);
        try (this.mout) {
            //this.calendarTemplate = this.calMarkup.toCharArray();
            this.calendarTemplate = this.fileTest().toCharArray();
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

        switch (token) {
            case "calendartitle":
                this.markupOut.append(this.gc.get(Calendar.YEAR)).append(" Calendar");
                break;
            case "jan":
                this.monthTemplate(0, data, dataExtra);
                break;
            case "feb":
                this.monthTemplate(1, data, dataExtra);
                break;
            case "mar":
                this.monthTemplate(2, data, dataExtra);
                break;
            case "apr":
                this.monthTemplate(3, data, dataExtra);
                break;
            case "may":
                this.monthTemplate(4, data, dataExtra);
                break;
            case "jun":
                this.monthTemplate(5, data, dataExtra);
                break;
            case "jul":
                this.monthTemplate(6, data, dataExtra);
                break;
            case "aug":
                this.monthTemplate(7, data, dataExtra);
                break;
            case "sep":
                this.monthTemplate(8, data, dataExtra);
                break;
            case "oct":
                this.monthTemplate(9, data, dataExtra);
                break;
            case "nov":
                this.monthTemplate(10, data, dataExtra);
                break;
            case "dec":
                this.monthTemplate(11, data, dataExtra);
                break;
            default:
                this.markupOut.append("<p>Calendar error!</p>");
        }
    }

    private void monthTemplate(int theMonth, String imageName, String imageDescription) {
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
            case "monthtitle":
                this.markupOut.append(this.getMonthText(gc.get(Calendar.MONTH)));
                break;
            case "monthdaynames":
                this.monthDayNames(data);
                break;
            case "monthweek":
                this.monthWeek(data);
                break;
            case "monthimage":
                this.monthImage(imageName);
                break;
            case "monthimagedescription":
                this.monthImage(imageDescription);
                break;
            default:
                this.markupOut.append("<p>Month error!</p>");
        }
    }

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

    private void monthImage(String text) {
        if (text != null) {
            this.markupOut.append(text);
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

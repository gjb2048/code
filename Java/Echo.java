/*
 * Echo
 *
 * @copyright  2022 G J Barnard
 * @license    http://www.gnu.org/copyleft/gpl.html GNU GPL v3 or later
 */

 public class Echo {
    public static void main(String args[]) {
        Echo us = new Echo();

        if (args.length < 1) {
            us.output("- Add text when running, i.e. java Echo Me or java Echo \"I'm me\"");
        } else {
            us.output(args[0]);
        }
        System.out.println("Args length: " + args.length);
    }

    private void output(String s) {
        System.out.println("Hello " + s);
    }
}
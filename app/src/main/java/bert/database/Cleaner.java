package bert.database;

import android.util.Log;

/**
 * Created by afiol-mahon on 5/6/15.
 */
public class Cleaner {
    public static boolean isValid(String input) {
        boolean out = false;
        if (input != null) {
            if (!input.trim().isEmpty()) {
                out = true;
            }
        }
        log("Validator determined String <" + input + "> was " + ((out) ? "Valid" : "Invalid"));
        return out;
    }

    public static String clean(String input) {
        String outputString = input.trim();
        log("Clean changed input <" + input + "> to <" + outputString + ">");
        return outputString;
    }

    public static String cleanProjectName(String input) {
        input = input.replaceAll("\\W|_", "").trim();
        return input;
    }

    private static void log(String out) {
        Log.d("Cleaner", out);
    }
}

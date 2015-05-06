package bert.database;

import android.util.Log;

/**
 * Created by afiol-mahon on 5/6/15.
 */
public class Cleaner {
    public static boolean isValid(String input) {
        boolean out = false;
        if (input != null) {
            String s = input.trim();
            if (!s.isEmpty()) {
                out = true;
            }
        }
        log("Validator determined <" + input + "> was " + out);
        return out;
    }

    public static String clean(String input) {
        String outputString = input.trim();
        log("Clean changed input <" + input + "> to <" + outputString + ">");
        return outputString;
    }

    private static void log(String out) {
        Log.d("Cleaner", out);
    }
}

package bert.database;

import java.text.SimpleDateFormat;

/**
 * Created by afiol-mahon on 5/8/15.
 * @author afiol-mahon
 */
public class DateProvider {
    public static String getDate() {
        return new SimpleDateFormat("MM-dd-yyyy").format(new java.util.Date());
    }
}

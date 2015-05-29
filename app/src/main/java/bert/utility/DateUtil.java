package bert.utility;

import java.text.SimpleDateFormat;

/**
 * Created by afiol-mahon on 5/8/15.
 * @author afiol-mahon
 */
public class DateUtil {
    public static String getDate() {
        return new SimpleDateFormat("MM-dd-yyyy").format(new java.util.Date());
    }
}

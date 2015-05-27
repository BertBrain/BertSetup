package bert.data.proj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liamcook on 5/13/15.
 */
public class CategoryPresets {

    private static boolean isInit = false;
    private static HashMap<String, List<Category>> presets = new HashMap<>();

    public static void init(){
        presets.put("Default", new ArrayList<>(Arrays.asList(DEFAULT_CATEGORIES)));
        presets.put("Office", new ArrayList<>(Arrays.asList(OFFICE)));
        presets.put("School", new ArrayList<>(Arrays.asList(SCHOOL)));
        isInit = true;
    }

    public static List<String> getNames(){
        if (!isInit) { init(); }

        return new ArrayList<>(presets.keySet());
    }

    public static HashMap<String, List<Category>> getPresets(){
        if (!isInit) { init(); }
        return presets;
    }

    private static Category[] OFFICE = {
            new Category("TV", 3, 15),
            new Category("Medium Printer", 3, 10),
            new Category("Large Printer/Copier 110V", 3, 15),
            new Category("Large Printer/Copier 220V", 3, 15)
    };

    private static Category[] SCHOOL = {
            new Category("Projector", 3, 5),
            new Category("Smart Board", 3, 7),
            new Category("Printer Monitor Combo", 3, 10),
    };

    public static Category[] DEFAULT_CATEGORIES = {
            new Category("Projector", 3, 5),
            new Category("Smart Board", 3, 7),
            new Category("Printer Monitor Combo", 3, 10),
            new Category("TV", 3, 15),
            new Category("Amplifier SmartBoard", 3, 20),
            new Category("Medium Printer", 3, 10),
            new Category("Large Printer/Copier 110V", 3, 15),
            new Category("Large Printer/Copier 220V", 3, 15)
    };
}

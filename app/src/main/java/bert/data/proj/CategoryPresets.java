package bert.data.proj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liamcook on 5/13/15.
 */
public class CategoryPresets {

    private static boolean isInit = false;
    private static HashMap<String, HashMap<String, Category>> presets = new HashMap<>();

    public static void init() {
        presets.put("Default", DEFAULT_CATEGORIES);
        presets.put("Office", OFFICE);
        presets.put("School", SCHOOL);
        isInit = true;
    }

    public static List<String> getNames() {
        if (!isInit) {
            init();
        }
        return new ArrayList<>(presets.keySet());
    }

    public static HashMap<String, HashMap<String, Category>> getPresets() {
        if (!isInit) { init(); }
        return presets;
    }

    private static HashMap<String, Category> OFFICE = new HashMap<>();
    static {
        OFFICE.put("TV", new Category(3, 15, false));
        OFFICE.put("Medium Printer", new Category(3, 10, false));
        OFFICE.put("Large Printer/Copier 110V", new Category(3, 15, false));
        OFFICE.put("Large Printer/Copier 220V", new Category(3, 15, false));
    }

    private static HashMap<String, Category> SCHOOL = new HashMap<>();
    static {
        SCHOOL.put("Projector", new Category(3, 5, false));
        SCHOOL.put("Smart Board", new Category(3, 7, false));
        SCHOOL.put("Printer Monitor Combo", new Category(3, 10, true));
    }

    private static HashMap<String, Category> DEFAULT_CATEGORIES = new HashMap<>();
    static {
        DEFAULT_CATEGORIES.put("Projector", new Category(3, 5, false));
        DEFAULT_CATEGORIES.put("Smart Board", new Category(3, 7, false));
        DEFAULT_CATEGORIES.put("Printer Monitor Combo", new Category(3, 10, true));
        DEFAULT_CATEGORIES.put("TV", new Category(3, 15, false));
        DEFAULT_CATEGORIES.put("Amplifier SmartBoard", new Category(3, 20, true));
        DEFAULT_CATEGORIES.put("Medium Printer", new Category(3, 10, false));
        DEFAULT_CATEGORIES.put("Large Printer/Copier 110V", new Category(3, 15, false));
        DEFAULT_CATEGORIES.put("Large Printer/Copier 220V", new Category(3, 15, false));
    }
}

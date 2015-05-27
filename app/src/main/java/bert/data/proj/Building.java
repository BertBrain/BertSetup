package bert.data.proj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afiol-mahon on 5/8/15.
 * @author afiol-mahon
 */
public class Building {
    private Time startTime;
    private Time endTime;
    private List<Category> categories;

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime){ this.startTime = startTime; }

    public Time getEndTime() { return  endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }

    public Building(Time startTime, Time endTime, List<Category> presetCategories) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.categories = new ArrayList<>(presetCategories);
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public List<Category> getCategories() { return  categories; }

    public int getCategoryCount() {
        return categories.size();
    }

    public Category getCategory(int categoryID) {
        return categories.get(categoryID);
    }

    public List<String> getCategoryNames() {
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : getCategories()) {
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }

    public Time getTimeOccupied() {
        return endTime.subtract(startTime);
    }
}

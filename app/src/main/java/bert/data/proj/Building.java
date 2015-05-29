package bert.data.proj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bert.data.proj.exceptions.InvalidCategoryNameException;
import bert.data.proj.exceptions.UnableToDeleteException;
import bert.utility.Cleaner;

/**
 * Created by afiol-mahon on 5/8/15.
 * @author afiol-mahon
 */
public class Building {
    private Time startTime;
    private Time endTime;
    private HashMap<String, Category> categoryList;

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime){ this.startTime = startTime; }

    public Time getEndTime() { return  endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }

    public Building(Time startTime, Time endTime, HashMap<String, Category> presetCategories) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.categoryList = new HashMap<>(presetCategories);
    }

    public void addCategory(String name, Category category) throws InvalidCategoryNameException {
        if (!categoryList.containsKey(name) && Cleaner.isValid(name)) {
            categoryList.put(name, category);
        } else {
            throw new InvalidCategoryNameException();
        }
    }

    public void renameCategory(String categoryID, String newCategoryID) throws InvalidCategoryNameException {
        if (categoryID != newCategoryID) {
            Category category = categoryList.get(categoryID);
            addCategory(newCategoryID, category);
            categoryList.remove(categoryID);
        }
    }

    public int getCategoryCount() {
        return categoryList.size();
    }

    public Category getCategory(String categoryID) {
        return categoryList.get(categoryID);
    }

    //TODO ensure a building with no categories does not cause problems
    public void deleteCategory(Project project, String buildingID, String categoryID) throws UnableToDeleteException {
        if (project.getBertsByCategory(buildingID, categoryID).isEmpty()) {
            categoryList.remove(categoryID);
        } else {
            throw new UnableToDeleteException();
        }
    }

    public List<String> getCategoryNames() {
        return new ArrayList<>(categoryList.keySet());
    }

    public Time getTimeOccupied() {
        return endTime.subtract(startTime);
    }
}

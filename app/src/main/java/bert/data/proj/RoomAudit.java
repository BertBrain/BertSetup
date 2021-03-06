package bert.data.proj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by afiolmahon on 5/29/15.
 * @author afiolmahon
 */
public class RoomAudit {
    private String buildingID;
    private String roomID;
    private HashMap<String, Integer> categoryCount;

    public RoomAudit(String buildingID, String roomID, HashMap<String, Integer> categoryCount) {
        this.buildingID = buildingID;
        this.roomID = roomID;
        this.categoryCount = new HashMap<>();
        this.categoryCount = categoryCount;
    }

    //TODO MAKE SURE LOCATION NAME IS SAFE
    //TODO make sure it saves
    public List<BertUnit> createBerts() {
        List<BertUnit> bertList = new ArrayList<>();
        for (String categoryID : getCategoryNames()) {
            for (int i = 0; i < getCategoryCount(categoryID); i++) {
                String countString = (i == 0) ? ("") : (String.valueOf(i + 1));
                String name = getRoomID() + " - " + categoryID + " " + countString;
                BertUnit bert = new BertUnit(name, getRoomID(), "", getBuildingID(), categoryID);
                bertList.add(bert);
            }
        }
        return bertList;
    }

    public int getNumberOfCategories() {
        return categoryCount.size();
    }

    public int totalBerts() {
        int total = 0;
        for (int i : categoryCount.values()) {
            total += i;
        }
        return total;
    }

    public List<String> getCategoryNames() {
        return new ArrayList<>(categoryCount.keySet());
    }

    public int getCategoryCount(String categoryID) {
        if (categoryCount.containsKey(categoryID)) {
            return categoryCount.get(categoryID);
        } else {
            return 0;
        }
    }

    public void setCategoryCount(String categoryID, int count) {
        categoryCount.put(categoryID, count);
    }

    public void removeCategory(String categoryID) {
        categoryCount.remove(categoryID);
    }

    public String getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(String newBuildingID) {
        this.buildingID = newBuildingID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String newRoomID) {//TODO prevent setting identical room names in the same building
        this.roomID = newRoomID;
    }
}

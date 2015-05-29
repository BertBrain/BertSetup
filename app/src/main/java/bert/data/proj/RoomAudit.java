package bert.data.proj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by afiolmahon on 5/29/15.
 */
public class RoomAudit {
    private String buildingID;
    private String roomID;
    private HashMap<String, Integer> categoryCount;

    public RoomAudit(String buildingID, String roomID, HashMap<String, Integer> categoryCount) {
        this.buildingID = buildingID;
        this.roomID = roomID;
        this.categoryCount = categoryCount;
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

    public String getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(String newBuildingID) {
        this.buildingID = newBuildingID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String newRoomID) {
        this.roomID = newRoomID;
    }
}

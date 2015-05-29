package bert.data.proj;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;

/**
 * Created by afiolmahon on 5/29/15.
 */
public class RoomAuditSerializer {
    public static final String TAG_ROOM_AUDIT = "RoomAudit";
    public static final String ATTR_BUILDING_ID = "BuildingID";
    public static final String ATTR_ROOM_ID = "RoomID";

    public static final String TAG_CATEGORY_COUNT = "CategoryCount";
    public static final String ATTR_CC_NAME = "Name";
    public static final String ATTR_CC_COUNT = "Count";

    public static RoomAudit getRoomAuditFromElement(Element e) {
        String buildingID = e.getAttribute(ATTR_BUILDING_ID);
        String roomID = e.getAttribute(ATTR_ROOM_ID);
        HashMap<String, Integer> categoryCount = new HashMap<>();

        NodeList categoryCountElements = e.getElementsByTagName(TAG_CATEGORY_COUNT);
        for (int i = 0; i < categoryCountElements.getLength(); i++) {
            Element categoryCountElement = (Element) categoryCountElements.item(i);
            String ccName = categoryCountElement.getAttribute(ATTR_CC_NAME);
            int ccCount = Integer.parseInt(categoryCountElement.getAttribute(ATTR_CC_COUNT));
            categoryCount.put(ccName, ccCount);
        }
        return new RoomAudit(buildingID, roomID, categoryCount);
    }

    public static Element getElementFromRoomAudit(RoomAudit r, Document d) {
        Element e = d.createElement(TAG_ROOM_AUDIT);
        e.setAttribute(ATTR_BUILDING_ID, r.getBuildingID());
        e.setAttribute(ATTR_ROOM_ID, r.getRoomID());

        for (String category : r.getCategoryNames()) {
            Element cc = d.createElement(TAG_CATEGORY_COUNT);
            cc.setAttribute(ATTR_CC_NAME, category);
            cc.setAttribute(ATTR_CC_COUNT, Integer.toString(r.getCategoryCount(category)));
            e.appendChild(cc);
        }
        return e;
    }
}

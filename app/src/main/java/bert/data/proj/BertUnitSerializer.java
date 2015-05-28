package bert.data.proj;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by afiol-mahon on 5/13/15.
 */
public class BertUnitSerializer {
    public static final String TAG_BERT = "Bert";
    public static final String ATTR_NAME = "Name";
    public static final String ATTR_ROOM_ID = "RoomID";
    public static final String ATTR_MAC = "MAC";
    public static final String ATTR_BUILDING_ID = "BuildingID";
    public static final String ATTR_CATEGORY_ID = "CategoryID";

    public static BertUnit getBertUnitFromElement(Element e) {
        String name = e.getAttribute(ATTR_NAME);
        String roomID = e.getAttribute(ATTR_ROOM_ID);
        String MAC = e.getAttribute(ATTR_MAC);
        String buildingID = e.getAttribute(ATTR_BUILDING_ID);
        String categoryID = e.getAttribute(ATTR_CATEGORY_ID);
        BertUnit b = new BertUnit(name, roomID, MAC, buildingID, categoryID);
        return b;
    }

    public static Element getElementFromBertUnit(BertUnit b, Document d) {
        Element e = d.createElement(TAG_BERT);
        e.setAttribute(ATTR_NAME, b.getName());
        e.setAttribute(ATTR_ROOM_ID, b.getRoomID());
        e.setAttribute(ATTR_MAC, b.getMAC());
        e.setAttribute(ATTR_BUILDING_ID, b.getBuildingID());
        e.setAttribute(ATTR_CATEGORY_ID, b.getCategoryID());
        return e;
    }
}

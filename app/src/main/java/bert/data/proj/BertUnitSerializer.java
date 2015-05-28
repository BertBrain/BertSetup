package bert.data.proj;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by afiol-mahon on 5/13/15.
 */
public class BertUnitSerializer {
    public static final String TAG_BERT = "Bert";
    public static final String ATTR_NAME = "Name";
    public static final String ATTR_LOCATION = "Location";
    public static final String ATTR_MAC = "MAC";
    public static final String ATTR_BUILDING_ID = "BuildingID";
    public static final String ATTR_CATEGORY_ID = "CategoryID";
    public static final String ATTR_DELETED = "Deleted";

    public static BertUnit getBertUnitFromElement(Element e) {
        String name = e.getAttribute(ATTR_NAME);
        String location = e.getAttribute(ATTR_LOCATION);
        String MAC = e.getAttribute(ATTR_MAC);
        String buildingID = e.getAttribute(ATTR_BUILDING_ID);
        String categoryID = e.getAttribute(ATTR_CATEGORY_ID);
        boolean deleted = Boolean.parseBoolean(e.getAttribute(ATTR_DELETED));
        BertUnit b = new BertUnit(name, location, MAC, buildingID, categoryID, deleted);
        return b;
    }

    public static Element getElementFromBertUnit(BertUnit b, Document d) {
        Element e = d.createElement(TAG_BERT);
        e.setAttribute(ATTR_NAME, b.getName());
        e.setAttribute(ATTR_LOCATION, b.getLocation());
        e.setAttribute(ATTR_MAC, b.getMAC());
        e.setAttribute(ATTR_BUILDING_ID, b.getBuildingID());
        e.setAttribute(ATTR_CATEGORY_ID, b.getCategoryID());
        e.setAttribute(ATTR_DELETED, Boolean.toString(b.isDeleted()));
        return e;
    }
}

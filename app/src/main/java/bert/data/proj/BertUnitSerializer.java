package bert.data.proj;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afiol-mahon on 5/13/15.
 */
public class BertUnitSerializer {
    public static final String TAG_BERT = "Bert";
    public static final String TAG_NAME = "Name";
    public static final String TAG_LOCATION = "Location";
    public static final String TAG_MAC = "MAC";
    public static final String TAG_BUILDING_ID = "BuildingID";
    public static final String TAG_CATEGORY_ID = "CategoryID";

    public static BertUnit getBertUnitFromElement(Element e) {
        String name = e.getAttribute(TAG_NAME);
        String location = e.getAttribute(TAG_LOCATION);
        String MAC = e.getAttribute(TAG_MAC);
        int buildingID = Integer.parseInt(e.getAttribute(TAG_BUILDING_ID));
        int categoryID = Integer.parseInt(e.getAttribute(TAG_CATEGORY_ID));
        BertUnit b = new BertUnit(name, location, MAC, buildingID, categoryID);
        return b;
    }

    public static Element getElementFromBertUnit(BertUnit b, Document d) {
        Element e = d.createElement(TAG_BERT);
        e.setAttribute(TAG_NAME, b.getName());
        e.setAttribute(TAG_LOCATION, b.getLocation());
        e.setAttribute(TAG_BUILDING_ID, String.valueOf(b.getBuildingID()));
        e.setAttribute(TAG_MAC, b.getMAC());
        e.setAttribute(TAG_CATEGORY_ID, String.valueOf(b.getCategoryID()));
        return e;
    }
}

package bert.data.proj;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afiol-mahon on 5/13/15.
 */
public class BuildingSerializer {

    public static final String TAG_BUILDING = "Building";
    public static final String ATTR_ID = "ID";
    public static final String ATTR_NAME = "Name";
    public static final String ATTR_START_TIME = "StartTime";
    public static final String ATTR_END_TIME = "EndTime";

    public static Building getBuildingFromElement(Element e) {
        String name = e.getAttribute(ATTR_NAME);
        Time startTime = new Time(Integer.valueOf(e.getAttribute(ATTR_START_TIME)));
        Time endTime = new Time(Integer.valueOf(e.getAttribute(ATTR_END_TIME)));

        List<Category> categories = new ArrayList<>();
        NodeList categoryElements = e.getElementsByTagName(CategorySerializer.TAG_CATEGORY);
        for (int i = 0; i < categoryElements.getLength(); i++) {
            Element categoryElement = (Element) categoryElements.item(i);
            categories.add(CategorySerializer.getCategoryFromElement(categoryElement));
        }
        return new Building(name, startTime, endTime, categories);
    }

    public static Element getElementFromBuilding(Building b, Document d, int index) {
        Element e = d.createElement(TAG_BUILDING);
        e.setAttribute(ATTR_NAME, b.getName());
        e.setAttribute(ATTR_START_TIME, String.valueOf(b.getStartTime().getMinutes()));
        e.setAttribute(ATTR_END_TIME, String.valueOf(b.getEndTime().getMinutes()));

        List<Category> categories = b.getCategories();
        for(int i = 0; i < categories.size(); i++) {
            e.appendChild(CategorySerializer.getElementFromCategory(categories.get(i), d, i));
        }

        e.setAttribute(ATTR_ID, String.valueOf(index));
        return e;
    }
}

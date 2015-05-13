package bert.data.proj;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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


    public static List<Building> getBuildingList(Document d) {
        List<Building> buildingList = new ArrayList<Building>();
        NodeList buildingNodeList = d.getElementsByTagName(BuildingSerializer.TAG_BUILDING);

        for (int i = 0; i < buildingNodeList.getLength(); i++) {
            Element e = (Element) buildingNodeList.item(i);

            int id = Integer.valueOf(e.getAttribute(ATTR_ID));
            String name = e.getAttribute(ATTR_NAME);

            Building b = new Building(name);

            buildingList.add(id, b);
        }
        return buildingList;
    }

    public static Element getBuildingElementList(List<Building> list, Document d) {
        Element buildingElementList = d.createElement("Buildings");
        for (int i = 0; i < list.size(); i++) {
            Building b = list.get(i);
            Element e = d.createElement(TAG_BUILDING);
            e.setAttribute(ATTR_NAME, b.getName());
            e.setAttribute(ATTR_ID, String.valueOf(i));
            buildingElementList.appendChild(e);
        }
        return buildingElementList;
    }
}

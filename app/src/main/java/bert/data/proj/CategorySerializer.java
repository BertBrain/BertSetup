package bert.data.proj;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afiol-mahon on 5/13/15.
 */
public class CategorySerializer {

    public static final String TAG_CATEGORIES = "Categories";
    public static final String TAG_CATEGORY = "Category";
    public static final String ATTR_ID = "ID";
    public static final String ATTR_NAME = "Name";
    public static final String ATTR_BERT_TYPE = "BertType";
    public static final String ATTR_ESTIMATED_LOAD = "EstimatedLoad";


    public static List<Category> getCategoryList(Document d) {
        List<Category> categories = new ArrayList<Category>();
        NodeList categoryNodeList = d.getElementsByTagName(TAG_CATEGORY);
        for (int i = 0; i < categoryNodeList.getLength(); i++) {
            Element e = (Element) categoryNodeList.item(i);

            int id = Integer.parseInt(e.getAttribute(ATTR_ID));
            String name = e.getAttribute(ATTR_NAME);
            int bertTypeID = Integer.parseInt(e.getAttribute(ATTR_BERT_TYPE));
            int estimatedLoad = Integer.parseInt(e.getAttribute(ATTR_ESTIMATED_LOAD));

            Category c = new Category(name, bertTypeID, estimatedLoad);
            categories.add(id, c);
        }
        return categories;
    }

    public static Element getCategoryElementList(List<Category> list, Document d) {
        Element categoryElementList = d.createElement(TAG_CATEGORIES);
        for (int i = 0; i < list.size(); i++) {
            Category c = list.get(i);
            Element e = d.createElement(TAG_CATEGORY);
            e.setAttribute(ATTR_NAME, c.getName());
            e.setAttribute(ATTR_BERT_TYPE, String.valueOf(c.getBertTypeID()));
            e.setAttribute(ATTR_ESTIMATED_LOAD, String.valueOf(c.getEstimatedLoad()));
            e.setAttribute(ATTR_ID, String.valueOf(i));//Stores index so that list can be recreated accurately when xml is parsed
            categoryElementList.appendChild(e);
        }
        return categoryElementList;
    }
}

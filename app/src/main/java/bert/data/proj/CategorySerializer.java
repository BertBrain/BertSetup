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
    public static final String TAG_CATEGORY = "Category";
    public static final String ATTR_ID = "ID";
    public static final String ATTR_NAME = "Name";
    public static final String ATTR_BERT_TYPE = "BertType";
    public static final String ATTR_ESTIMATED_LOAD = "EstimatedLoad";

    public static Category getCategoryFromElement(Element e) {
        String name = e.getAttribute(ATTR_NAME);
        int bertTypeID = Integer.parseInt(e.getAttribute(ATTR_BERT_TYPE));
        int estimatedLoad = Integer.parseInt(e.getAttribute(ATTR_ESTIMATED_LOAD));
        Category c = new Category(name, bertTypeID, estimatedLoad);
        return c;
    }

    public static Element getElementFromCategory(Category c, Document d, int index) {
        Element e = d.createElement(TAG_CATEGORY);
        e.setAttribute(ATTR_ID, Integer.toString(index));
        e.setAttribute(ATTR_NAME, c.getName());
        e.setAttribute(ATTR_BERT_TYPE, Integer.toString(c.getBertTypeID()));
        e.setAttribute(ATTR_ESTIMATED_LOAD, Integer.toString(c.getEstimatedLoad()));
        return e;
    }
}

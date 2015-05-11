package bert.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import bert.data.proj.BertUnit;
import bert.data.proj.Building;
import bert.data.proj.Category;
import bert.data.proj.Project;

/**
 * Created by afiol-mahon on 5/8/15.
 * @author afiol-mahon
 */
public class ProjectSerializer {
    //TODO unify all serialization code and make less prone to errors
    public static final String TAG_PROJECT = "Project";
    public static final String TAG_CONTACT = "Contact";
    public static final String TAG_PROJECT_NAME = "ProjectName";
    public static final String TAG_CREATION_DATE = "CreationDate";
    public static final String TAG_MODIFIED_DATE = "ModifiedDate";
    public static final String TAG_CONTACT_NAME = "ContactName";
    public static final String TAG_CONTACT_NUMBER = "ContactNumber";
    public static final String TAG_BERT = "Bert";
    public static final String TAG_CATEGORY = "Category";
    public static final String TAG_CATEGORY_ATTR_ID = "ID";
    public static final String TAG_BUILDING = "Building";
    public static final String TAG_BUILDING_ATTR_ID = "ID";
    public static final String TAG_BUILDING_ATTR_NAME = "Name";

    public static Project getProjectFromDocument(Document document) {
        try {
            Element projectTag = (Element) document.getElementsByTagName(TAG_PROJECT).item(0);
            Element contactTag = (Element) document.getElementsByTagName(TAG_CONTACT).item(0);

            String newProjectName = projectTag.getAttribute(TAG_PROJECT_NAME);
            String newCreationDate = projectTag.getAttribute(TAG_CREATION_DATE);
            String newModifiedDate = projectTag.getAttribute(TAG_MODIFIED_DATE);

            String newContactName = contactTag.getAttribute(TAG_CONTACT_NAME);
            String newContactNumber = contactTag.getAttribute(TAG_CONTACT_NUMBER);

            List<BertUnit> newBerts = new ArrayList<BertUnit>();
            NodeList bertNodeList = document.getElementsByTagName(TAG_BERT);
            for (int i = 0; i < bertNodeList.getLength(); i++) {
                Element e = (Element) bertNodeList.item(i);
                newBerts.add(new BertUnit(e));
            }

            List<Category> newCategories = new ArrayList<Category>();
            NodeList categoryNodeList = document.getElementsByTagName(TAG_CATEGORY);
            for (int i = 0; i < categoryNodeList.getLength(); i++) {
                Element e = (Element) categoryNodeList.item(i);
                newCategories.add(Integer.parseInt(e.getAttribute(TAG_CATEGORY_ATTR_ID)), new Category(e));
            }

            List<Building> newBuildings = new ArrayList<Building>();
            NodeList buildingNodeList = document.getElementsByTagName(TAG_BUILDING);
            for (int i = 0; i < buildingNodeList.getLength(); i++) {
                Element e = (Element) buildingNodeList.item(i);
                newBuildings.add(Integer.parseInt(e.getAttribute(TAG_BUILDING_ATTR_ID)), new Building(e.getAttribute(TAG_BUILDING_ATTR_NAME)));
            }

            Project newProject = new Project(newProjectName);
            newProject.setContactName(newContactName);
            newProject.setContactNumber(newContactNumber);
            newProject.setCreationDate(newCreationDate);
            newProject.setModifiedDate(newModifiedDate);
            newProject.setBerts(newBerts);
            newProject.setCategories(newCategories);
            newProject.setBuildings(newBuildings);
            return newProject;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static Document exportToXML(Project p) {
        Document projectDoc = FileProvider.createDocument();
        Element root = projectDoc.createElement(TAG_PROJECT);
        //Project
        root.setAttribute(TAG_PROJECT_NAME, p.getProjectName());
        root.setAttribute(TAG_CREATION_DATE, p.getCreationDate());
        root.setAttribute(TAG_MODIFIED_DATE, bert.data.utility.DateUtil.getDate());
        //Client
        Element client = projectDoc.createElement(TAG_CONTACT);
        client.setAttribute(TAG_CONTACT_NAME, (p.getContactName() != null) ? p.getContactName() : "");
        client.setAttribute(TAG_CONTACT_NUMBER, (p.getContactNumber() != null) ? p.getContactNumber(): "");
        root.appendChild(client);
        //BertList
        Element bertElementList = projectDoc.createElement("BertList");
        for (BertUnit b : p.getBerts()) {
            //Bert
            Element bertElement = projectDoc.createElement(TAG_BERT);
            bertElement.setAttribute("Name", b.getName());
            bertElement.setAttribute("Location", b.getLocation());
            bertElement.setAttribute("BuildingID", String.valueOf(b.getBuildingID()));
            bertElement.setAttribute("MAC", b.getMAC());
            bertElement.setAttribute("CategoryID", String.valueOf(b.getCategoryID()));
            bertElementList.appendChild(bertElement);
        }
        root.appendChild(bertElementList);

        Element buildingElementList = projectDoc.createElement("Buildings");
        for (int i = 0; i < p.getBuildings().size(); i++) {
            Building b = p.getBuildings().get(i);
            Element buildingElement = projectDoc.createElement(TAG_BUILDING);
            buildingElement.setAttribute(TAG_BUILDING_ATTR_NAME, b.getName());
            buildingElement.setAttribute(TAG_BUILDING_ATTR_ID, String.valueOf(i));
            buildingElementList.appendChild(buildingElement);
        }
        root.appendChild(buildingElementList);

        Element categoryList = projectDoc.createElement("Categories");
        for (int i = 0; i < p.getCategories().size(); i++) {
            Element cat = projectDoc.createElement(TAG_CATEGORY);
            Category c = p.getCategories().get(i);
            cat.setAttribute("Name", c.getName());
            cat.setAttribute("BertType", String.valueOf(c.getBertType()));
            cat.setAttribute("EstimatedLoad", String.valueOf(c.getEstimatedLoad()));
            cat.setAttribute(TAG_CATEGORY_ATTR_ID, String.valueOf(i));//Stores index so that list can be recreated accurately when xml is parsed
            categoryList.appendChild(cat);
        }
        root.appendChild(categoryList);
        projectDoc.appendChild(root);
        return projectDoc;
    }
}

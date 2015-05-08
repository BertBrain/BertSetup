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
import bert.data.utility.DateUtil;

/**
 * Created by afiol-mahon on 5/8/15.
 * @author afiol-mahon
 */
public class ProjectSerializer {
    public static Project getProjectFromDocument(Document document) {
        try {
            Element projectTag = (Element) document.getElementsByTagName("Project").item(0);
            Element contactTag = (Element) document.getElementsByTagName("Contact").item(0);

            String newProjectName = projectTag.getAttribute("projectName");
            String newCreationDate = projectTag.getAttribute("creationDate");
            String newModifiedDate = projectTag.getAttribute("modifiedDate");

            String newContactName = contactTag.getAttribute("contactName");
            String newContactNumber = contactTag.getAttribute("contactNumber");

            List<BertUnit> newBerts = new ArrayList<BertUnit>();
            NodeList bertNodeList = document.getElementsByTagName("Bert");
            for (int i = 0; i < bertNodeList.getLength(); i++) {
                Element e = (Element) bertNodeList.item(i);
                newBerts.add(new BertUnit(e));
            }

            List<Category> newCategories = new ArrayList<Category>();
            NodeList categoryNodeList = document.getElementsByTagName("Category");
            for (int i = 0; i < categoryNodeList.getLength(); i++) {
                Element e = (Element) categoryNodeList.item(i);
                newCategories.add(Integer.parseInt(e.getAttribute("id")), new Category(e));
            }

            List<Building> newBuildings = new ArrayList<Building>();
            NodeList buildingNodeList = document.getElementsByTagName("Building");
            for (int i = 0; i < buildingNodeList.getLength(); i++) {
                Element e = (Element) buildingNodeList.item(i);
                newBuildings.add(Integer.parseInt(e.getAttribute("id")), new Building(e.getAttribute("name")));
            }

            Project newProject = new Project(newProjectName);
            newProject.setContactName(newContactName);
            newProject.setContactNumber(newContactNumber);
            newProject.setCreationDate(newCreationDate);
            newProject.setModifiedDate(newModifiedDate);
            newProject.setBerts(newBerts);
            newProject.setCategories(newCategories);
            return newProject;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static Document exportToXML(Project p) {
        Document projectDoc = FileProvider.createDocument();
        Element root = projectDoc.createElement("Project");
        //Project
        root.setAttribute("projectName", p.getProjectName());
        root.setAttribute("creationDate", p.getCreationDate());
        root.setAttribute("modifiedDate", DateUtil.getDate());
        //Client
        Element client = projectDoc.createElement("Contact");
        client.setAttribute("contactName", (p.getContactName() != null) ? p.getContactName() : "");
        client.setAttribute("contactNumber", (p.getContactNumber() != null) ? p.getContactNumber(): "");
        root.appendChild(client);
        //BertList
        Element bertElementList = projectDoc.createElement("BertList");
        for (BertUnit b : p.getBerts()) {
            //Bert
            Element bertElement = projectDoc.createElement("Bert");
            bertElement.setAttribute("name", b.getName());
            bertElement.setAttribute("location", b.getLocation());
            bertElement.setAttribute("building", String.valueOf(b.getBuildingID()));
            bertElement.setAttribute("MAC", b.getMAC());
            bertElement.setAttribute("category", String.valueOf(b.getCategoryID()));
            bertElementList.appendChild(bertElement);
        }
        root.appendChild(bertElementList);

        Element buildingElementList = projectDoc.createElement("Buildings");
        for (int i = 0; i < p.getBuildings().size(); i++) {
            Building b = p.getBuildings().get(i);
            Element buildingElement = projectDoc.createElement("Building");
            buildingElement.setAttribute("name", b.getName());
            buildingElement.setAttribute("id", String.valueOf(i));
            buildingElementList.appendChild(buildingElement);
        }
        root.appendChild(buildingElementList);

        Element categoryList = projectDoc.createElement("Categories");
        for (int i = 0; i < p.getCategories().size(); i++) {
            Element cat = projectDoc.createElement("Category");
            Category c = p.getCategories().get(i);
            cat.setAttribute("name", c.getName());
            cat.setAttribute("bertType", String.valueOf(c.getBertType()));
            cat.setAttribute("estimatedLoad", String.valueOf(c.getEstimatedLoad()));
            cat.setAttribute("id", String.valueOf(i));//Stores index so that list can be recreated accurately when xml is parsed
            categoryList.appendChild(cat);
        }
        root.appendChild(categoryList);
        projectDoc.appendChild(root);
        return projectDoc;
    }
}

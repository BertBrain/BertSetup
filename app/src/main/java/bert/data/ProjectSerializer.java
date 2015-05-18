package bert.data;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import bert.data.proj.BertUnit;
import bert.data.proj.BertUnitSerializer;
import bert.data.proj.Building;
import bert.data.proj.BuildingSerializer;
import bert.data.proj.exceptions.InvalidProjectNameException;
import bert.data.proj.Project;

/**
 * Created by afiol-mahon on 5/8/15.
 * @author afiol-mahon
 */
public class ProjectSerializer {
    public static final String TAG_PROJECT = "Project";
    public static final String TAG_CONTACT = "Contact";
    public static final String TAG_PROJECT_NAME = "ProjectName";
    public static final String TAG_CREATION_DATE = "CreationDate";
    public static final String TAG_MODIFIED_DATE = "ModifiedDate";
    public static final String TAG_CONTACT_NAME = "ContactName";
    public static final String TAG_CONTACT_NUMBER = "ContactNumber";


    public static Project getProjectFromDocument(Document document) {
        try {
            Element projectTag = (Element) document.getElementsByTagName(TAG_PROJECT).item(0);
            Element contactTag = (Element) document.getElementsByTagName(TAG_CONTACT).item(0);

            NodeList bertNodeList = document.getElementsByTagName(BertUnitSerializer.TAG_BERT);
            NodeList buildingNodeList = document.getElementsByTagName(BuildingSerializer.TAG_BUILDING);


            Project newProject = new Project(projectTag.getAttribute(TAG_PROJECT_NAME));

            newProject.setContactName(contactTag.getAttribute(TAG_CONTACT_NAME));
            newProject.setContactNumber(contactTag.getAttribute(TAG_CONTACT_NUMBER));
            newProject.setCreationDate(projectTag.getAttribute(TAG_CREATION_DATE));
            newProject.setModifiedDate(projectTag.getAttribute(TAG_MODIFIED_DATE));

            //BERT DESERIALIZATION
            List<BertUnit> bertList = new ArrayList<>();
            for (int i = 0; i < bertNodeList.getLength(); i++) {
                Element e = (Element) bertNodeList.item(i);
                bertList.add(BertUnitSerializer.getBertUnitFromElement(e));
            }
            newProject.setBerts(bertList);

            //BUILDING DESERIALIZATION
            List<Building> buildingList = new ArrayList<>();
            for (int i = 0; i < buildingNodeList.getLength(); i++) {
                Element e = (Element) buildingNodeList.item(i);
                int id = Integer.valueOf(e.getAttribute(BuildingSerializer.ATTR_ID));
                buildingList.add(id, BuildingSerializer.getBuildingFromElement(e));
            }
            newProject.setBuildings(buildingList);

            return newProject;
        } catch (NullPointerException e) {
            return null;
        } catch (InvalidProjectNameException e) {
            e.printStackTrace();
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
        client.setAttribute(TAG_CONTACT_NUMBER, (p.getContactNumber() != null) ? p.getContactNumber() : "");
        root.appendChild(client);

        //BERT SERIALIZATION
        Element bertElementList = projectDoc.createElement("Berts");
        for (int i = 0; i < p.getBerts().size(); i++) {
            BertUnit b = p.getBerts().get(i);
            bertElementList.appendChild(BertUnitSerializer.getElementFromBertUnit(b, projectDoc));
        }
        root.appendChild(bertElementList);

        //BUILDING SERIALIZATION
        Element buildingElementList = projectDoc.createElement("Buildings");
        for (int i = 0; i < p.getBuildings().size(); i++) {
            Building b = p.getBuildings().get(i);
            buildingElementList.appendChild(BuildingSerializer.getElementFromBuilding(b, projectDoc, i));
        }
        root.appendChild(buildingElementList);

        projectDoc.appendChild(root);
        return projectDoc;
    }
}

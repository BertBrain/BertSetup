package bert.data;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bert.data.proj.BertUnit;
import bert.data.proj.BertUnitSerializer;
import bert.data.proj.Building;
import bert.data.proj.BuildingSerializer;
import bert.data.proj.RoomAudit;
import bert.data.proj.RoomAuditSerializer;
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
    public static final String TAG_BERTS = "Berts";
    public static final String TAG_BUILDINGS = "Buildings";
    public static final String TAG_AUDITS = "Audits";


    public static Project getProjectFromDocument(Document document) throws InvalidProjectNameException{
        try {
            Element projectTag = (Element) document.getElementsByTagName(TAG_PROJECT).item(0);
            Element contactTag = (Element) document.getElementsByTagName(TAG_CONTACT).item(0);

            NodeList bertNodeList = document.getElementsByTagName(BertUnitSerializer.TAG_BERT);
            NodeList buildingNodeList = document.getElementsByTagName(BuildingSerializer.TAG_BUILDING);
            NodeList roomAuditNodeList = document.getElementsByTagName(RoomAuditSerializer.TAG_ROOM_AUDIT);

            //BERT DESERIALIZATION
            List<BertUnit> bertList = new ArrayList<>();
            for (int i = 0; i < bertNodeList.getLength(); i++) {
                Element e = (Element) bertNodeList.item(i);
                bertList.add(BertUnitSerializer.getBertUnitFromElement(e));
            }

            //BUILDING DESERIALIZATION
            HashMap<String, Building> buildingList = new HashMap<>();
            for (int i = 0; i < buildingNodeList.getLength(); i++) {
                Element e = (Element) buildingNodeList.item(i);
                String buildingID = BuildingSerializer.getBuildingNameFromElement(e);
                Building nextBuilding = BuildingSerializer.getBuildingFromElement(e);
                buildingList.put(buildingID, nextBuilding);
            }

            //AUDIT DESERIALIZATION
            List<RoomAudit> roomAuditList = new ArrayList<>();
            for (int i = 0; i < roomAuditNodeList.getLength(); i++) {
                Element e = (Element) roomAuditNodeList.item(i);
                RoomAudit r = RoomAuditSerializer.getRoomAuditFromElement(e);
                roomAuditList.add(r);
            }

            Project newProject = new Project(projectTag.getAttribute(TAG_PROJECT_NAME),
                    bertList,
                    buildingList,
                    roomAuditList
            );

            newProject.setContactName(contactTag.getAttribute(TAG_CONTACT_NAME));
            newProject.setContactNumber(contactTag.getAttribute(TAG_CONTACT_NUMBER));
            newProject.setCreationDate(projectTag.getAttribute(TAG_CREATION_DATE));
            newProject.setModifiedDate(projectTag.getAttribute(TAG_MODIFIED_DATE));

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
        root.setAttribute(TAG_MODIFIED_DATE, bert.utility.DateUtil.getDate());

        //Client
        Element client = projectDoc.createElement(TAG_CONTACT);
        client.setAttribute(TAG_CONTACT_NAME, (p.getContactName() != null) ? p.getContactName() : "");
        client.setAttribute(TAG_CONTACT_NUMBER, (p.getContactNumber() != null) ? p.getContactNumber() : "");
        root.appendChild(client);

        //BERT SERIALIZATION
        Element bertElementList = projectDoc.createElement(TAG_BERTS);
        for (BertUnit bert : p.getBerts()) {
            Element bertElement = BertUnitSerializer.getElementFromBertUnit(bert, projectDoc);
            bertElementList.appendChild(bertElement);
        }
        root.appendChild(bertElementList);
        log("Saved " + bertElementList.getElementsByTagName("Bert").getLength() +" BertUnits");

        //BUILDING SERIALIZATION
        Element buildingElementList = projectDoc.createElement(TAG_BUILDINGS);
        for (String buildingID : p.getBuildingNames()) {
            Building building = p.getBuilding(buildingID);
            Element buildingElement = BuildingSerializer.getElementFromBuilding(buildingID, building, projectDoc);
            buildingElementList.appendChild(buildingElement);
        }
        root.appendChild(buildingElementList);
        log("Saved " + buildingElementList.getElementsByTagName("Building").getLength() + " Buildings");

        //AUDIT SERIALIZATION
        Element auditElementList = projectDoc.createElement(TAG_AUDITS);
        for (RoomAudit r : p.getAuditList()) {
            Element auditElement = RoomAuditSerializer.getElementFromRoomAudit(r, projectDoc);
            auditElementList.appendChild(auditElement);
        }
        root.appendChild(auditElementList);
        log("Saved " + auditElementList.getElementsByTagName(RoomAuditSerializer.TAG_ROOM_AUDIT).getLength() + " Audits");

        projectDoc.appendChild(root);
        return projectDoc;
    }

    private static void log(String output) {
        Log.d("Project Serializer", output);
    }
}

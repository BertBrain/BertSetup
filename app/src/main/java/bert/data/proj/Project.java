package bert.data.proj;

import android.util.Log;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import bert.data.FileProvider;
import bert.data.ProjectProvider;
import bert.data.ProjectSerializer;
import bert.data.proj.exceptions.InvalidBuildingNameException;
import bert.data.proj.exceptions.InvalidProjectNameException;
import bert.data.utility.Cleaner;
import bert.data.utility.DateUtil;

/**
 * @author afiol-mahon
 */
public class Project {
	private String projectName;
	private String contactName;
	private String contactNumber;
	private String creationDate;
	private String modifiedDate;
	private List<BertUnit> bertList;
    private HashMap<String, Building> buildingList;

	public Project(String name) throws InvalidProjectNameException {
		setProjectName(name);
	    this.creationDate = DateUtil.getDate();
	    this.modifiedDate = creationDate;
        bertList = new ArrayList<>();
        buildingList = new HashMap<>();
	}

    public List<String> getLocationNamesInBuilding(String buildingID) {
        List<String> locationNames = new ArrayList<>();
        for (BertUnit b : getBerts()) {
            String nextLocation = b.getLocation();
            if (!locationNames.contains(nextLocation) && b.getBuildingID().equals(buildingID)) {
                locationNames.add(nextLocation);
            }
        }
        return locationNames;
    }

    public List<String> getBuildingNames() {
        return new ArrayList<>(buildingList.keySet());
    }

    public List<BertUnit> getBertsByLocation(String buildingID, String location) {
        List<BertUnit> returnList = new ArrayList<>();
        for (BertUnit b : getBerts()) {
            if (b.getBuildingID().equals(buildingID) && b.getLocation().equals(location)) {
                returnList.add(b);
            }
        }
        return returnList;
    }

    public List<BertUnit> getBertsByBuilding(String buildingID) {
        List<BertUnit> returnList = new ArrayList<>();
        for (BertUnit b : getBerts()) {
            if (b.getBuildingID().equals(buildingID)) {
                returnList.add(b);
            }
        }
        return returnList;
    }

    public int getLocationCount() {
        List<String> locationNames = new ArrayList<>();
        for (BertUnit b : getBerts()) {
            String nextLocation = b.getLocation();
            if (!locationNames.contains(nextLocation)) {
                locationNames.add(nextLocation);
            }
        }
        return locationNames.size();
    }

    public List<BertUnit> getBertsByCategory(String buildingID, String categoryID) {
        List<BertUnit> returnList = new ArrayList<>();
        for (BertUnit b : getBerts()) {
            if (b.getBuildingID().equals(buildingID) && b.getCategoryID().equals(categoryID)) {
                returnList.add(b);
            }
        }
        return returnList;
    }

	public void addBert(BertUnit bert) {
		bertList.add(bert);
	}

    public void addBerts(List<BertUnit> berts) {
        for (BertUnit b : berts){
            addBert(b);
        }
    }

    public void addBuilding(String buildingID, Building building) throws InvalidBuildingNameException {
        if (!buildingList.containsKey(buildingID) && Cleaner.isValid(buildingID)) {
            buildingList.put(buildingID, building);
        } else {
            throw new InvalidBuildingNameException();
        }
    }

    public void deleteBuilding(String buildingID) {
        for (BertUnit b : getBertsByBuilding(buildingID)) {
            b.deleteBert();
        }
        buildingList.remove(buildingID);
    }

    //Getters and setters
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String newProjectName) throws InvalidProjectNameException {
        Cleaner.cleanProjectName(newProjectName);
        if (ProjectProvider.getInstance().projectNameCheck(newProjectName)) {
            this.projectName = newProjectName;
        } else {
            throw new InvalidProjectNameException();
        }
    }

    public String getContactName() {
        return contactName;
    }
    public void setContactName(String newContactName) {
        Cleaner.clean(newContactName);
        if (Cleaner.isValid(newContactName)) {
            this.contactName = newContactName;
        }
    }

    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String newContactNumber) {
        Cleaner.clean(newContactNumber);
        if (Cleaner.isValid(newContactNumber)) {
            this.contactNumber = newContactNumber;
        }
    }

    public String getCreationDate() {
        return this.creationDate;
    }
    public void setCreationDate(String newCreationDate) {
        this.creationDate = newCreationDate;
    }

    public String getModifiedDate() {
        return this.modifiedDate;
    }
    public void setModifiedDate(String newModifiedDate) {
        this.modifiedDate = newModifiedDate;
    }

    public List<BertUnit> getAllBertsAndDeleted() {
        return this.bertList;
    }

    public List<BertUnit> getBerts() {
        List<BertUnit> output = new ArrayList<>();
        for (BertUnit b : bertList) {
            if (b.isDeleted() == false) {
                output.add(b);
            }
        }
        return output;
    }

    public void setBertList(List<BertUnit> newBertList) {
        this.bertList = newBertList;
    }

    public Building getBuilding(String buildingID) {
        return buildingList.get(buildingID);
    }

    public void renameBuilding(String buildingID, String newBuildingID) throws InvalidBuildingNameException {
        Building building = buildingList.get(buildingID);
        addBuilding(newBuildingID, building);
        buildingList.remove(buildingID);
    }

    public int getBuildingCount() {
        return buildingList.size();
    }

    public void setBuildings(HashMap<String, Building> newBuildings) {
        this.buildingList = newBuildings;
    }

    public String getFileName() {
        return this.getProjectName() + ".xml";
    }

    public void save() {
        Log.d("ProjectSaver", "Saving Project: " + getProjectName() + " to XML file");
        Document d = ProjectSerializer.exportToXML(this);
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            File outputFile = new File(FileProvider.getProjectDirectory(), this.getFileName());
            Result result = new StreamResult(outputFile);
            Source source = new DOMSource(d);
            transformer.transform(source, result);
        } catch(TransformerConfigurationException e) {
            e.printStackTrace();
        } catch(TransformerException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();//TODO should these be handled better?
        }
    }

    public static Project loadProject(File file) {
        Log.d("ProjectLoader", "Loading document: " + file.getName());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document d = builder.parse(file);
            return ProjectSerializer.getProjectFromDocument(d);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
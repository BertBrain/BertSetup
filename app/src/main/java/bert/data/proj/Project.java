package bert.data.proj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bert.data.FileProvider;
import bert.data.ProjectProvider;
import bert.data.proj.exceptions.DuplicateBuildingInProjectException;
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
    private HashMap<Integer, Building> buildings;
	
	public Project(String name) throws InvalidProjectNameException {
		setProjectName(name);
	    this.creationDate = DateUtil.getDate();
	    this.modifiedDate = creationDate;
        bertList = new ArrayList<>();
        buildings = new HashMap<>();
	}

    public List<String> getLocationNamesInBuilding(int building) {
        List<String> locationNames = new ArrayList<>();
        for (BertUnit b : getBerts()) {
            String nextLocation = b.getLocation();
            if (!locationNames.contains(nextLocation) && b.getBuildingID() == building) {
                locationNames.add(nextLocation);
            }
        }
        return locationNames;
    }

    public int highestBuildingIndex(){
        int maxIndex = 0;
        for (Integer i : buildings.keySet()){
            if (i > maxIndex){
                maxIndex = i;
            }
        }
        return  maxIndex;
    }

    public HashMap<Integer, String> getBuildingNames() {
        HashMap<Integer, String> buildingNames = new HashMap<>();
        for (Integer i : buildings.keySet()) {
            buildingNames.put(i, buildings.get(i).getName());
        }
        return buildingNames;
    }

    public List<BertUnit> getBertsByLocation(int building, String location) {
        List<BertUnit> returnList = new ArrayList<>();
        for (BertUnit b : getBerts()) {
            if (b.getBuildingID() == building && b.getLocation() == location) {
                returnList.add(b);
            }
        }
        return returnList;
    }

    public List<BertUnit> getBertsByBuilding(int buildingID) {
        List<BertUnit> returnList = new ArrayList<>();
        for (BertUnit b : getBerts()) {
            if (b.getBuildingID() == buildingID) {
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

    public List<BertUnit> getBertsByCategory(int building, int categoryID) {
        List<BertUnit> returnList = new ArrayList<>();
        for (BertUnit b : getBerts()) {
            if (b.getBuildingID() == building && b.getCategoryID() == categoryID) {
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

    public void addBuilding(Building building) throws DuplicateBuildingInProjectException {
        if (getBuildingNames().values().contains(building.getName())) {
            throw new DuplicateBuildingInProjectException();
        } else {
            buildings.put(highestBuildingIndex()+1, building);
        }
    }
    public void deleteBuilding(int ID){
        for (BertUnit b : getBertsByBuilding(ID)){
            b.deleteBert();
        }
        buildings.remove(ID);
        FileProvider.saveProject(this);
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

    public HashMap<Integer, Building> getBuildings() {
        return this.buildings;
    }

    public List<Integer> getOrderedBuildingKeys(){
        List<Integer> keys = new ArrayList<>();
        for (int i = 0; i<highestBuildingIndex()+1; i++){
            if (buildings.keySet().contains(i)){
                keys.add(i);
            }
        }
        return keys;
    }

    public List<String> getOrderedBuildingNames(){
        List<String> names = new ArrayList<>();
        for (Integer i : getOrderedBuildingKeys()){
            names.add(buildings.get(i).getName());
        }
        return names;
    }

    public void setBuildings(HashMap<Integer, Building> newBuildings) {
        this.buildings = newBuildings;
    }
}
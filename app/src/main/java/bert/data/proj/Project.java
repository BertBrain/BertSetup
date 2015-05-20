package bert.data.proj;

import java.util.ArrayList;
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
    private List<Building> buildings;
	
	public Project(String name) throws InvalidProjectNameException {
		setProjectName(name);
	    this.creationDate = DateUtil.getDate();
	    this.modifiedDate = creationDate;
        bertList = new ArrayList<>();
        buildings = new ArrayList<>();
	}

    public List<String> getLocationNamesInBuilding(int building) {
        List<String> locationNames = new ArrayList<>();
        for (BertUnit b : getBerts()) {
            String n = b.getLocation();
            if (!locationNames.contains(n) && b.getBuildingID() == building) {
                locationNames.add(n);
            }
        }
        return locationNames;
    }

    public List<String> getBuildingNames() {
        List<String> buildingNames = new ArrayList<>();
        for (Building b : buildings) {
            buildingNames.add(b.getName());
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
        if (getBuildingNames().contains(building.getName())) {
            throw new DuplicateBuildingInProjectException();
        } else {
            buildings.add(building);
        }
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

    public List<Building> getBuildings() {
        return this.buildings;
    }

    public void setBuildings(List<Building> newBuildings) {
        this.buildings = newBuildings;
    }
}
package bert.data.proj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bert.data.utility.Cleaner;
import bert.data.utility.DateUtil;

/**
 * @author afiol-mahon
 */
public class Project implements Serializable{
	private String projectName;
	private String contactName;
	private String contactNumber;
	private String creationDate;
	private String modifiedDate;
	private List<BertUnit> berts;
	private List<Category> categories;
    private List<Building> buildings;
	
	public Project(String name) {
		setProjectName(name);
	    this.creationDate = DateUtil.getDate();
	    this.modifiedDate = creationDate;
	    berts = new ArrayList<BertUnit>();
	    categories = new ArrayList<Category>();
        buildings = new ArrayList<Building>();
	    categories = new ArrayList<Category>(Arrays.asList(Category.DEFAULT_CATEGORIES));
	}

	public List<String> getCategoryNames() {
		List<String> categoryNames = new ArrayList<String>();
        for (Category cat : this.categories){
            categoryNames.add(cat.getName());
        }
		for (BertUnit b : berts) {
			String catName = categories.get(b.getCategoryID()).getName();
			if (!categoryNames.contains(catName)) {
				categoryNames.add(catName);
			}
		}
		return categoryNames;
	}

    public List<String> getLocationNames() {
        List<String> locationNames = new ArrayList<String>();
        for (BertUnit b : berts) {
            String n = b.getLocation();
            if (!locationNames.contains(n)) {
                locationNames.add(n);
            }
        }
        return locationNames;
    }

    public List<String> getLocationNamesInBuilding(int building){
        List<String> locationNames = new ArrayList<String>();
        for (BertUnit b : berts) {
            String n = b.getLocation();
            if (!locationNames.contains(n) && b.getBuildingID() == building) {
                locationNames.add(n);
            }
        }
        return locationNames;
    }

    public List<String> getBuildingNames() {
        List<String> buildingNames = new ArrayList<String>();
        for (Building b : buildings) {
            buildingNames.add(b.getName());
        }
        return buildingNames;
    }

    public List<BertUnit> getBertsByLocation(int building, String location) {
        List<BertUnit> returnList = new ArrayList<BertUnit>();
        for (BertUnit b : berts) {
            if (b.getBuildingID() == building && b.getLocation() == location) {
                returnList.add(b);
            }
        }
        return returnList;
    }

	public void exportBertConfiguratorCSV() {
	    //TODO write
	}
	
	public void exportROI() {
	    //TODO write
	}
	  
	public void addBert(BertUnit bert) {
		berts.add(bert);
	}

    public void addBerts(List<BertUnit> berts){
        for (BertUnit b : berts){
            addBert(b);
        }
    }

    public void addCategory(Category category){
        categories.add(category);
    }

    public void addBuilding(Building building) {buildings.add(building);}

    //Getters and setters
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String newProjectName) {
        Cleaner.cleanProjectName(newProjectName);
        boolean goodName = Cleaner.isValid(newProjectName);
        if (goodName) {
            this.projectName = newProjectName;
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

    public List<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(List<Category> newCategories) {
        this.categories = newCategories;
    }

    public List<BertUnit> getBerts() {
        return this.berts;
    }

    public void setBerts(List<BertUnit> newBerts) {
        this.berts = newBerts;
    }

    public List<Building> getBuildings() {
        return this.buildings;
    }

    public void setBuildings(List<Building> newBuildings) {
        this.buildings = newBuildings;
    }
}
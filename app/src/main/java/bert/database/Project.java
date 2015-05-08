package bert.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author afiol-mahon
 */
public class Project {
	private String projectName;
	private String contactName;
	private String contactNumber;
	private String creationDate;
	private String modifiedDate;
	private List<BertUnit> berts;
	private List<Category> categories;
	
	public Project(String name) {
		setProjectName(name);
	    this.creationDate = DateProvider.getDate();
	    this.modifiedDate = creationDate;
	    berts = new ArrayList<BertUnit>();
	    categories = Arrays.asList(Category.projector, Category.vendingMachine, Category.printer, Category.hotWaterHeater, Category.cat1, Category.cat2, Category.cat3);
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

    public List<String> getLocationNamesInBuilding(String building){
        List<String> locationNames = new ArrayList<String>();
        for (BertUnit b : berts) {
            String n = b.getLocation();
            if (!locationNames.contains(n) && b.getBuilding() == building) {
                locationNames.add(n);
            }
        }
        return locationNames;
    }

    public List<String> getBuildingNames() {
        List<String> buildingNames = new ArrayList<String>();
        for (BertUnit b : berts) {
            String n = b.getBuilding();
            if (!buildingNames.contains(n)){
                buildingNames.add(n);
            }
        }
        return buildingNames;
    }

    public List<BertUnit> getBertsByLocation(String building, String location) {
        List<BertUnit> returnList = new ArrayList<BertUnit>();
        for (BertUnit b : berts) {
            if (b.getBuilding() == building && b.getLocation() == location) {
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
}
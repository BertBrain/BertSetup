package bert.data.proj;

import bert.data.utility.Cleaner;

/**
 * @author afiol-mahon
 */
public class BertUnit {

	private String name;
	private String location;
	private String MAC;
	private String categoryID;
	private String buildingID;
	private boolean deleted;
	
	public BertUnit(String name, String location, String MAC, String buildingID, String categoryID, boolean deleted) {
		this.name = name;
		this.location = location;
		this.MAC = MAC;
		this.buildingID = buildingID;
		this.categoryID = categoryID;
        this.deleted = deleted;
	}
	
	public String getName() {
		return name;
	}

    public void setName(String newName) {
        Cleaner.clean(newName);
        if (Cleaner.isValid(newName)) {
            this.name = newName;
        }
    }

	public String getCSVName() {
		return getName(); //TODO make this limit to 20 char and format
	}

	public String getLocation() {
		return location;
	}

	public String getBuildingID() {
		return buildingID;
	}

	public void setBuildingID(String newBuildingID) {
		this.buildingID = newBuildingID;
	}
	
	public void setMAC(String newMAC) {
		//TODO implement MAC address formatter in this method
		this.MAC = newMAC;
	}
	
	public String getMAC() {
		return (MAC != null) ? MAC : "";
	}
	
	public String getCategoryID() {
		return categoryID;
	}

    public void setCategoryID(String newCategoryID) {
        this.categoryID = newCategoryID;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void deleteBert() {
        deleted = true;
    }
}

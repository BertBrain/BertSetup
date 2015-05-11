package bert.data.proj;

import org.w3c.dom.Element;

import bert.data.utility.Cleaner;

/**
 * @author afiol-mahon
 */
public class BertUnit {
	private String name;
	private String location;
	private String MAC;
	private int categoryID;
	private int buildingID;
	
	public BertUnit(String name, String location, String MAC, int buildingID, int categoryID) {
		this.name = name;
		this.location = location;
		this.MAC = MAC;
		this.buildingID = buildingID;
		this.categoryID = categoryID;
	}
	
	public BertUnit(Element e) {
		this.name = e.getAttribute("Name");
		this.location = e.getAttribute("Location");
		this.MAC = e.getAttribute("MAC");
		this.buildingID = Integer.parseInt(e.getAttribute("BuildingID"));
		this.categoryID = Integer.parseInt(e.getAttribute("CategoryID"));
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

	public String getLocation() {
		return location;
	}

    public void setLocation(String newLocation) {
        Cleaner.clean(newLocation);
        if (Cleaner.isValid(newLocation)) {
            this.location = newLocation;
        }
    }

	public int getBuildingID() {
		return buildingID;
	}

	public void setBuildingID(int newBuildingID) {
		this.buildingID = newBuildingID;
	}
	
	public void setMAC(String newMAC) {
		//TODO implement MAC address formatter in this method
		this.MAC = newMAC;
	}
	
	public String getMAC() {
		return (MAC != null) ? MAC : "";
	}
	
	public int getCategoryID() {
		return categoryID;
	}

    public void setCategoryID(int newCategoryID) {
        this.categoryID = newCategoryID;
    }
}

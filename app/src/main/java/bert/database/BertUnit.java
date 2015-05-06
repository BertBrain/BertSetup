package bert.database;

import org.w3c.dom.Element;

/**
 * @author afiol-mahon
 */
public class BertUnit {
	private String name;
	private String location;
	private String building;
	private String MAC;
	private int categoryID;
	
	public BertUnit(String name, String location, String building, int categoryID) {
		this.name = name;
		this.location = location;
		this.building = building;
		this.categoryID = categoryID;
	}
	
	public BertUnit(Element e) {
		this.name = e.getAttribute("name");
		this.location = e.getAttribute("location");
		this.building = e.getAttribute("building");
		this.MAC = e.getAttribute("MAC");
		this.categoryID = Integer.parseInt(e.getAttribute("category"));
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

	public String getBuilding() {
		return building;
	}

    public void setBuilding(String newBuilding) {
        Cleaner.clean(newBuilding);
        if (Cleaner.isValid(newBuilding)) {
            this.building = newBuilding;
        }
    }
	
	public void setMAC(String newMAC) {
		//TODO implement MAC address formatter in this method
		this.MAC = newMAC;
	}
	
	public String getMAC() {
		return MAC;
	}
	
	public int getCategoryID() {
		return categoryID;
	}

    public void setCategoryID(int newCategoryID) {
        this.categoryID = newCategoryID;
    }
}

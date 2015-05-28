package bert.data.proj;

import bert.data.proj.exceptions.InvalidBertNameException;
import bert.data.utility.Cleaner;

/**
 * @author afiol-mahon
 */
public class BertUnit {

	private String name;
	private String roomID;
	private String MAC;
	private String categoryID;
	private String buildingID;

	public BertUnit(String name, String roomID, String MAC, String buildingID, String categoryID) {
		this.name = name;
		this.roomID = roomID;
		this.MAC = MAC;
		this.buildingID = buildingID;
		this.categoryID = categoryID;
	}
	
	public String getName() {
		return name;
	}

    public void setName(String newName) throws InvalidBertNameException {
        if (Cleaner.isValid(newName)) {
            this.name = newName;
        } else {
			throw new InvalidBertNameException();
		}
    }

	public String getCSVName() {
		return getName(); //TODO make this limit to 20 char and format
	}

	public String getRoomID() {
		return roomID;
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
}

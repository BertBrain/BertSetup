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
	
	public String getLocation() {
		return location;
	}
	
	public String getBuilding() {
		return building;
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
}

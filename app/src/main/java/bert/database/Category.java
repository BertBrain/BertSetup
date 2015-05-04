package bert.database;

import org.w3c.dom.Element;

public class Category {
	private final String name;
	private final int bertType;
	private final int estimatedLoad;
	
	//FIXME bertType should be an enum
	/**
	 * 
	 * @param name Name of device
	 * @param type 15A Bert, 20A Bert, 220V Bert
	 * @param estimatedLoad in watts
	 */
	public Category(String name, int bertType, int estimatedLoad) {
		this.name = name;
		this.bertType = bertType;
		this.estimatedLoad = estimatedLoad;
	}
	
	public Category(Element e) {
		this.name = e.getAttribute("name");
		this.bertType = Integer.parseInt(e.getAttribute("bertType"));
		this.estimatedLoad = Integer.parseInt(e.getAttribute("estimatedLoad"));
	}
	
	public String getName() {
		return name;
	}
	
	public int getBertType() {
		return bertType;
	}
	
	/**
	 * @return estimated power consumption of device in watts
	 */
	public int getEstimatedLoad() {
		return estimatedLoad;
	}
	
	public static Category projector = new Category("Projector", 15, 12);
	public static Category vendingMachine = new Category("VendingMachine", 220, 350);
}

package bert.data.proj;

import org.w3c.dom.Element;

public class Category {
	private final String name;
	private final int bertType;
	private final int estimatedLoad;
	
	//FIXME bertType should be an enum
	/**
	 * 
	 * @param name Name of device
	 * @param bertType 15A Bert, 20A Bert, 220V Bert
	 * @param estimatedLoad in watts
	 */
	public Category(String name, int bertType, int estimatedLoad) {
		this.name = name;
		this.bertType = bertType;
		this.estimatedLoad = estimatedLoad;
	}
	
	public Category(Element e) {
		this.name = e.getAttribute("Name");
		this.bertType = Integer.parseInt(e.getAttribute("BertType"));
		this.estimatedLoad = Integer.parseInt(e.getAttribute("EstimatedLoad"));
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

	public static Category[] DEFAULT_CATEGORIES = {
			new Category("Projector", 15, 12),
			new Category("VendingMachine", 220, 350),
			new Category("Printer", 220, 350),
			new Category("hotWaterHeater", 220, 350),
			new Category("cat1", 220, 350),
			new Category("cat2", 220, 350),
			new Category("cat3", 220, 350)
	};
}

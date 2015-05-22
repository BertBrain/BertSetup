package bert.data.proj;

import java.util.Arrays;
import java.util.List;

public class Category {

    public final static int UNSET = -1;

	private String name;
	private int bertTypeID;
	private int estimatedLoad;
	
	/**
	 * @param name Name of device
	 * @param bertTypeID
	 * @param estimatedLoad in watts
	 */
	public Category(String name, int bertTypeID, int estimatedLoad) {
		this.name = name;
		this.bertTypeID = bertTypeID;
		this.estimatedLoad = estimatedLoad;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String newName) {
		this.name = newName;
	}

	public int getBertTypeID() {
		return bertTypeID;
	}

	public void setBertTypeID(int id) {
		this.bertTypeID = id;
	}

	/**
	 * @return estimated power consumption of device in watts
	 */
	public int getEstimatedLoad() {
		return estimatedLoad;
	}

	public void setEstimatedLoad(int newEstimatedLoad) {
		this.estimatedLoad = newEstimatedLoad;
	}

	public static List<String> bertTypes = Arrays.asList("Undecided", "Smart Plug 15 Amp", "Smart Plug 20 Amp", "Inline");
    public static List<Integer> bertTypeCosts = Arrays.asList(0, 80, 85, 100);
}
package bert.data.proj;

import java.util.Arrays;
import java.util.List;

public class Category {

    public static final int UNSET = -1;

	private int bertTypeID;
	private int estimatedLoad;
	
	/**
	 * @param bertTypeID
	 * @param estimatedLoad in watts
	 */
	public Category(int bertTypeID, int estimatedLoad) {
		this.bertTypeID = bertTypeID;
		this.estimatedLoad = estimatedLoad;
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
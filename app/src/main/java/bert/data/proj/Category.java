package bert.data.proj;

import java.util.Arrays;
import java.util.List;

public class Category {

    public static final int UNSET_ESTIMATED_LOAD = -1;

	private int bertTypeID;
	private int estimatedLoad;
	private boolean requiresExtensionCord;
	
	/**
	 * @param bertTypeID
	 * @param estimatedLoad in watts
     * @param requiresExtensionCord true if there are multiple devices in this category
	 */
	public Category(int bertTypeID, int estimatedLoad, boolean requiresExtensionCord) {
		this.bertTypeID = bertTypeID;
		this.estimatedLoad = estimatedLoad;
        this.requiresExtensionCord = requiresExtensionCord;
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

    public boolean doesRequireExtensionCord() {
        return requiresExtensionCord;
    }

    public void setRequiresExtensionCord(boolean requiresExtensionCord) {
        this.requiresExtensionCord = requiresExtensionCord;
    }

	public static List<String> bertTypes = Arrays.asList("Undecided", "Smart Plug 15 Amp", "Smart Plug 20 Amp", "Inline");
    public static List<Integer> bertTypeCosts = Arrays.asList(0, 80, 85, 100);
}
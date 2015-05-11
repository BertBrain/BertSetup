package bert.data.proj;

import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Category implements Serializable{

    public final static int UNSET = -1;

	private final String name;
	private int bertTypeID;
	private int estimatedLoad;
	
	//FIXME bertType should be an enum
	/**
	 *
	 * @param name Name of device
	 * @param bertTypeID
	 * @param estimatedLoad in watts
	 */
	public Category(String name, int bertTypeID, int estimatedLoad) {
		this.name = name;
		this.bertTypeID = bertTypeID;
		this.estimatedLoad = estimatedLoad;
	}


	public Category(Element e) {
		this.name = e.getAttribute("Name");
		this.bertTypeID = Integer.parseInt(e.getAttribute("BertType"));
		this.estimatedLoad = Integer.parseInt(e.getAttribute("EstimatedLoad"));
	}
	
	public String getName() {
		return name;
	}
	
	public int getBertTypeID() {
		return bertTypeID;
	}
	
	/**
	 * @return estimated power consumption of device in watts
	 */
	public int getEstimatedLoad() {
		return estimatedLoad;
	}

	public static Category[] DEFAULT_CATEGORIES = {
			new Category("Projector", 1, 12),
			new Category("VendingMachine", 2, 350),
			new Category("Printer", 0, 350),
			new Category("hotWaterHeater", 3, 350)
	};

	public static List<String> bertTypes = Arrays.asList("WALL_15", "WALL_20", "INLINE", "UNDEFINED");
}


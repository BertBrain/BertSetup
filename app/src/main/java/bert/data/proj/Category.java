package bert.data.proj;

import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Category implements Serializable {

    public final static int UNSET = -1;

	private String name;
	private int bertTypeID;
	private int estimatedLoad;
	
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

	public static Category[] DEFAULT_CATEGORIES = {
			new Category("Projector", 3, 0),
			new Category("Smart Board", 3, 0),
			new Category("Printer Monitor Combo", 3, 0),
			new Category("TV", 3, 0),
			new Category("Amplifier SmartBoard", 3, 0),
			new Category("Medium Priner", 3, 0),
			new Category("Large Printer/Copier 110V", 3, 0),
			new Category("Large Printer/Copier 220V", 3, 0)
	};

	public static List<String> bertTypes = Arrays.asList("WALL_15", "WALL_20", "INLINE", "UNDEFINED");
}
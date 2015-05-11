package bert.database;

import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable{

    public final static int UNSET = -1;

	private final String name;
	private BertType bertType;
	private int estimatedLoad;
	
	//FIXME bertType should be an enum
	/**
	 * 
	 * @param name Name of device
	 * @param bertType 15A Bert, 20A Bert, 220V Bert
	 * @param estimatedLoad in watts
	 */
	public Category(String name, BertType bertType, int estimatedLoad) {
		this.name = name;
		this.bertType = bertType;
		this.estimatedLoad = estimatedLoad;
	}


	public Category(Element e) {
		this.name = e.getAttribute("name");
        //TODO: make this pull from file
		//this.bertType = Integer.parseInt(e.getAttribute("bertType"));
		this.estimatedLoad = Integer.parseInt(e.getAttribute("estimatedLoad"));
	}
	
	public String getName() {
		return name;
	}
	
	public BertType getBertType() {
		return bertType;
	}
	
	/**
	 * @return estimated power consumption of device in watts
	 */
	public int getEstimatedLoad() {
		return estimatedLoad;
	}
	
	public static Category projector = new Category("Projector", BertType.WALL_20, 12);
	public static Category vendingMachine = new Category("VendingMachine", BertType.INLINE, 350);
    public static Category printer = new Category("Printer", BertType.WALL_15, 350);
    public static Category hotWaterHeater = new Category("hotWaterHeater", BertType.WALL_15, 350);

    public enum BertType implements Serializable{
        WALL_15, WALL_20, INLINE, UNDEFINED;

        public String toString(){
            switch (this){
                case  WALL_15:
                    return "Wall, 15 A";
                case  WALL_20:
                    return "Wall, 20 A";
                case UNDEFINED:
                    return "To Be Determined";
                case INLINE:
                    return "Inline";
                default:
                    return "Error in bert type desciption";
            }
        }
        public static List<BertType> getBertTypes(){
            List<BertType> bertTypes = new ArrayList<BertType>();
            bertTypes.add(WALL_15);
            bertTypes.add(WALL_20);
            bertTypes.add(INLINE);
            bertTypes.add(UNDEFINED);
            return  bertTypes;
        }
    }
}


package bert.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author afiol-mahon
 */
public class Project {
	String projectName;
	String contactName;
	String contactNumber;
	String creationDate;
	String modifiedDate;
	List<BertUnit> berts;
	List<Category> categories;
	
	public Project(String name, String contactName, String contactNumber) {
		this.projectName = name;
	    this.contactName = contactName;
	    this.contactNumber = contactNumber;
	    this.creationDate = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
	    this.modifiedDate = creationDate;
	    berts = new ArrayList<BertUnit>();
	    categories = Arrays.asList(Category.projector, Category.vendingMachine, Category.printer);
	}
	  
	public Project(Document document) {
	    Element projectTag = (Element) document.getElementsByTagName("Project").item(0);
	    this.projectName = projectTag.getAttribute("projectName");
	    this.creationDate = projectTag.getAttribute("creationDate");
	    this.modifiedDate = projectTag.getAttribute("modifiedDate");
	    
	    Element contactTag = (Element) document.getElementsByTagName("Contact").item(0);
	    this.contactName = contactTag.getAttribute("contactName");
	    this.contactNumber = contactTag.getAttribute("contactNumber");
	    
	    berts = new ArrayList<BertUnit>();
	    NodeList bertNodeList = document.getElementsByTagName("Bert");
	    for (int i = 0; i < bertNodeList.getLength(); i++) {
	    	Element e = (Element) bertNodeList.item(i);
	    	berts.add(new BertUnit(e));
	    }
	    
	    categories = new ArrayList<Category>();
	    NodeList categoryNodeList = document.getElementsByTagName("Category");
	    for (int i = 0; i < categoryNodeList.getLength(); i++) {
	    	Element e = (Element) categoryNodeList.item(i);
			categories.add(Integer.parseInt(e.getAttribute("id")), new Category(e));
	    }
	}
	  
	public Document exportToXML() {
		Document projectDoc = FileHelper.createDocument();
	 	Element root = projectDoc.createElement("Project");	 	
	 	//Project
	 	root.setAttribute("projectName", projectName);
	 	root.setAttribute("creationDate", creationDate);
	 	root.setAttribute("modifiedDate", new SimpleDateFormat("MM-dd-yyyy").format(new Date()));
	 	//Client
	 	Element client = projectDoc.createElement("Contact");
	 	client.setAttribute("contactName", contactName);
	 	client.setAttribute("contactNumber", contactNumber);
	 	root.appendChild(client);
	 	//BertList
		Element bertElementList = projectDoc.createElement("BertList");
		for (BertUnit b : berts) {
			//Bert
			Element bertElement = projectDoc.createElement("Bert");
			bertElement.setAttribute("name", b.getName());
			bertElement.setAttribute("location", b.getLocation());
			bertElement.setAttribute("building", b.getBuilding());
			bertElement.setAttribute("MAC", b.getMAC());
			bertElement.setAttribute("category", String.valueOf(b.getCategoryID()));
			bertElementList.appendChild(bertElement);
		}
		root.appendChild(bertElementList);
		
		Element categoryList = projectDoc.createElement("Categories");
		for (int i = 0; i < categories.size(); i++) {
			Element cat = projectDoc.createElement("Category");
			Category c = categories.get(i);
			cat.setAttribute("name", c.getName());
			cat.setAttribute("bertType", String.valueOf(c.getBertType()));
			cat.setAttribute("estimatedLoad", String.valueOf(c.getEstimatedLoad()));
			cat.setAttribute("id", String.valueOf(i));//Stores index so that list can be recreated accurately when xml is parsed
			categoryList.appendChild(cat);
		}
		root.appendChild(categoryList);
		projectDoc.appendChild(root);
        return projectDoc;
	}
	  
	public List<String> getCategoryNames() {
		List<String> categoryNames = new ArrayList<String>();
		for (BertUnit b : berts) {
			String catName = categories.get(b.getCategoryID()).getName();
			if (!categoryNames.contains(catName)) {
				categoryNames.add(catName);
			}
		}
		return categoryNames;
	}

    public String getProjectName() {
        return projectName;
    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<Category>();
        for (BertUnit b : berts) {
            Category cat = this.categories.get(b.getCategoryID());
            if (!categories.contains(cat)) {
                categories.add(cat);
            }
        }
        return categories;
    }

    public List<String> getLocationNames() {
        List<String> locationNames = new ArrayList<String>();
        for (BertUnit b : berts) {
            String n = b.getLocation();
            if (!locationNames.contains(n)) {
                locationNames.add(n);
            }
        }
        return locationNames;
    }

    public List<String> getBuildingNames() {
        List<String> buildingNames = new ArrayList<String>();
        for (BertUnit b : berts) {
            String n = b.getBuilding();
            if (!buildingNames.contains(n)){
                buildingNames.add(n);
            }
        }
        return buildingNames;
    }

    //FIXME Currently ignores buildings reimplement soon
    public List<BertUnit> getBertsByLocation(String building, String location) {
        List<BertUnit> returnList = new ArrayList<BertUnit>();
        for (BertUnit b : berts) {
            if (/*b.getBuilding() == building && */b.getLocation() == location) {
                returnList.add(b);
            }
        }
        return returnList;
    }

	public void exportBertConfiguratorCSV() {
	    //TODO write
	}
	
	public void exportROI() {
	    //TODO write
	}
	  
	public void addBert(BertUnit bert) {
		berts.add(bert);
	}

    public void addBerts(List<BertUnit> berts){
        for (BertUnit b : berts){
            addBert(b);
        }
    }
}
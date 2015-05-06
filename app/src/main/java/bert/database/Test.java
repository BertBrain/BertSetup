package bert.database;

/**
 * @author afiol-mahon
 */
public class Test {
    public static Project testProject = new Project("DebugProject", "Joe Smith", "555-555-5555");
    static {
        testProject.addBert(new BertUnit("Debug1", "1", "DebugBuilding", 0));
    }

	public static void main(String[] args) {
	    testProject.exportToXML();
	}
}

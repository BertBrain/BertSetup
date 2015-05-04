package bert.database;

/**
 * @author afiol-mahon
 */
public class Test {
    public static Project testProject = new Project("Client", "Joe Smith", "555-555-5555");
    static {
        testProject.addBert(new BertUnit("Projector1", "102", "Science", 0));
        testProject.addBert(new BertUnit("vendingMachine", "245", "Caf", 1));
        testProject.addBert(new BertUnit("bert4", "231", "Math", 1));
        testProject.addBert(new BertUnit("bert5", "231", "Math", 1));
        testProject.addBert(new BertUnit("bert6", "231", "Math", 1));
        testProject.addBert(new BertUnit("bert7", "245", "Caf", 1));
    }

	public static void main(String[] args) {		
	    testProject.exportToXML();
	}
}

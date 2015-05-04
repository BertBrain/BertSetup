package bert.database;

/**
 * @author afiol-mahon
 */
public class Test {
    public static Project testProject = new Project("Client", "Joe Smith", "555-555-5555");
    static {
        testProject.addBert(new BertUnit("Projector1", "1", "Math", 0));
        testProject.addBert(new BertUnit("vendingMachine", "1", "Math", 1));
        testProject.addBert(new BertUnit("bert4", "1", "Math", 1));
        testProject.addBert(new BertUnit("bert5", "1", "Math", 1));
        testProject.addBert(new BertUnit("bert6", "1", "Math", 1));
        testProject.addBert(new BertUnit("bert7", "1", "Math", 1));
        testProject.addBert(new BertUnit("bert8", "1", "Math", 0));
        testProject.addBert(new BertUnit("bert9", "1", "Math", 1));
        testProject.addBert(new BertUnit("bert10", "1", "Math", 1));
        testProject.addBert(new BertUnit("bert11", "1", "Math", 1));
        testProject.addBert(new BertUnit("bert12", "1", "Math", 1));
        testProject.addBert(new BertUnit("bert13", "1", "Math", 1));
        testProject.berts.get(0).setMAC("12345");
    }

	public static void main(String[] args) {		
	    testProject.exportToXML();
	}
}

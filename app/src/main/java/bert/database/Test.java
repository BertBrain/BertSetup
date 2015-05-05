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
        testProject.addBert(new BertUnit("bert6", "1", "Math", 2));
        testProject.addBert(new BertUnit("bert7", "1", "Math", 1));
        testProject.addBert(new BertUnit("bert8", "1", "Math", 0));
        testProject.addBert(new BertUnit("bert9", "1", "Math", 1));
        testProject.addBert(new BertUnit("bert10", "1", "Math", 1));
        testProject.addBert(new BertUnit("bert11", "1", "Math", 1));
        testProject.addBert(new BertUnit("bert12", "1", "Math", 2));
        testProject.addBert(new BertUnit("bert13", "1", "Math", 1));
        testProject.addBert(new BertUnit("bert14", "2", "Math", 1));
        testProject.addBert(new BertUnit("bert15", "3", "Math", 2));
        testProject.addBert(new BertUnit("bert16", "4", "Math", 1));
        testProject.addBert(new BertUnit("bert17", "5", "Math", 2));
        testProject.addBert(new BertUnit("bert18", "6", "Math", 1));
        testProject.addBert(new BertUnit("bert19", "7", "Math", 1));
        testProject.addBert(new BertUnit("bert20", "8", "Math", 1));
        testProject.addBert(new BertUnit("bert21", "9", "Math", 1));
        testProject.addBert(new BertUnit("bert22", "10", "Math", 1));
        testProject.addBert(new BertUnit("bert23", "11", "Math", 1));
        testProject.addBert(new BertUnit("bert24", "12", "Math", 1));


        testProject.berts.get(0).setMAC("12345");
    }

	public static void main(String[] args) {		
	    testProject.exportToXML();
	}
}
